package top.ckxgzxa.notepad;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        // 给按钮设置点击事件
        findViewById(R.id.pwd_submit).setOnClickListener(v -> {
            // 获取输入的密码
            String pwd = ((EditText) findViewById(R.id.password)).getText().toString();
            // 判断密码是否为空
            if (pwd.isEmpty()) {
                // 如果为空，提示用户
                Toast.makeText(this, "请输入密码!", Toast.LENGTH_SHORT).show();
                return;
            }
            // 如果不为空，判断密码是否正确
            if (pwd.equals(MyApplication.getPassWord())) {
                // 如果正确，跳转到主界面
                MyApplication.setPassed(true);
                Toast.makeText(this, "密码正确!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // 如果错误，提示用户
                Toast.makeText(this, "密码错误!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
