package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText sid;
    private EditText password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT> 9) {
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
            sid.setText(oldSid);
            password.setText(oldPassword);
            checkBox.setChecked(true);
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
        if (pref.getBoolean("online",false)) {
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
                if (Sid.equals("14051131") && Password.equals("123456")) {
                    editor = pref.edit();
                    if (checkBox.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("sid", Sid);
                        editor.putString("password", Password);
                        editor.putBoolean("online",true);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    //启动主活动
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
