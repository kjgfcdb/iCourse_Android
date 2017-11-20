package buaa.icourse;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.min;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context mContext;
    private List<CourseItem> mCourseList;
    public static final String TAG = "CourseAAAAdapter";

    //private ResourceAdapter adapter;
    //private List<ResourceItem> resourceItemList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        //资源图片以及资源名
        ImageView courseImage;
        TextView courseName;

        ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view;
            courseImage = view.findViewById(R.id.course_image);
            courseName = view.findViewById(R.id.course_item_name);
        }
    }

    CourseAdapter(List<CourseItem> CourseItemList) {
        mCourseList = CourseItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final String courseName, courseCode = "";

        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.course_item,
                parent, false);
       // RecyclerView recyclerView = view.findViewById(R.id.home_recycler_search);


        //final RecyclerView recV = view.findViewById(R.id.home_recycler_search);



        //recyclerView.setAdapter(adapter);

        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                CourseItem item = mCourseList.get(position);

                //Intent intent = new Intent(mContext, CourseDetail.class);


                //intent.putExtra(ResourceDetail.RESOURCE_NAME, );
                //intent.putExtra(ResourceDetail.RESOURCE_TYPE, item.getResourceType());

                String courseName  = item.getCourseName();
                int courseType = item.getCourseType();
                String courseCode = item.getCourseCode();

                Log.d(TAG, "!!!!!Query: "+courseName+"^^^^^^"+courseType+"*******"+courseCode);


                Intent intent = new Intent(mContext, CourseActivity.class);
                intent.putExtra("Course_code", courseCode);
                mContext.startActivity(intent);

                //RecyclerView recV = view.findViewById(R.id.home_recycler_search);
                //List<ResourceItem> resourceItemList = new ArrayList<>();
                //ResourceAdapter adapter = new ResourceAdapter(resourceItemList);
                //recV.setAdapter(adapter);

                /*
                ResourceItem ri;
                SolrQuery st = new SolrQuery();
                JSONArray ja;
                double score;
                String name;

                try {

                    Init_mysql con = new Init_mysql(0);

                    String cypher;
                    cypher = "select count(*) from backend_resource;";

//			cypher = "select *  from java_repositories where projectId >= 15032988 limit 5";


                    //"select *  from java_repositories limit 10;";
                    //"SELECT count(*) FROM java_repositories;";
                    String columnName = "name";
                    //"count(*)";
                    JSONArray ans;
                    ans = con.executeCypher(cypher, 1);


                    int cnt;
                    cnt = ans.getJSONObject(0).getInt("count(*)");
                    System.out.println("User cnt = " + cnt);

                    cypher = "select * from backend_resource where course_code='"
                            + courseCode + "';";

                    ans = con.executeCypher(cypher, cnt + 1);
                    con.close();

                    String nam, passwd;
                    int id, contribution;

                    int siz = ans.length();
                    System.out.println("siz = " + siz);

                    for (int i = 0; i < siz; i++) {
                        JSONObject job = ans.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        //System.out.println(columnName+" = "+job.get(columnName));
                        name = job.getString("name");
                        id = job.getInt("id");
                        System.out.println("id:" + id + " name:" +
                                name);

                        ri = new ResourceItem(name, "ppt");
                        resourceItemList.add(ri);
                        adapter.notifyDataSetChanged();

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*/

                //try {
                    //ja = st.work(str);
                    /*for (int i = 0; i < min(10, ja.length()); ++i) {
                        JSONObject courseData = ja.getJSONObject(i);
                        score = courseData.getDouble("score");
                        //if (score < 5)
                        //    continue;
                        name = (String)courseData.get("name");
                        ri = new ResourceItem(name, "ppt");
                        resourceItemList.add(ri);
                        adapter.notifyDataSetChanged();
                    }*/
                //}
                //catch (Exception e) {
                //    e.printStackTrace();
                //}


                //resourceItemList.clear();
//                EditText et = (EditText) getActivity().findViewById(R.id.query_content);
//                String str = et.getText().toString();
//                Log.d(TAG, "!!!!!Query: "+str);
//                try {
//                    ja = st.work(str);
//                    for (int i = 0; i < min(10, ja.length()); ++i) {
//                        JSONObject courseData = ja.getJSONObject(i);
//                        score = courseData.getDouble("score");
//                        //if (score < 5)
//                        //    continue;
//                        name = (String)courseData.get("name");
//                        college_id = courseData.getInt("college_id");
//                        ri = new CourseItem(name, college_id);
//                        courseItemList.add(ri);
//                        adapter.notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


//                Intent intent = new Intent(mContext, ResourceDetail.class);
//                intent.putExtra(ResourceDetail.RESOURCE_NAME, item.getCourseName());
//                intent.putExtra(ResourceDetail.RESOURCE_TYPE, item.getCourseType());
//
//
//
//
//
//                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseItem courseItem = mCourseList.get(position);
        holder.courseName.setText(courseItem.getCourseName());

        holder.courseImage.setImageResource((int)MainActivity.c_pictures.get(courseItem.getCourseType()));
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }
}
