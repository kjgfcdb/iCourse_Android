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
    static Map c_pictures = new HashMap();

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
        pictures.put("doc",R.drawable.vector_drawable_file_doc);
        pictures.put("pdf",R.drawable.vector_drawable_pdf);
        pictures.put("xls",R.drawable.vector_drawable_file_xls);
        pictures.put("ppt",R.drawable.vector_drawable_file_ppt);
        for (int i = 1; i < 100; ++i){
            c_pictures.put(i, R.drawable.school_logo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT> 9) {
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

        adapter.addFragment(HomeFragment.newInstance());
        adapter.addFragment(CategoryFragment.newInstance());
        adapter.addFragment(SearchFragment.newInstance());
        adapter.addFragment(UploadFragment.newInstance());
        adapter.addFragment(UserFragment.newInstance());
        viewPager.setAdapter(adapter);
    }
}
