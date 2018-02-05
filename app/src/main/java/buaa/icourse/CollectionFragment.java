package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class CollectionFragment extends Fragment {
    /**
     * 收藏界面，用于显示收藏信息
     */

    private CourseAdapter adapter;
    private List<CourseItem> courseItemList = new ArrayList<>();
    private Queue<CourseItem> localCourseItemQueue = new ArrayDeque<>();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private View view;
    private CourseItem ri;
    private JSONArray ja;
    private String name, course_code, course_teacher, course_credit, course_type;
    private int college_id;
    private SwipeRefreshLayout swipeRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection, container, false);
        initCourses();
        bindViews();
        return view;
    }
    private void initCourses(){
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

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Request request = new Request.Builder()
                .url(UploadFragment.uploadUrl)
                .post(new FormBody.Builder()
                        .add("collect", "1")
                        .add("studentNo",pref.getString("sid", "$$$"))
                        .build()
                ).build();
        OkHttpClient client = new OkHttpClient();
        //Log.i("yaoling1997","hh");
        try {
            String responseString = client.newCall(request).execute().body().string();
            //Log.i("yaoling1997","responseString: "+responseString);
            ja = new JSONArray(responseString);
            Log.i("yaoling1997","ja length: "+ja.length());
            for (int i=0;i<ja.length();i++){
                Log.i("yaoling1997",ja.get(i).toString());
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
                    Log.e(TAG, "!!!"+course_credit+"!!!");
                    if (course_credit.equals("0.0")||course_credit.equals("0")) {
                        course_credit = "";
                        Log.e(TAG, course_credit);
                    }
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
                localCourseItemQueue.add(ri);
            }
            for (int i = 0; localCourseItemQueue.size() > 0 && i < 10; i++) {
                courseItemList.add(localCourseItemQueue.poll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void bindViews(){
        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view2);
        final ProgressBar bar = view.findViewById(R.id.fragment_home_progress_bar2);
        //网格布局,设置为1列
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new CourseAdapter(courseItemList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = view.findViewById(R.id.swipe_refresh2);
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
        TextView no_resource = view.findViewById(R.id.no_resource);
        if (courseItemList.size()==0 && localCourseItemQueue.size()==0) {
            no_resource.setVisibility(View.VISIBLE);
        }

    }
//    private static String unicodeToString(String str) {
//        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");
//        Matcher matcher = pattern.matcher(str);
//        char ch;
//        while (matcher.find()) {
//            //group 6728
//            String group = matcher.group(2);
//            //ch:'木' 26408
//            ch = (char) Integer.parseInt(group, 16);
//            //group1 \u6728
//            String group1 = matcher.group(1);
//            str = str.replace(group1, ch + "");
//        }
//        return str;
//    }
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

                getActivity().runOnUiThread(new Runnable() {
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

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }
}
