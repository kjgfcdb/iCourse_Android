package buaa.icourse;

import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class ResourceDetail extends AppCompatActivity {
    /**
     * 详情展示页,用于展示资源的详细信息
     */
    public static final String RESOURCE_NAME = "resource_name";
    public static final String RESOURCE_TYPE = "resource_type";
    public static final String RESOURCE_INFO = "resource_info";
    public static final String RESOURCE_UPLOADER = "resource_uploader";
    public static final String RESOURCE_DOWNLOAD_COUNT = "resource_download_count";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_detail);
        Intent intent = getIntent();
        //获取资源名以及资源类型
        String resourceName = intent.getStringExtra(RESOURCE_NAME);
        String resourceType = intent.getStringExtra(RESOURCE_TYPE);
        String resourceInfo = intent.getStringExtra(RESOURCE_INFO);
        String resourceUploader = intent.getStringExtra(RESOURCE_UPLOADER);
        int resourceDownloadCount = intent.getIntExtra(RESOURCE_DOWNLOAD_COUNT,0);
        //获取工具栏
        Toolbar toolbar = findViewById(R.id.resource_detail_toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView resourceImage = findViewById(R.id.resource_detail_image);
        TextView resourceDetailText = findViewById(R.id.resource_detail_text);
        TextView resourceUploaderView = findViewById(R.id.resource_detail_uploader_name);
        TextView resourceDownloadView = findViewById(R.id.resource_down_count);

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
        Button uploadRating = findViewById(R.id.resource_detail_uploadRating);
        uploadRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUploadRating(starRating.getRating());
            }
        });

        //下载文件
        Button downloadFile = findViewById(R.id.resource_detail_download);
        downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDownloadFile("http://course.buaa.edu.cn/access/content/group/0544583d-3cb5-4902-bb71-78e035c9918e/%E8%AF%BE%E4%BB%B6/%E8%AE%A1%E7%AE%97%E6%9C%BA%E8%BD%AF%E4%BB%B6%E6%8A%80%E6%9C%AF%E5%9F%BA%E7%A1%80-04%20%E5%85%B3%E7%B3%BB%E6%95%B0%E6%8D%AE%E5%BA%93%E5%9F%BA%E7%A1%803.pdf");
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
            Toast.makeText(getApplicationContext(), "感谢你的评价！",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "网络异常，请稍后再试。",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void doDownloadFile(String url) {
        // 调用系统自带下载器下载文件
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("/download/", url.substring(url.lastIndexOf("/")));
        DownloadManager downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            assert downloadManager != null;
            downloadManager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to download",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
