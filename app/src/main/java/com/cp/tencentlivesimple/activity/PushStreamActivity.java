package com.cp.tencentlivesimple.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.cp.tencentlivesimple.util.TimeUtil;
import com.cp.tencentlivesimple.view.ConfirmDialog;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.view.CountDownTextView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import java.util.List;

/**
 * create by libo
 * create on 2020-7-29
 * description 推流页
 */
public class PushStreamActivity extends BasePermissionActivity implements View.OnClickListener {
    public static final String liveUrl = "rtmp://65799.livepush.myqcloud.com/HuyaLive/huyalive?txSecret=9cb224c7d598a79cbfc76f8ab61a847d&txTime=5FDCAC29";
    private TXLivePusher livePusher;
    private TXCloudVideoView videoView;
    private TXLivePushConfig pushConfig;
    private ImageView ivSwitch;
    private TextView tvStartLive, tvWatch, tvDuration;
    private CountDownTextView tvCountDown;
    private View viewStartLive, viewDoingLive;
    private ImageView ivClose;
    private int curDuration;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            curDuration++;
            tvDuration.setText(TimeUtil.duration2Time(curDuration));
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_stream);

        initView();

        setPushConfig();

        onOrientationChange(true);

        requestPermissions();
    }

    private void initView() {
        videoView = findViewById(R.id.videoview_push);
        ivSwitch = findViewById(R.id.iv_switch);
        tvStartLive = findViewById(R.id.tv_startLive);
        tvWatch = findViewById(R.id.tv_watch);
        tvCountDown = findViewById(R.id.tv_countdown);
        tvDuration = findViewById(R.id.tvDuration);
        viewStartLive = findViewById(R.id.view_startlive);
        viewDoingLive = findViewById(R.id.view_doinglive);
        viewDoingLive.setVisibility(View.GONE);
        ivClose = findViewById(R.id.ivClose);

        ivSwitch.setOnClickListener(this);
        tvStartLive.setOnClickListener(this);
        tvWatch.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        tvCountDown.setListener(new CountDownTextView.OnCountDownFinishListener() {
            @Override
            public void onStartCount() {
                //直播倒计时开始
                viewStartLive.setVisibility(View.GONE);
                viewDoingLive.setVisibility(View.VISIBLE);
                curDuration = 0;
            }

            @Override
            public void onFinish() {
                //直播开始
                startTimerTask();
//                livePusher.startPusher(liveUrl);  暂不推流
            }
        });
    }

    /**
     * App权限申请，这里需要开启相机权限
     */
    private void requestPermissions() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                startPreview();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        startPreview();
    }

    /**
     * 设置推流配置
     */
    private void setPushConfig() {
        livePusher = new TXLivePusher(this);
        pushConfig = new TXLivePushConfig();
        pushConfig.setTouchFocus(true);  //开启手动聚焦
        pushConfig.enableNearestIP(true);
        livePusher.setConfig(pushConfig);
    }

    /**
     * 开启摄像头预览并开启推流
     */
    private void startPreview() {
        livePusher.startCameraPreview(videoView);
    }

    /**
     * 结束预览并关闭推流
     */
    private void stopPreview() {
        livePusher.stopPusher();
        livePusher.stopCameraPreview(true);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_switch:
                livePusher.switchCamera();
                break;
            case R.id.tv_startLive:
                tvCountDown.startLive();
                break;
            case R.id.tv_watch:
                startActivity(new Intent(this, PullStreamActivity.class));
                break;
            case R.id.ivClose:
                closeLive();
                break;
        }
    }

    /**
     * 设置摄像头采集方向
     * @param isPortrait
     *
     * TXLivePushConfig 中的setHomeOrientation改变观众端看到的视频画面的宽高比方向
     *
     * TXLivePusher 中的setRenderRotation接口改变主播端的视频画面的渲染方向
     */
    public void onOrientationChange(boolean isPortrait) {
        if (isPortrait) {
            pushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_DOWN);
            livePusher.setConfig(pushConfig);
            livePusher.setRenderRotation(0);
        } else {
            pushConfig.setHomeOrientation(TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT);
            livePusher.setConfig(pushConfig);
            // 因为采集旋转了，为了保证本地渲染是正的，则设置渲染角度为90度。 
            livePusher.setRenderRotation(90);
        }
    }

    private void closeLive() {
        new ConfirmDialog(this, R.style.basedialog_style)
                .setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        finishLive();
                    }
                });
    }

    /**
     * 结束直播
     */
    private void finishLive() {
        startActivity(new Intent(PushStreamActivity.this, LiveFinishActivity.class));
        stopPreview();
        //恢复未开播前的菜单页面
        viewStartLive.setVisibility(View.VISIBLE);
        viewDoingLive.setVisibility(View.GONE);
        tvDuration.setVisibility(View.GONE);
    }

    private void startTimerTask() {
        tvDuration.setVisibility(View.VISIBLE);
        handler.sendEmptyMessage(0);
    }
}
