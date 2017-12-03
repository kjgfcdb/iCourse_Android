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

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Button logout = view.findViewById(R.id.logout);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = pref.edit();
        Boolean isFilled = pref.getBoolean("isFilled", false);
        if (!isFilled) {
            initUserInfo();
            editor.putBoolean("isFilled", true);
            editor.apply();
        }
        String userName = pref.getString("username", "Admin");
        String userEmail = pref.getString("useremail", "anonymous@gmail.com");
        String userIntro = pref.getString("userintro", "Write the code, change the world!");
        //TODO:填充TextView


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("online", false);
                editor.putBoolean("isFilled", false);
                editor.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    void initUserInfo() {
        Request request = new Request.Builder()
                .url(UploadFragment.uploadUrl)
                .post(new FormBody.Builder()
                        .add("initUserInfo", "true")
                        .build()
                )
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            String responseString = client.newCall(request).execute().body().string();
            JSONObject object = new JSONObject(responseString);
            String userName = object.getString("username");
            String userEmail = object.getString("useremail");
            String userIntro = object.getString("userintro");
            editor.putString("username", userName);
            editor.putString("useremail", userEmail);
            editor.putString("userintro", userIntro);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }


}
