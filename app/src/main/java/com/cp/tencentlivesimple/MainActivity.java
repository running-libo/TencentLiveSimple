package com.cp.tencentlivesimple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * create by libo
 * create on 2019-10-29
 * description 主页
 */
public class MainActivity extends BasePermissionActivity {
    public static final String liveUrl = "rtmp://65799.livepush.myqcloud.com/HuyaLive/huyalive?txSecret=9cb224c7d598a79cbfc76f8ab61a847d&txTime=5FDCAC29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 开始直播
     * @param view
     */
    public void openLive(View view) {
        startActivity(new Intent(MainActivity.this, PushStreamActivity.class));
    }

    /**
     * 观看直播
     * @param view
     */
    public void openWatch(View view) {
        startActivity(new Intent(MainActivity.this, PullStreamActivity.class));
    }
}
