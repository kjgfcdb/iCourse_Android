package buaa.icourse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "Register!!!";
    //用户注册模块
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 学号 邮箱 密码 性别
        final EditText studentNo = findViewById(R.id.student_no);
        final EditText studentMail = findViewById(R.id.student_mail);
        final EditText studentPasswd = findViewById(R.id.student_passwd);
        final RadioGroup genderGroup = findViewById(R.id.student_gender);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pattern p =  Pattern.compile("^[a-zA-Z0-9_-]+@buaa.edu.cn$");//复杂匹配
                Matcher m = p.matcher(studentMail.toString());
                //return m.matches();




                RadioButton studentGender = findViewById(genderGroup.getCheckedRadioButtonId());
                Request request = new Request.Builder()
                        .url(UploadFragment.uploadUrl)
                        .post(new FormBody.Builder()
                                .add("registration", "1")
                                .add("studentNo", studentNo.getText().toString())
                                .add("studentMail", studentMail.getText().toString())
                                .add("studentPasswd", studentPasswd.getText().toString())
                                .add("studentGender", studentGender.getText().toString())
                                .build()
                        ).build();
                Log.e(TAG, studentGender.getText().toString());

                //Toast.makeText(getApplicationContext(), studentGender.getText(), Toast.LENGTH_SHORT).show();
                OkHttpClient client = new OkHttpClient();

                try {
                    Response response = client.newCall(request).execute();

                    byte[] bytes = response.body().bytes();
                    String responseString = new String(bytes);
                    JSONObject jo = new JSONObject(responseString);
                    String status = jo.getString("status");
                    Log.e(TAG, "jjkls"+status);
                    if (response.code() == 200 && status == "success") {
                        Toast.makeText(getApplicationContext(),
                                "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (response.code() != 200)
                            Toast.makeText(getApplicationContext(),
                                    "注册失败", Toast.LENGTH_SHORT).show();
                        else if (status.equals("same"))
                            Toast.makeText(getApplicationContext(),
                                    "该用户已存在！请重新注册", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
