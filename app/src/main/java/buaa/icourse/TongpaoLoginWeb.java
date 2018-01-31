package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.Toast;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.HttpClientUtils;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
//
//
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Result;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhaoy on 2017/12/29.
 */

public class TongpaoLoginWeb extends AppCompatActivity {
    public static final String TAG = "ResourceDetail";
    private CheckBox checkBox;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public String tp_login_time;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //checkBox = findViewById(R.id.checkBox);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tongpao_web);

        //WebView
        WebView browser=(WebView)findViewById(R.id.Toweb2);

        String url = "https://tongpao.qinix.com/auths/send_params";
        String token = "";
        try {
            OkHttpClient client = new OkHttpClient();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            final double d = Math.random();
            final int ran = (int)(d*1000000);
            int now_ms = (int)System.currentTimeMillis() % 1000000;
            now_ms = (now_ms + ran) % 1000000;
            now_ms = (now_ms + 1000000) % 1000000;
            tp_login_time = dateFormat.format( now );
            tp_login_time = tp_login_time+"."+now_ms;//".000000";
            System.out.println("NOWNOW::::"+tp_login_time);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JSONObject post_para = new JSONObject();
            post_para.put("redirect", "http://60.205.211.127:8080/androidServer/HelloWorld"); //UploadFragment.uploadUrl); //直接post到服务器//"http://www.baidu.com/");
            post_para.put("need_email", "1");
            post_para.put("need_school_info", "1");
            post_para.put("need_personal", "1");
            post_para.put("status", tp_login_time);
            RequestBody body = RequestBody.create(JSON, post_para.toString());
            System.out.println("@@@@@@@@@@@"+body.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Tongpao-Auth-appid", "c643da987bdc3ec74efbb0ef7927f7ea")
                    .addHeader("Tongpao-Auth-secret", "GNcP_Pa0Z3nFjjsQa8sd8VCUmUEiIZBa6Rue682LDsMyUIx7iwPplQ")
                    .post(body).build();
//                    .post(new FormBody.Builder()
//                            .add("redirect", "http://www.baidu.com/")
//                            .add("need_email", "1")
//                            .add("need_email", "1")
//                            .add("need_personal", "1")
//                            .add("need_school_info", "1")
//                            .add("need_identification", "1")
//                            .build()
            // ).build();
            System.out.println("@@@@@@@@@#@$!#$!@#!@#"+request.toString());
            Response response = client.newCall(request).execute();



            byte[] bytes = response.body().bytes();
            String responseString = new String(bytes);
            System.out.println("@@@@@@"+responseString);
            JSONObject res = new JSONObject(responseString);
            System.out.println("############Token:"+ res + res.getString("token"));
            token = res.getString("token");
        }catch (Exception e){
            e.printStackTrace();;
        }

        //String content = "<p><font color='red'>hello baidu!</font></p>";
        if (token != null) {
            Log.e(TAG, "JJJJJJ");
            WebSettings settings = browser.getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            browser.getSettings().setJavaScriptEnabled(true);//添加对JavaScript支持
            browser.loadUrl("https://tongpao.qinix.com/auths/login?token=" + token);
            //browser.loadData(content, "text/html", "UTF-8"); // 加载定义的代码，并设定编码格式和字符集。
        }
        else
             browser.loadUrl("http://www.baidu.com");



        //设置可自由缩放网页
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setBuiltInZoomControls(true);



        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);//"https://www.baidu.com");
                System.out.println("URLLLLLL:" + url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    //view.loadUrl("about:blank"); // 避免出现默认的错误界面
                    //view.loadUrl(mErrorUrl);
                    Toast.makeText(getApplicationContext(),
                            "同袍登录失败！请检查网络环境是否为校园网！", Toast.LENGTH_SHORT).show();
                    //e.printStackTrace();
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("#######"+url);
                int pos2 = url.indexOf("tongpao.qinix");
                if (pos2 != -1)
                    return ;
               //super.onPageFinished(view, url);
                System.out.println("URRRRRR:" + url);
            int pos = url.indexOf("code=");
                    System.out.println(pos);
                    String code = url.substring(pos+5, url.length());
                    System.out.println(code);
                    System.out.println(pos);

                    boolean zy = true;
                    int cnt_l = 0;
                    while (!tpLoginSuccess(code)) {
                        cnt_l++;
                        if (cnt_l >= 2)
                            break;
                    }
                    if (cnt_l < 2){
                        Intent intent = new Intent(TongpaoLoginWeb.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }
        });
    }

    //go back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView browser=(WebView)findViewById(R.id.Toweb2);
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
            browser.goBack();
            return true;
        }
        //  return true;
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    public boolean tpLoginSuccess(String code){
        System.out.println("!!!!POST_AFTER");
        try {
            //Thread.sleep(3000);

            //OkHttpClient client = new OkHttpClient();
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(3, TimeUnit.SECONDS) //连接超时
                    .readTimeout(4, TimeUnit.SECONDS) //读取超时
                    .writeTimeout(5, TimeUnit.SECONDS) //写超时
                    .build();
            Request request = new Request.Builder()
                    .url(UploadFragment.uploadUrl)
                    .post(new FormBody.Builder()
                            .add("Time", tp_login_time)
                            .add("code", code)
                            .build()
                    ).build();

            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            String responseString = new String(bytes);
            JSONArray jsonArray = new JSONArray(responseString);
            JSONObject jo = jsonArray.getJSONObject(0);
            //new JSONObject(responseString);
            String res = "";
            res = jo.getString("result");
            String username = "";
            if (jo.has("username"))
                username= jo.getString("username");
            System.out.println("RES::::::" + res);

            if (res == null || username == null)
                return false;
            if (res.equals("true")) {
                System.out.println("UUUUUUU::"+username);
                editor = pref.edit();
                //if (checkBox.isChecked())
                {
                    editor.putBoolean("remember_password", false);
                    editor.putString("sid", username);
                    editor.putString("password", "");
                    editor.putBoolean("online", true);
                    editor.putBoolean("isTongpao", true);
                }
                //else {
                //    editor.clear();
                //}
                editor.apply();
                //启动主活动
//                Intent intent = new Intent(TongpaoLoginWeb.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                return true;
            } else {
                String information_fail = "Wrong password";
                if (res.equals("User Not Exist"))
                    information_fail = "用户不存在！";
                else if (res.equals("Wrong password"))
                    information_fail = "密码错误！";
                //Toast.makeText(TongpaoLoginWeb.this, information_fail, Toast.LENGTH_SHORT).show(); //"Wrong password
                return false;
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    "同袍登录失败！请检查网络环境是否为校园网！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }
}

