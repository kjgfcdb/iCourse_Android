package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class LoginActivity extends AppCompatActivity {
    private EditText sid;
    private EditText password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        //WebView
//        WebView browser=(WebView)findViewById(R.id.Toweb);
//        browser.loadUrl("http://www.baidu.com");
//
//        //设置可自由缩放网页
//        browser.getSettings().setSupportZoom(true);
//        browser.getSettings().setBuiltInZoomControls(true);
//
//        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
//        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
//        browser.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            {
//                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//                view.loadUrl(url);
//                return true;
//            }
//        });


        TextView tongpao_login = findViewById(R.id.tongpao_login);
        tongpao_login.setClickable(true);
        tongpao_login.setFocusable(true);
        tongpao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(LoginActivity.this, TongpaoLoginWeb.class);
                startActivity(intent3);
            }
        });

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //学号
        sid = findViewById(R.id.sid);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //从"remember_password"中取值，默认值为false(第二个参数)，如果不存在该键那么就取默认值
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String oldSid = pref.getString("sid", "");
            String oldPassword = pref.getString("password", "");
            if (oldPassword != null) {
                sid.setText(oldSid);
                password.setText(oldPassword);
                checkBox.setChecked(true);
            }
        }
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        //开启注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //如果用户成功登录,那么下次点开则不必再出现登录界面了
        if (pref.getBoolean("online", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //开启登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Sid = sid.getText().toString();
                String Password = password.getText().toString();

                if (Sid==null||Sid.equals("")){
                    Toast.makeText(LoginActivity.this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else
                if (Password==null||Password.equals("")){
                    Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    Request request = new Request.Builder()
                            .url(UploadFragment.uploadUrl)
                            .post(new FormBody.Builder()
                                    .add("login", "1")
                                    .add("studentNo", Sid)
                                    .add("password_fill", Password)
                                    .build()
                            ).build();
                    OkHttpClient client = new OkHttpClient();

                    try {
                        String responseString = client.newCall(request).execute().body().string();
                        System.out.println("@@" + responseString);
                        JSONArray jsonArray = new JSONArray(responseString);
                        JSONObject jo = jsonArray.getJSONObject(0);
                        //new JSONObject(responseString);
                        res = jo.getString("result");
                        System.out.println("RES::::::" + res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (res.equals("true")) {
                        editor = pref.edit();
                        if (checkBox.isChecked()) {
                            editor.putBoolean("remember_password", true);
                            editor.putString("sid", Sid);
                            editor.putString("password", Password);
                            editor.putBoolean("online", true);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        //启动主活动
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String information_fail = "Wrong password";
                        if (res.equals("User Not Exist"))
                            information_fail = "用户不存在！";
                        else if (res.equals("Wrong password"))
                            information_fail = "密码错误！";
                        Toast.makeText(LoginActivity.this, information_fail, Toast.LENGTH_SHORT).show(); //"Wrong password
                    }
                }
            }
        });
    }



//    //go back
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        WebView browser=(WebView)findViewById(R.id.Toweb);
//        // Check if the key event was the Back button and if there's history
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
//            browser.goBack();
//            return true;
//        }
//        //  return true;
//        // If it wasn't the Back key or there's no web page history, bubble up to the default
//        // system behavior (probably exit the activity)
//        return super.onKeyDown(keyCode, event);
//    }
}
