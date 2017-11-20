//package buaa.icourse;
//
//import android.content.Intent;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//public class CourseDetail extends AppCompatActivity {
//    /**
//     * 详情展示页,用于展示资源的详细信息
//     */
//    public static final String COURSE_NAME = "course_name";
//    public static final String COURSE_TYPE = "course_type";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.course_detail);
//        Intent intent = getIntent();
//        String courseName = intent.getStringExtra(COURSE_NAME);
//        String courseType = intent.getStringExtra(COURSE_TYPE);
//        Toolbar toolbar = findViewById(R.id.course_detail_toolbar);
//        CollapsingToolbarLayout collapsingToolbar= findViewById(R.id.collapsing_toolbar);
//        ImageView courseImage = findViewById(R.id.course_detail_image);
//        TextView courseDetailText = findViewById(R.id.course_detail_text);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar!=null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        collapsingToolbar.setTitle(courseName);
//        courseImage.setImageCourse((int)MainActivity.pictures.get(courseType));
//        String detailText = generateDetailText();
//        courseDetailText.setText(detailText);
//    }
//    String generateDetailText() {
//        StringBuilder builder = new StringBuilder();
//        for (int i=0;i<200;i++) {
//            builder.append("OK");
//        }
//        return builder.toString();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
