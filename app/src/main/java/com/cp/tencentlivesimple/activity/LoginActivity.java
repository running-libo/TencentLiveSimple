package com.cp.tencentlivesimple.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.cp.tencentlivesimple.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnAnchor = findViewById(R.id.btnAnchor);
        Button btnAudience = findViewById(R.id.btnAudience);
        btnAnchor.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PushStreamActivity.class)));

        btnAudience.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PullStreamActivity.class)));
    }
}