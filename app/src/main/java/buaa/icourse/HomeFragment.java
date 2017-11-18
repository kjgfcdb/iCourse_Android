package buaa.icourse;

import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HomeFragment extends Fragment {
    /**
     * 主页面，用于显示排序的资源
     */
    private static final int SUCCESS = 2;//状态识别码
    private static final int FAILD = 3;
    private List<ResourceItem> resourceItemList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;
    private ResourceAdapter adapter;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
//                    Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();
                    break;
                case FAILD:
//                    Toast.makeText(getContext(), "更新失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

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
        Message msg = Message.obtain();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UploadFragment.uploadUrl)
                    .post(new FormBody.Builder()
                            .add("homePage", "init")
                            .build()
                    ).build();
            String responseString = client.newCall(request).execute().body().string();
            JSONArray jsonArray = new JSONArray(responseString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                resourceItemList.add(new ResourceItem(object.getString("resourceName"),
                        object.getString("resourceType")
                ));
            }
            msg.what = SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            msg.what = FAILD;
        }
        mHandler.sendMessage(msg);
//        for (int i = 0; i < 20; i++) {
//            Random random = new Random();
//            int index = random.nextInt(items.length);
//            resourceItemList.add(items[index]);
//        }
    }

    public void refreshResources() {
        //下滑刷新页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
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
