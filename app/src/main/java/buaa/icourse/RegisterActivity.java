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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
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
        final String TAG = "RegisterActivity";

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //Toast.makeText(getApplicationContext(), studentGender.getText(), Toast.LENGTH_SHORT).show();
                OkHttpClient client = new OkHttpClient();
                try {
                    Response response = client.newCall(request).execute();

                    byte[] bytes = response.body().bytes();
                    String responseString = new String(bytes);
                    JSONObject jsonobject = new JSONObject(responseString);

                    if (response.code() == 200 ) {
                        Log.d(TAG, "Status:::"+jsonobject.get("status"));
                        if (jsonobject.get("status") == "success") {
                            Toast.makeText(getApplicationContext(),
                                    "注册成功", Toast.LENGTH_SHORT).show();
                        }else if (jsonobject.get("status") == "same") {
                            Toast.makeText(getApplicationContext(),
                                    "该用户名已被注册！请重新注册", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "注册失败", Toast.LENGTH_SHORT).show();
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
