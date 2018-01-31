package buaa.icourse;

import android.app.Fragment;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class CourseActivity extends AppCompatActivity {
    public static final String TAG = "CourseActivity";

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

        Button courseCollect = findViewById(R.id.course_upload);
        courseCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "FUUUUUUUUUUUUUU");
                Intent uploadIntent = new Intent(CourseActivity.this, UploadActivity.class);

                Intent intent = getIntent();
                String courseCode = intent.getStringExtra("Course_code");
                String courseName = intent.getStringExtra("Course_name");
                String courseCollege = intent.getStringExtra("Course_college");

                Log.d(TAG, "********"+courseCode);

                uploadIntent.putExtra("Course_code", courseCode);
                uploadIntent.putExtra("Course_name", courseName);
                uploadIntent.putExtra("Course_college", courseCollege);

                startActivity(uploadIntent);
            }
        });
    }
}
