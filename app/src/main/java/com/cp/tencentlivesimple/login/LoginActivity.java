package com.cp.tencentlivesimple.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.activity.RoomListActivity;
import com.cp.tencentlivesimple.login.model.ProfileManager;
import com.cp.tencentlivesimple.login.model.UserModel;

public class LoginActivity extends AppCompatActivity {
    private EditText etLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnAnchor = findViewById(R.id.btnAnchor);
        Button btnAudience = findViewById(R.id.btnAudience);
        etLogin = findViewById(R.id.etUserId);
    }

    public void login(View view) {
        String userId = etLogin.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "请输入userId", Toast.LENGTH_SHORT).show();
        } else {
            UserModel.userId = userId;
            ProfileManager.getInstance().login(UserModel.userId, "我的直播间", new ProfileManager.ActionCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, RoomListActivity.class));
                }

                @Override
                public void onFailed(int code, String msg) {
                    Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}