package buaa.icourse;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView navigation;
    static Map pictures = new HashMap();
    static Map collegePics = new HashMap();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.category:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.search:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.upload:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.user:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    static void initPictures() {
        pictures.put("doc", R.drawable.vector_drawable_file_doc);
        pictures.put("docx", R.drawable.vector_drawable_file_doc);
        pictures.put("pdf", R.drawable.vector_drawable_pdf);
        pictures.put("xls", R.drawable.vector_drawable_file_xls);
        pictures.put("xlsx", R.drawable.vector_drawable_file_xls);
        pictures.put("ppt", R.drawable.vector_drawable_file_ppt);
        pictures.put("pptx", R.drawable.vector_drawable_file_ppt);
        pictures.put("file", R.drawable.vector_drawable_file);
        pictures.put("jpg", R.drawable.vector_drawable_file_jpg);
        pictures.put("png", R.drawable.vector_drawable_file_png);
        pictures.put("gif", R.drawable.vector_drawable_file_gif);
        pictures.put("zip", R.drawable.vector_drawable_file_zip);
        pictures.put("rar", R.drawable.vector_drawable_file_zip);
        pictures.put("mp4", R.drawable.vector_drawable_file_video);
        pictures.put("MPG", R.drawable.vector_drawable_file_video);
        pictures.put("rmvb", R.drawable.vector_drawable_file_video);
        pictures.put("mpg", R.drawable.vector_drawable_file_video);
        pictures.put("avi", R.drawable.vector_drawable_file_video);
        pictures.put("swf", R.drawable.vector_drawable_file_video);

        collegePics.put(0, R.drawable.school_logo);
        collegePics.put(1, R.drawable.college_1);
        collegePics.put(2, R.drawable.college_2);
        collegePics.put(3, R.drawable.college_3);
        collegePics.put(4, R.drawable.college_4);
        collegePics.put(5, R.drawable.college_5);
        collegePics.put(6, R.drawable.college_6);
        collegePics.put(7, R.drawable.college_7);
        collegePics.put(8, R.drawable.college_8);
        collegePics.put(9, R.drawable.college_9);
        collegePics.put(10, R.drawable.college_10);
        collegePics.put(13, R.drawable.college_13);
        collegePics.put(15, R.drawable.college_15);
        collegePics.put(17, R.drawable.college_17);
        collegePics.put(19, R.drawable.college_19);
        collegePics.put(20, R.drawable.college_20);
        collegePics.put(21, R.drawable.college_21);
        collegePics.put(23, R.drawable.college_23);
        collegePics.put(24, R.drawable.college_24);
        collegePics.put(26, R.drawable.college_26);
        collegePics.put(27, R.drawable.college_27);
        collegePics.put(29, R.drawable.college_29);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initPictures();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        //ViewPager存放的是碎片
        viewPager = findViewById(R.id.fragment_container);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) menuItem.setChecked(false);
                else navigation.getMenu().getItem(0).setChecked(false);
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(CategoryFragment.newInstance());
        adapter.addFragment(HomeFragment.newInstance());
        adapter.addFragment(SearchFragment.newInstance());
        adapter.addFragment(UploadFragment.newInstance());
        adapter.addFragment(UserFragment.newInstance());
        viewPager.setAdapter(adapter);
    }
}
