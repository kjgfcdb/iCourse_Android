package buaa.icourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseActivity extends AppCompatActivity {

    private static final String TAG = "CourseActivity";
    private ResourceAdapter adapter;
    private List<ResourceItem> resourceItemList = new ArrayList<>();

    private static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course2);

        RecyclerView recyclerView = findViewById(R.id.home_recycler_view2);
        //网格布局,设置为1列
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new ResourceAdapter(resourceItemList);
        recyclerView.setAdapter(adapter);

        String courseCode = getIntent().getStringExtra("Course_code");
        ResourceItem ri;
        String name, passwd, resourceType, url, intro, username;
        int siz, downloadCount;
        adapter = new ResourceAdapter(resourceItemList);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UploadFragment.uploadUrl)
                    .post(new FormBody.Builder()
                            .add("CourseActivity", courseCode)
                            .build()
                    ).build();
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();

            String responseString = new String(bytes);
            Log.d(TAG, "initResources: " + bytes.length);
            Log.d(TAG, "initResources: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            siz = jsonArray.length();
            System.out.println("siz = " + siz);
            for (int i = 0; i < siz; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                resourceType = unicodeToString(object.getString("resourceName"));
                url = object.getString("url");
                name = unicodeToString(object.getString("resourceName"));
                intro = unicodeToString(object.getString("intro"));
                username = unicodeToString(object.getString("username"));
                downloadCount = object.getInt("downloadCount");
                ri = new ResourceItem(name, resourceType,url,
                        intro,username,downloadCount
                        );
                resourceItemList.add(ri);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){ e.printStackTrace(); }
    }

}
