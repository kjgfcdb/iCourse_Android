package buaa.icourse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadFragment extends Fragment {
    //static String uploadUrl = "http://39.106.60.94:8080/Hello/HelloWorld";
    static String uploadUrl1 = "http://39.106.60.94:8080/Hello/HelloWorld";
    static String uploadUrl = "http://10.2.28.124:8080/Hello/HelloWorld";// "http://10.2.28.124:8080/dir/2017/12/";
    private static final int SUCCESS = 2;//状态识别码
    private static final int FAILD = 3;
    private TextView fileNameTextView; // 文件名

    private String path;//本地文件路径
    private Uri globalUri;

    public UploadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        fileNameTextView = view.findViewById(R.id.filename);
        Button uploadBrowse = view.findViewById(R.id.upload_browse);
        Button uploadUpload = view.findViewById(R.id.upload_upload);

        uploadBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);//调用系统资源管理器帮助我们选择文件
                startActivityForResult(intent, 1);
            }
        });

        uploadUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        return view;
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    path = "";
                    fileNameTextView.setText(path);
                    Toast.makeText(getContext(), "上传成功！", Toast.LENGTH_LONG).show();
                    break;
                case FAILD:
                    Toast.makeText(getContext(), "上传失败！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private void uploadFile() {
        // !!!!!!! 2017.12.18 由于时间原因暂时阉割此功能。
        /*
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                // 服务器的访问路径
                Map<String, File> files = new HashMap<String, File>();

                String name = path.substring(path.lastIndexOf("/")+1,path.length());
                files.put(name, new File(path));
                try {
                    HttpUtil.postFile(uploadUrl1, new HashMap<String, String>(), files);
//                    RequestBody requestBody = new MultipartBody.Builder()
//                            .setType(MultipartBody.FORM)
//                            .addFormDataPart("file", name,
//                                    RequestBody.create(MediaType.parse("image/jpg"), new File(path)))
//                            .addFormDataPart("filename", name)
//                            .build();
//
//                    Request request = new Request.Builder()
//                            .url(uploadUrl)
//                            .post(requestBody)
//                            .build();
//                    OkHttpClient client = new OkHttpClient();
//                    Response response =  client.newCall(request).execute();
//                    System.out.println(response.body().string());
                    msg.what = SUCCESS;
                } catch (Exception e) {
                    msg.what = FAILD;
                }
                mHandler.sendMessage(msg);
            }
        }.start();
        */
    }

    public static UploadFragment newInstance() {
        return new UploadFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                globalUri = uri;
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                    fileNameTextView.setText(path);
                } else {
                    //尝试获取读权限
                    int permission = ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }, 1);
                    } else {
                        path = getPath(getContext(), uri);
                        fileNameTextView.setText(path);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                //在此处做文件枚举
                path = getPath(getContext(), globalUri);
                fileNameTextView.setText(path);
            }
        }
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
