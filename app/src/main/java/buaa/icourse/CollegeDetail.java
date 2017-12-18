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
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Math.min;

public class CollegeDetail extends AppCompatActivity {
    public static final String TAG = "CollegeDetail";
    public static final String COLLEGE_ID = "college_id";

    private CourseAdapter adapter;
    //private List<ResourceItem> courseItemList = new ArrayList<>();
    private List<CourseItem> courseItemList = new ArrayList<>();
    private Queue<CourseItem> localCourseItemQueue = new ArrayDeque<>();
    private int collegeId;
    private SwipeRefreshLayout swipeRefresh;

    CourseItem ri;
    SolrQuery st = new SolrQuery();
    JSONArray ja;
    String name, course_code, course_teacher, course_credit, course_type;
    int college_id;

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
        collegeId = getIntent().getIntExtra(COLLEGE_ID,6);
        initCourses();
        RecyclerView recyclerView = findViewById(R.id.home_recycler_view2);
        final ProgressBar bar = findViewById(R.id.fragment_home_progress_bar2);
        //网格布局,设置为1列
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new CourseAdapter(courseItemList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = findViewById(R.id.swipe_refresh2);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.
                        OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshCourses();
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
                            for (int i = 0; i < 5 && localCourseItemQueue.size() > 0; i++) {
                                courseItemList.add(localCourseItemQueue.poll());
                            }
                            adapter.notifyDataSetChanged();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
            }
        });
        TextView no_resource = findViewById(R.id.no_resource);
        if (courseItemList.size()==0 && localCourseItemQueue.size()==0) {
            no_resource.setVisibility(View.VISIBLE);
        }
    }
    void initCourses() {
        final HashMap<String, String> mp_class_id = new HashMap<String, String>();
        mp_class_id.put("1", "工程基础类");
        mp_class_id.put("2", "数学与自然科学类");
        mp_class_id.put("3", "语言类");
        mp_class_id.put("4", "博雅类");
        mp_class_id.put("5", "核心通识类");
        mp_class_id.put("6", "体育类");
        mp_class_id.put("7", "一般通识类");
        mp_class_id.put("8", "核心专业类");
        mp_class_id.put("9", "一般专业类");

        localCourseItemQueue.clear();
        courseItemList.clear();
        try {
            ja = st.work(Integer.toString(collegeId), 0);
            for (int i = 0; i < ja.length(); ++i) {
                course_teacher = course_credit = course_type = "";

                JSONObject courseData = ja.getJSONObject(i);
                name = (String) courseData.get("name");
                System.out.println("%%%" + name);
                college_id = courseData.getInt("college_id");
                course_code = (String) courseData.get("course_code");
                if (courseData.has("teacher"))
                    course_teacher = "教师:"+courseData.getString("teacher");

                if (courseData.has("credit")) {
                    course_credit = courseData.getString("credit");
                    if (course_credit=="0.0")
                        course_credit = "";
                    else
                        course_credit += "学分";
                    Log.e(TAG, course_credit);
                }

                if (courseData.has("class_id")) {
                    course_type = courseData.getString("class_id");
                    course_type = mp_class_id.get(course_type);
                }
                System.out.println("???" + course_code);
                ri = new CourseItem(name, course_code, college_id,course_teacher,course_credit,course_type);
                //courseItemList.add(ri);
                localCourseItemQueue.add(ri);
                //adapter.notifyDataSetChanged();
            }
            for (int i = 0; localCourseItemQueue.size() > 0 && i < 10; i++) {
                courseItemList.add(localCourseItemQueue.poll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void refreshCourses() {
        //下滑刷新页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                CollegeDetail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initCourses();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}
