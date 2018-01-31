package buaa.icourse;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {
    public static final String TAG = "UploadActivity";
    static String uploadUrl1 = "http://39.106.60.94:8080/Hello/HelloWorld";
    static String uploadUrl = "http://60.205.211.127:8080/androidServer/HelloWorld";// "http://60.205.211.127:8080/dir/2017/12/";
    private static final int SUCCESS = 2;//状态识别码
    private static final int FAILD = 3;
    private TextView fileNameTextView, courseNameTextView, courseCollegeTextView; // 文件名

    private String path;//本地文件路径
    private Uri globalUri;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload);
        Intent intent = getIntent();
        final String courseCode = intent.getStringExtra("Course_code");
        final String courseName = intent.getStringExtra("Course_name");
        final String courseCollege = intent.getStringExtra("Course_college");
        fileNameTextView = findViewById(R.id.filename);
        Button uploadBrowse = findViewById(R.id.upload_browse);
        Button uploadUpload = findViewById(R.id.upload_upload);
        final EditText editDescription = findViewById(R.id.description);
        courseNameTextView = findViewById(R.id.upload_course_name);
        courseCollegeTextView = findViewById(R.id.upload_college_name);
        courseNameTextView.setText(courseName);
        courseCollegeTextView.setText(courseCollege);
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
                Log.d(TAG, "@@@@@#####");
                uploadFile(courseCode, courseCollege, courseName, editDescription.getText().toString());
            }
        });
        return ;
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    path = "";
                    fileNameTextView.setText(path);
                    Toast.makeText(getApplicationContext(), "上传成功！", Toast.LENGTH_LONG).show();
                    break;
                case FAILD:
                    Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private void uploadFile(final String courseCode, final String courseCollege, final String courseName, final String intro) {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.e(TAG, intro);
        Log.e(TAG, pref.getString("sid", "$$$"));
        Log.e(TAG, pref.getString("password", "$$$"));

        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                // 服务器的访问路径
                Map<String, File> files = new HashMap<String, File>();
                Map<String, String> postPara = new HashMap<String, String>();
                try {
                    postPara.put("Course_code", URLEncoder.encode(courseCode, "UTF-8"));
                    postPara.put("Course_intro", intro);
                    postPara.put("Course_upload_user", pref.getString("sid", "$$$"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                String name = path.substring(path.lastIndexOf("/")+1,path.length());
                files.put(name, new File(path));
                try {
                    HttpUtil.postFile(uploadUrl, postPara, files); //new HashMap<String, String>()
                    msg.what = SUCCESS;
                } catch (Exception e) {
                    msg.what = FAILD;
                }
                mHandler.sendMessage(msg);
            }
        }.start();
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
                    int permission = ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE
                                }, 1);
                    } else {
                        path = getPath(getApplicationContext(), uri);
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
                path = getPath(getApplicationContext(), globalUri);
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