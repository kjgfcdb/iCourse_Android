package buaa.icourse;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class CourseActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        tabLayout = findViewById(R.id.course_tab);
        viewPager = findViewById(R.id.course_viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),new String[]{"课程介绍","资源列表","讨论区"});
        adapter.addFragment(new course_intro_fragment());
        adapter.addFragment(new course_list_fragment(getIntent().getStringExtra("Course_code")));
        adapter.addFragment(new course_forum_fragment());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
