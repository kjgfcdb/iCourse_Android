package buaa.icourse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private ResourceAdapter adapter;
    private List<ResourceItem> resourceItemList = new ArrayList<>();
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



        System.out.println("$$#@$#@#@*&($(");

        String courseCode = getIntent().getStringExtra("Course_code");

        ResourceItem ri;
        SolrQuery st = new SolrQuery();
        JSONArray ja;
        double score;
        String name;

        System.out.println("@@@@@@@@@@@@s");

        adapter = new ResourceAdapter(resourceItemList);

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

                if (i > 5)
                    break;

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
        }

    }

}
