package com.cp.tencentlivesimple.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cp.tencentlivesimple.livingroom.GenerateTestUserSig;
import com.cp.tencentlivesimple.livingroom.IMLVBLiveRoomListener;
import com.cp.tencentlivesimple.livingroom.MLVBLiveRoom;
import com.cp.tencentlivesimple.roomutil.commondef.AnchorInfo;
import com.cp.tencentlivesimple.roomutil.commondef.AudienceInfo;
import com.cp.tencentlivesimple.roomutil.commondef.LoginInfo;
import com.cp.tencentlivesimple.util.TimeUtil;
import com.cp.tencentlivesimple.view.ExitConfirmDialog;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.view.CountDownTextView;
import com.cp.tencentlivesimple.view.SwitchQualityDialog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_GET_MESSAGE;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;

/**
 * create by libo
 * create on 2020-7-29
 * description 推流页
 */
public class PushStreamActivity extends BasePermissionActivity implements View.OnClickListener, IMLVBLiveRoomListener   {
    public static final String liveUrl = "rtmp://65799.livepush.myqcloud.com/HuyaLive/huyalive?txSecret=9cb224c7d598a79cbfc76f8ab61a847d&txTime=5FDCAC29";
    private TXLivePusher livePusher;
    private TXCloudVideoView videoView;
    private TXLivePushConfig pushConfig;
    private ImageView ivSwitch, ivSwitch2, ivPixel;
    private TextView tvStartLive, tvWatch, tvDuration;
    private CountDownTextView tvCountDown;
    private View viewStartLive, viewDoingLive;
    private ImageView ivClose;
    private int curDuration;
    private ScaleGestureDetector scaleGestureDetector;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            curDuration++;
            tvDuration.setText(TimeUtil.duration2Time(curDuration));
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };
    private Handler getAudienceHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            getAudienceLists();
            getAudienceHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };
    private String userId = "123456789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_stream);

        initView();

        setPushConfig();

        requestPermissions();
    }

    private void initView() {
        videoView = findViewById(R.id.videoview_push);
        ivSwitch = findViewById(R.id.iv_switch);
        ivSwitch2 = findViewById(R.id.ivSwitch);
        tvStartLive = findViewById(R.id.tv_startLive);
        tvWatch = findViewById(R.id.tv_watch);
        tvCountDown = findViewById(R.id.tv_countdown);
        tvDuration = findViewById(R.id.tvDuration);
        viewStartLive = findViewById(R.id.view_startlive);
        viewDoingLive = findViewById(R.id.view_doinglive);
        viewDoingLive.setVisibility(View.GONE);
        ivClose = findViewById(R.id.ivClose);
        ivPixel = findViewById(R.id.ivPixel);

        ivSwitch.setOnClickListener(this);
        ivSwitch2.setOnClickListener(this);
        tvStartLive.setOnClickListener(this);
        tvWatch.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivPixel.setOnClickListener(this);

        setScaleEvent();

        loginRoom();

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
                livePusher.startPusher(liveUrl);
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
        livePusher.setRenderRotation(270);  //直播端旋转角度为270为正常角度
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
        getAudienceHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopPreview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSwitch:
            case R.id.iv_switch:
                livePusher.switchCamera();
                break;
            case R.id.tv_startLive:
                tvCountDown.startLive();
                break;
            case R.id.tv_watch:
//                startActivity(new Intent(this, PullStreamActivity.class));
//                getRoomList();
                break;
            case R.id.ivClose:
                closeLive();
                break;
            case R.id.ivPixel:
                new SwitchQualityDialog(this, R.style.basedialog_style, pos -> {
                    switchQuality(pos);
                });
                break;
        }
    }

    /**
     * 切换画质
     */
    private void switchQuality(int pos) {
        switch (pos) {
            case 0:
                livePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, true, false);
                Toast.makeText(getApplicationContext(), "已切换到流畅画质", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                livePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, true, false);
                Toast.makeText(getApplicationContext(), "已切换到高清画质", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                livePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION, true, false);
                Toast.makeText(getApplicationContext(), "已切换到超清画质", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void closeLive() {
        new ExitConfirmDialog(this, R.style.basedialog_style)
                .setOnClickListener(new ExitConfirmDialog.OnClickListener() {
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

    private void setScaleEvent() {
        viewDoingLive.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });
        scaleGestureDetector = new ScaleGestureDetector(this, new MyScaleListener());
    }

    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {

    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {

    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onRoomDestroy(String roomID) {

    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {
        Toast.makeText(getApplicationContext(), "观众加入", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {
        Toast.makeText(getApplicationContext(), "观众退出", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {

    }

    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
        Toast.makeText(getApplicationContext(), "收到消息" + userName + ":" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {

    }

    class MyScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("minfo", "缩放比例： " + detector.getScaleFactor());
            Log.i("minfo", "最大焦距 " + livePusher.getMaxZoom());

            livePusher.setZoom((int) (detector.getScaleFactor()*5));  //设置缩放
            return super.onScale(detector);
        }
    }

    /**
     * 登录账号
     */
    private void loginRoom() {
        LoginInfo loginInfo = new LoginInfo(GenerateTestUserSig.SDKAPPID, userId,
                "libo", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1598533687014&di=62d1d19b70073344e6f5010e5dbfb5c6&imgtype=0&src=http%3A%2F%2Fi1.17173cdn.com%2F2fhnvk%2FYWxqaGBf%2Fcms3%2FKhIMufbldqvFqra.png", GenerateTestUserSig.genTestUserSig(userId));
        MLVBLiveRoom.sharedInstance(getApplicationContext()).login(loginInfo, new IMLVBLiveRoomListener.LoginCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "登录失败,errCode= " + errCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                createRoom();
            }
        });

    }

    /**
     * 获取房间观众
     */
    private void getAudienceLists() {
        MLVBLiveRoom.sharedInstance(getApplicationContext()).getAudienceList(new IMLVBLiveRoomListener.GetAudienceListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "获取观众失败,errCode=" + errCode + " errInfo " + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<AudienceInfo> audienceInfoList) {
                Toast.makeText(getApplicationContext(), "获取到" + audienceInfoList.size() + "个观众", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 创建房间
     */
    private void createRoom() {
        MLVBLiveRoom.sharedInstance(getApplicationContext()).startLocalPreview(true, videoView);
        MLVBLiveRoom.sharedInstance(getApplicationContext()).createRoom(userId, "房间信息", new IMLVBLiveRoomListener.CreateRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "创建房间失败,errCode=" + errCode + "  " + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String roomId) {
                Toast.makeText(getApplicationContext(), "创建房间成功, RoomID=" + roomId, Toast.LENGTH_SHORT).show();
                intervalGetAudienceList();
            }
        });
    }

    /**
     * 进入房间
     * @param roomId
     */
    private void enterRoom(String roomId) {
        MLVBLiveRoom.sharedInstance(getApplicationContext()).enterRoom(roomId, videoView, new IMLVBLiveRoomListener.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "进入房间失败 errCode=" + errCode + "  " + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "进入房间成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intervalGetAudienceList() {
        getAudienceHandler.sendEmptyMessage(0);
    }

}
