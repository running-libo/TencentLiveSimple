package com.cp.tencentlivesimple;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import java.util.List;

/**
 * create by libo
 * create on 2019-10-29
 * description 主页
 */
public class MainActivity extends BasePermissionActivity {
    private TXLivePusher livePusher;
    private TXCloudVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoview_push);

        requestPermissions();
    }

    /**
     * App权限申请，这里需要开启相机权限
     */
    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                setPushConfig();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
    }

    /**
     * 设置推流配置
     */
    private void setPushConfig() {
        livePusher = new TXLivePusher(this);
        TXLivePushConfig pushConfig = new TXLivePushConfig();
        pushConfig.enableNearestIP(true);
        livePusher.setConfig(pushConfig);
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
