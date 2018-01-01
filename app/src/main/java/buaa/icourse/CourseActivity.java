package buaa.icourse;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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
    private Queue<ResourceItem> localResourceItemQueue = new ArrayDeque<>();
    private String courseCode;
    private SwipeRefreshLayout swipeRefresh;

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
        courseCode = getIntent().getStringExtra("Course_code");
        initResources();
        RecyclerView recyclerView = findViewById(R.id.home_recycler_view2);
        final ProgressBar bar = findViewById(R.id.fragment_home_progress_bar2);
        //网格布局,设置为1列
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new ResourceAdapter(resourceItemList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = findViewById(R.id.swipe_refresh2);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.
                        OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshResources();
                    }
                }
        );
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        layoutManager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    bar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 5 && localResourceItemQueue.size() > 0; i++) {
                                resourceItemList.add(localResourceItemQueue.poll());
                            }
                            adapter.notifyDataSetChanged();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
            }
        });
        TextView no_resource = findViewById(R.id.no_resource);
        if (resourceItemList.size()==0 && localResourceItemQueue.size()==0) {
            no_resource.setVisibility(View.VISIBLE);
        }
    }

    void initResources() {
        localResourceItemQueue.clear();
        resourceItemList.clear();
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
            JSONArray jsonArray = new JSONArray(responseString);
            int siz = jsonArray.length();
            for (int i = 0; i < siz; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String resourceType = unicodeToString(object.getString("resourceType"));
                String url = object.getString("url");
                String name = unicodeToString(object.getString("resourceName"));
                String intro = unicodeToString(object.getString("intro"));
                String username = unicodeToString(object.getString("username"));
                int downloadCount = object.getInt("downloadCount");
                double evaluation = object.getDouble("evaluation");
                ResourceItem ri = new ResourceItem(name, resourceType, url,
                        intro, username, downloadCount, evaluation
                );
                localResourceItemQueue.add(ri);
            }
            for (int i = 0; localResourceItemQueue.size() > 0 && i < 10; i++) {
                resourceItemList.add(localResourceItemQueue.poll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void refreshResources() {
        //下滑刷新页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CourseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initResources();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}
