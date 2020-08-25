package com.cp.tencentlivesimple;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import java.util.List;

/**
 * create by libo
 * create on 2019-10-29
 * description 推流页
 */
public class PushStreamActivity extends BasePermissionActivity implements View.OnClickListener {
    private TXLivePusher livePusher;
    private TXCloudVideoView videoView;
    private Button btnClose, btnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_stream);

        initView();

        requestPermissions();
    }

    private void initView() {
        videoView = findViewById(R.id.videoview_push);
        btnClose = findViewById(R.id.btn_close);
        btnSwitch = findViewById(R.id.btn_switch);
        btnClose.setOnClickListener(this);
        btnSwitch.setOnClickListener(this);
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
                startPreview();
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
        pushConfig.setTouchFocus(true);  //开启手动聚焦
        pushConfig.enableNearestIP(true);
        livePusher.setConfig(pushConfig);
    }

    /**
     * 开启摄像头预览并开启推流
     */
    private void startPreview() {
        livePusher.startCameraPreview(videoView);
        int ret = livePusher.startPusher(MainActivity.liveUrl);
        if (ret == -5) {
            Log.i("minfo", "startRTMPPush: license 校验失败");
        }
    }

    /**
     * 结束预览并关闭推流
     */
    private void stopPreview() {
        livePusher.stopPusher();
        livePusher.stopCameraPreview(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                closeLive();
                break;
            case R.id.btn_switch:
                livePusher.switchCamera();
                break;
        }
    }

    private void closeLive() {
        new ConfirmDialog(this, R.style.basedialog_style)
                .setTitle("是否退出直播")
                .setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        finish();
                    }
                });
    }
}
