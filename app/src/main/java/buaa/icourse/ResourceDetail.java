package buaa.icourse;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class ResourceDetail extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    public static final String TAG = "ResourceDetail";

//    ProgressBar pb;
//    TextView tv;
//    int   fileSize;
//    int   downLoadFileSize;
//    String fileEx,fileNa,filename;
//    private Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {//定义一个Handler，用于处理下载线程与UI间通讯
//            if (!Thread.currentThread().isInterrupted())
//            {
//                switch (msg.what)
//                {
//                    case 0:
//                        pb.setMax(fileSize);
//                    case 1:
//                        pb.setProgress(downLoadFileSize);
//                        int result = downLoadFileSize * 100 / fileSize;
//                        tv.setText(result + "%");
//                        break;
//                    case 2:
//                        Toast.makeText(getApplicationContext(), "文件下载完成", 1).show();
//                        break;
//
//                    case -1:
//                        String error = msg.getData().getString("error");
//                        Toast.makeText(getApplicationContext(), error, 1).show();
//                        break;
//                }
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    private void sendMsg(int flag)
//    {
//        Message msg = new Message();
//        msg.what = flag;
//        handler.sendMessage(msg);
//    }

    /**
     * 详情展示页,用于展示资源的详细信息
     */
    public static final String RESOURCE_NAME = "resource_name";
    public static final String RESOURCE_TYPE = "resource_type";
    public static final String RESOURCE_INFO = "resource_info";
    public static final String RESOURCE_UPLOADER = "resource_uploader";
    public static final String RESOURCE_DOWNLOAD_COUNT = "resource_download_count";
    public static final String RESOURCE_URL = "resource_url";
    public static final String RESOURCE_EVALUATION = "evaluation";
    private String m_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_detail);
        Intent intent = getIntent();
        //获取资源名以及资源类型
        final String resourceName = intent.getStringExtra(RESOURCE_NAME);
        String resourceType = intent.getStringExtra(RESOURCE_TYPE);
        String resourceInfo = intent.getStringExtra(RESOURCE_INFO);
        String resourceUploader = intent.getStringExtra(RESOURCE_UPLOADER);
        final String resourceUrl = intent.getStringExtra(RESOURCE_URL);
        int resourceDownloadCount = intent.getIntExtra(RESOURCE_DOWNLOAD_COUNT, 0);
        double rating = intent.getDoubleExtra(RESOURCE_EVALUATION, 0);
        //获取工具栏
        Toolbar toolbar = findViewById(R.id.resource_detail_toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView resourceImage = findViewById(R.id.resource_detail_image);
        TextView resourceDetailText = findViewById(R.id.resource_detail_text);
        TextView resourceUploaderView = findViewById(R.id.resource_detail_uploader_name);
        TextView resourceDownloadView = findViewById(R.id.resource_down_count);

        Button button_evaluation = findViewById(R.id.resource_detail_uploadRating);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 设置显示内容
        collapsingToolbar.setTitle(resourceName);
        try {
            resourceImage.setImageResource((int) MainActivity.pictures.get(resourceType));
        } catch (Exception e) {
            resourceImage.setImageResource((int) MainActivity.pictures.get("file"));
        }
        resourceDetailText.setText(resourceInfo);
        resourceUploaderView.setText(resourceUploader);
        resourceDownloadView.setText(Integer.toString(resourceDownloadCount));

        //上传评分
        final RatingBar starRating = findViewById(R.id.resource_detail_rating);
//        Button uploadRating = findViewById(R.id.resource_detail_uploadRating);
//        uploadRating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doUploadRating(starRating.getRating());
//            }
//        });
        String tt = ""+rating;
        Log.e("EVALUTAITON",tt);
        if (rating != -1){ //代表有用戶评价过了
            button_evaluation.setText("该资源评分");
            starRating.setRating((float)rating);
        }else { //否则用户就可以自己评分
            Button uploadRating = findViewById(R.id.resource_detail_uploadRating);
            uploadRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doUploadRating(starRating.getRating());
                }
            });
        }

//        RatingBar ratingBar = findViewById(R.id.resource_detail_rating);
//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                float rat = 1.5f;
//                Log.e("------------","当前的评价等级："rating);
//                //ratingBar.setRating(rat);
//            }
//        });



        //下载文件
        Button downloadFile = findViewById(R.id.resource_detail_download);
        downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDownloadFile(resourceUrl, resourceName);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doUploadRating(float rating) {
        Request request = new Request.Builder()
                .url(UploadFragment.uploadUrl)
                .post(new FormBody.Builder()
                        .add("rating", Float.toString(rating))
                        .build()
                ).build();
        OkHttpClient client = new OkHttpClient();
        try {
            client.newCall(request).execute();

            //!!!!TOBEDONE

            Toast.makeText(getApplicationContext(), "感谢你的评价！",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "网络异常，请稍后再试。",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void doDownloadFile(String url, String resourceName) {
        // 调用系统自带下载器下载文件
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 1);
            m_url = url;
        } else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir("/download/", url.substring(url.lastIndexOf("/")));
            DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            try {
                assert downloadManager != null;
                long Id = downloadManager.enqueue(request);
                listener(Id, resourceName);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to download",
                        Toast.LENGTH_SHORT).show();
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
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(m_url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir("/download/", m_url.substring(m_url.lastIndexOf("/")));
                DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
                try {
                    assert downloadManager != null;
                    long Id = downloadManager.enqueue(request);

                    //listener(Id);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to download",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void listener(final long Id, final String resourceName) {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.e(TAG, "!!!"+ID);
                if (ID == Id) {
                    Toast.makeText(getApplicationContext(), "任务:" + resourceName + " 下载完成!", Toast.LENGTH_LONG).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

}