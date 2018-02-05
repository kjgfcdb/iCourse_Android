package buaa.icourse;

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
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    /**
     * 主页面，用于显示排序的资源
     */
    private static final int SUCCESS = 2;//状态识别码
    private static final int FAILED = 3;
    private static final String TAG = "HomeFragment";
    private List<ResourceItem> resourceItemList = new ArrayList<>();
    private Queue<ResourceItem> localResourceItemQueue = new ArrayDeque<>();
    private SwipeRefreshLayout swipeRefresh;
    private ResourceAdapter adapter;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
//                    Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();
                    break;
                case FAILED:
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
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        initResources();

        RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
        final ProgressBar bar = view.findViewById(R.id.fragment_home_progress_bar);
        //网格布局,设置为1列
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        //设置资源适配器
        adapter = new ResourceAdapter(resourceItemList);
        recyclerView.setAdapter(adapter);
        //设置滑动刷新器
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        //下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshResources();
            }
        });
        //上滑加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        layoutManager.findLastVisibleItemPosition() + 1 == adapter.getItemCount()) {
                    bar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 5 && localResourceItemQueue.size() > 0; i++) {
                                resourceItemList.add(localResourceItemQueue.poll());
                            }
                            adapter.notifyDataSetChanged();
                            bar.setVisibility(View.INVISIBLE);
                        }
                    }, 1000);
                }
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
        localResourceItemQueue.clear();
        Message msg = Message.obtain();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(UploadFragment.uploadUrl)
                    .post(new FormBody.Builder()
                            .add("homePage", "init")
                            .build()
                    ).build();
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();

            String responseString = new String(bytes);
//            String responseString = response.body().string().toString();
            Log.d(TAG, "initResources: " + bytes.length);
            Log.d(TAG, "initResources: " + responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String h = ""+object.getDouble("evaluation");
                Log.d("EVA == ",Double.toString(object.getDouble("evaluation")));
                String intro = object.getString("intro");
                if (intro.equals("")) {
                    intro = "该资源尚无简介";
                }
                localResourceItemQueue.add(new ResourceItem(
                        object.getString("resourceName"),//unicodeToString(object.getString("resourceName")),
                        object.getString("resourceType"),
                        object.getString("url"),
                        intro,//object.getString("intro"),//unicodeToString(object.getString("intro")),
                        object.getString("username"),//unicodeToString(object.getString("username")),
                        object.getInt("downloadCount"),
                        object.getDouble("evaluation")
                ));
            }
            for (int i = 0; localResourceItemQueue.size() > 0 && i < 10; i++) {
                resourceItemList.add(localResourceItemQueue.poll());
            }
            msg.what = SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            msg.what = FAILED;
        }
        mHandler.sendMessage(msg);
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

    private static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }
}
