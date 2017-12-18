package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class UserFragment extends Fragment {
    /**
     * 用户界面，用于显示用户信息
     */

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView user_id;
    private TextView user_email;
    private TextView user_credit;

    HashMap mp_college = new HashMap(){
        {
            put(  1, "材料科学与工程学院");
            put(  2, "电子信息工程学院");
            put(  3, "自动化科学与电气工程学院");
            put(  4, "能源与动力工程学院");
            put(  5, "航空科学与工程学院");
            put(  6, "计算机学院");
            put(  7, "机械工程及自动化学院");
            put(  8, "经济管理学院");
            put(  9, "数学与系统科学学院");
            put(  10, "生物与医学工程学院");
            put(  11, "人文社会科学学院");
            put(  12, "外国语学院");
            put(  13, "交通科学与工程学院");
            put(  14, "可靠性与系统工程学院");
            put(  15, "宇航学院");
            put(  16, "飞行学院");
            put(  17, "仪器科学与光电工程学院");
            put(  18, "北京学院");
            put(  19, "物理科学与核能工程学院");
            put(  20, "法学院");
            put(  21, "软件学院");
            put(  22, "现代远程教育学院");
            put(  23, "高等工程学院");
            put(  24, "中法工程师学院");
            put(  25, "国际学院");
            put(  26, "新媒体艺术与设计学院");
            put(  27, "化学与环境学院");
            put(  28, "思想政治理论学院");
            put(  29, "人文与社会科学高等研究");
        }
    };

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Button logout = view.findViewById(R.id.logout);
        user_id = view.findViewById(R.id.user_id);
        user_email = view.findViewById(R.id.user_email);
        user_credit = view.findViewById(R.id.user_credit);

        System.out.println("#@@@@@@@@@");
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!pref.getBoolean("userInfoFilled", false)) {
            System.out.println("FIIUUUU#@@@@@@@@@");
            fillUserInfo();
        }
        user_id.setText(pref.getString("user_id", "Anonymous"));
        user_email.setText(pref.getString("user_email", "anonymous@example.com"));
        user_credit.setText(pref.getString("user_credit", "10"));
        editor = pref.edit();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("online", false);
                editor.putBoolean("userInfoFilled", false);
                editor.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    public void fillUserInfo() {
        Request request = new Request.Builder()
                .url(UploadFragment.uploadUrl)
                .post(new FormBody.Builder()
                        .add("getUserInfo", "1")
                        .add("username",pref.getString("sid", "$$$"))
                        .build()
                ).build();
        OkHttpClient client = new OkHttpClient();
        try {
            String responseString = client.newCall(request).execute().body().string();
            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jo = jsonArray.getJSONObject(0);
            String _user_id = pref.getString("sid", "$$$");//"";//jo.getString("nickname");//"";//jo.getString("user_id");
            System.out.println("@@@"+_user_id);
            String _user_email = jo.getString("user_email");
            String _user_credit = "";
            if (jo.has("user_intro"))
                _user_credit = jo.getString("user_intro");//jo.getString("user_credit");
            editor = pref.edit();
            editor.putBoolean("userInfoFilled", true);
            editor.putString("user_id", _user_id);
            editor.putString("user_email", _user_email);
            editor.putString("user_credit", _user_credit);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }


}
