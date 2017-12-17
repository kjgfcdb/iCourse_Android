package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    /**
     * 种类页面,支持按照资源种类(院系)进行资源检索
     */
    private List<CollegeItem> CollegeList = new ArrayList<>();
    private CollegeAdapter adapter;
    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initResources();
        RecyclerView recyclerView = view.findViewById(R.id.category_recycler_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        //设置布局为两列
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CollegeAdapter(CollegeList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initResources() {
        CollegeList.add(new CollegeItem("材料科学与工程学院",1));
        CollegeList.add(new CollegeItem("电子信息工程学院",2));
        CollegeList.add(new CollegeItem("自动化科学与电气工程学院",3));
        CollegeList.add(new CollegeItem("能源与动力工程学院",4));
        CollegeList.add(new CollegeItem("航空科学与工程学院",5));
        CollegeList.add(new CollegeItem("计算机学院",6));
        CollegeList.add(new CollegeItem("机械工程及自动化学院",7));
        CollegeList.add(new CollegeItem("经济管理学院",8));
        CollegeList.add(new CollegeItem("数学与系统科学学院",9));
        CollegeList.add(new CollegeItem("生物与医学工程学院",10));
        CollegeList.add(new CollegeItem("人文社会科学学院（公共管理学院）",11));
        CollegeList.add(new CollegeItem("外国语学院",12));
        CollegeList.add(new CollegeItem("交通科学与工程学院",13));
        CollegeList.add(new CollegeItem("可靠性与系统工程学院",14));
        CollegeList.add(new CollegeItem("宇航学院",15));
        CollegeList.add(new CollegeItem("飞行学院",16));
        CollegeList.add(new CollegeItem("仪器科学与光电工程学院",17));
        CollegeList.add(new CollegeItem("物理科学与核能工程学院",19));
        CollegeList.add(new CollegeItem("法学院",20));
        CollegeList.add(new CollegeItem("软件学院",21));
        CollegeList.add(new CollegeItem("高等工程学院",23));
        CollegeList.add(new CollegeItem("中法工程师学院",24));
        CollegeList.add(new CollegeItem("国际学院",25));
        CollegeList.add(new CollegeItem("新媒体艺术与设计学院",26));
        CollegeList.add(new CollegeItem("化学与环境学院",27));
        CollegeList.add(new CollegeItem("人文与社会科学高等研究院",29));
    }


    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }


}
