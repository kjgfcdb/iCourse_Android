package buaa.icourse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    /**
     * 主页面，用于显示排序的资源
     */

    private ResourceItem[] items = {
            new ResourceItem("Math", 1),
            new ResourceItem("English", 2),
            new ResourceItem("Chinese", 2),
            new ResourceItem("Computer", 2),
            new ResourceItem("Science", 2),
            new ResourceItem("Database", 2),
            new ResourceItem("Compiler", 2),
            new ResourceItem("Matlab", 2),
            new ResourceItem("MachineLearning", 2),
    };
    private List<ResourceItem> resourceItemList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ResourceAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initResources();
        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        //网格布局,设置为1列
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new ResourceAdapter(resourceItemList);
        recyclerView.setAdapter(adapter);
        //设置滑动刷新器
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshResources();
            }
        });
        return view;
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public void initResources() {
        //初始化资源
        resourceItemList.clear();
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            int index = random.nextInt(items.length);
            resourceItemList.add(items[index]);
        }
    }

    public void refreshResources() {
        //下滑刷新页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initResources();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
