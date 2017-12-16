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

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (!pref.getBoolean("userInfoFilled", false)) {
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
                        .build()
                ).build();
        OkHttpClient client = new OkHttpClient();
        try {
            String responseString = client.newCall(request).execute().body().string();
            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jo = jsonArray.getJSONObject(0);
            String _user_id = jo.getString("user_id");
            String _user_email = jo.getString("user_email");
            String _user_credit = jo.getString("user_credit");
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
