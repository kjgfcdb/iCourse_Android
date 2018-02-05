package buaa.icourse;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class CourseActivity extends AppCompatActivity {
    public static final String TAG = "CourseActivity";
    private EditText sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        TabLayout tabLayout = findViewById(R.id.course_tab);
        ViewPager viewPager = findViewById(R.id.course_viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),new String[]{"资源列表","课程介绍","讨论区"});

        adapter.addFragment(new course_list_fragment(getIntent().getStringExtra("Course_code")));
        adapter.addFragment(new course_intro_fragment());
        adapter.addFragment(new course_forum_fragment());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        final String courseCode = intent.getStringExtra("Course_code");
        final String courseName = intent.getStringExtra("Course_name");
        final String courseCollege = intent.getStringExtra("Course_college");

        Button courseUpload = findViewById(R.id.course_upload);

        courseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "FUUUUUUUUUUUUUU");
                Intent uploadIntent = new Intent(CourseActivity.this, UploadActivity.class);
                Log.d(TAG, "********"+courseCode);
                uploadIntent.putExtra("Course_code", courseCode);
                uploadIntent.putExtra("Course_name", courseName);
                uploadIntent.putExtra("Course_college", courseCollege);
                startActivity(uploadIntent);
            }
        });

        //课程收藏功能
        final Button courseCollect = findViewById(R.id.course_collect);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String Sid = pref.getString("sid", "$$$");
        Request request = new Request.Builder()
                .url(UploadFragment.uploadUrl)
                .post(new FormBody.Builder()
                        .add("isCollect", "1")
                        .add("studentNo", Sid)
                        .add("Course_code", courseCode)
                        .build()
                ).build();

        try {
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(3, TimeUnit.SECONDS) //连接超时
                    .readTimeout(4, TimeUnit.SECONDS) //读取超时
                    .writeTimeout(5, TimeUnit.SECONDS) //写超时
                    .build();

            String responseString = client.newCall(request).execute().body().string();
            System.out.println("@@" + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jo = jsonArray.getJSONObject(0);
            //new JSONObject(responseString);
            String res = jo.getString("res");
            System.out.println("RES::::::" + res);
            if (res.equals("yes")){
                courseCollect.setText("取消收藏");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        courseCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseCollectText = (String)courseCollect.getText();
                Log.e(TAG, "COOOOOOOOOO"+courseCollectText);
                int toCollect = 2;
                if (courseCollectText.equals("收藏该课程"))
                    toCollect = 1;
                Request request = new Request.Builder()
                        .url(UploadFragment.uploadUrl)
                        .post(new FormBody.Builder()
                                .add("toCollect", ""+toCollect)
                                .add("studentNo", Sid)
                                .add("Course_code", courseCode)
                                .build()
                        ).build();

                try {
                    OkHttpClient client = new OkHttpClient.Builder().
                            connectTimeout(3, TimeUnit.SECONDS) //连接超时
                            .readTimeout(4, TimeUnit.SECONDS) //读取超时
                            .writeTimeout(5, TimeUnit.SECONDS) //写超时
                            .build();

                    String responseString = client.newCall(request).execute().body().string();
                    System.out.println("@@" + responseString);
                    JSONArray jsonArray = new JSONArray(responseString);
                    JSONObject jo = jsonArray.getJSONObject(0);
                    //new JSONObject(responseString);
                    String res = jo.getString("res");
                    System.out.println("RES::::::" + res);
                    if (res.equals("success")){
                        if (toCollect == 1){
                            courseCollect.setText("取消收藏");
                        }else{
                            courseCollect.setText("收藏该课程");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(),
                    //      "登录失败!请检查网络环境是否为校园网!", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        });
    }
}
