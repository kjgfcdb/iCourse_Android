package buaa.icourse;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class SearchFragment extends Fragment {
    /**
     * 搜索页面，提供资源检索
     */
    private CourseAdapter adapter;
    private List<CourseItem> courseItemList = new ArrayList<>();
    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initArgs(view);

        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_search);
        //网格布局,设置为1列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new CourseAdapter(courseItemList);
        recyclerView.setAdapter(adapter);

        Button bt = view.findViewById(R.id.query_button);
        bt.setOnClickListener(new View.OnClickListener() {
            public static final String TAG = "Search";
            CourseItem ri;
            SolrQuery st = new SolrQuery();
            JSONArray ja;
            double score;
            String name, course_code;
            int college_id;

            @Override
            public void onClick(View view)  {
                courseItemList.clear();
                EditText et = (EditText) getActivity().findViewById(R.id.query_content);
                String str = et.getText().toString();
                Log.d(TAG, "!!!!!Query: "+str);
                try {
                    ja = st.work(str, 1);
                    for (int i = 0; i < min(10, ja.length()); ++i) {
                        JSONObject courseData = ja.getJSONObject(i);
                        score = courseData.getDouble("score");
                        //if (score < 5)
                        //    continue;
                        name = (String)courseData.get("name");
                        System.out.println("%%%"+name);
                        college_id = courseData.getInt("college_id");
                        course_code = (String)courseData.get("course_code");
                        String course_teacher = courseData.getString("course_teacher");
                        String course_credit = courseData.getString("course_credit");
                        String course_type = courseData.getString("course_type");
                        System.out.println("???"+course_code);
                        ri = new CourseItem(name, course_code, college_id,course_teacher,course_credit,course_type);
                        courseItemList.add(ri);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void initArgs(View view) {
        Bundle bundle = getArguments();
    }

    public static SearchFragment newInstance() {
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }
}
