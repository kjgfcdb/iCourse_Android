package buaa.icourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText account;
    private EditText email;
    private EditText password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = findViewById(R.id.account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //从"remember_password"中取值，默认值为false(第二个参数)，如果不存在该键那么就取默认值
        boolean isRemember = pref.getBoolean("remember_password",false);
        if (isRemember) {
            String oldAccount = pref.getString("account","");
            String oldEmail = pref.getString("email","");
            String oldPassword = pref.getString("password","");
            account.setText(oldAccount);
            email.setText(oldEmail);
            password.setText(oldPassword);
            checkBox.setChecked(true);
        }
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Account = account.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (Account.equals("admin") && Password.equals("123456")) {
                    editor = pref.edit();
                    if (checkBox.isChecked()) {
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",Account);
                        editor.putString("email",Email);
                        editor.putString("password",Password);
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    //启动主活动
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,"Wrong password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
