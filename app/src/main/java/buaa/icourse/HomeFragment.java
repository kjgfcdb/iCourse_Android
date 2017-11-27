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

import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
            byte[] bytes =  response.body().bytes();

            String responseString = new String(bytes);
//            String responseString = response.body().string().toString();
            Log.d(TAG, "initResources: "+bytes.length);
            Log.d(TAG, "initResources: "+responseString);
            JSONArray jsonArray = new JSONArray(responseString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                localResourceItemQueue.add(new ResourceItem(
//                        convertUTF8ToString(object.getString("resourceName").substring(0,
//                                object.getString("resourceName").lastIndexOf(".")
//                                )),
//                        new String(object.getString("resourceName").replace("\\\\","\\").getBytes("utf-8"),"utf-8"),
//                        unicodeToUtf8(object.getString("resourceName")),
                        object.getString("resourceName"),
                        object.getString("resourceType"),
                        object.getString("url"),
                        object.getString("intro"),
                        object.getString("username"),
                        object.getInt("downloadCount")
                ));
            }
            for (int i = 0; localResourceItemQueue.size() > 0 && i < 5; i++) {
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
    public String convertUTF8ToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        try {
            s = s.toUpperCase();
            int total = s.length() / 2;
            //标识字节长度
            int pos = 0;
            byte[] buffer = new byte[total];
            for (int i = 0; i < total; i++) {
                int start = i * 2;
                //将字符串参数解析为第二个参数指定的基数中的有符号整数。
                buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
                pos++;
            }
            //通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
            //新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
            return new String(buffer, 0, pos, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }
    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
            if(ub == Character.UnicodeBlock.BASIC_LATIN){
                //英文及数字等
                sb.append(myBuffer[i]);
            }else if(ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS){
                //全角半角字符
                int j = (int) myBuffer[i] - 65248;
                sb.append((char)j);
            }else{
                //汉字
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u"+hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * unicode 转换成 utf-8
     * @author fanhui
     * 2007-3-15
     * @param theString
     * @return
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
