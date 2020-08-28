package com.cp.tencentlivesimple.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidkun.xtablayout.XTabLayout;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.livingroom.IMLVBLiveRoomListener;
//import com.cp.tencentlivesimple.livingroom.MLVBLiveRoom;
import com.cp.tencentlivesimple.roomutil.commondef.AnchorInfo;
import com.cp.tencentlivesimple.roomutil.commondef.AudienceInfo;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Map;

import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_GET_MESSAGE;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;

/**
 * create by libo
 * create on 2019-10-29
 * description 拉流页
 */
public class PullStreamActivity extends BasePermissionActivity implements IMLVBLiveRoomListener  {
    private TXLivePlayer livePlayer;
    private TXCloudVideoView videoView;
    private String roomId;
    private String url;
    private XTabLayout tabLayout;
    private String[] titles = new String[]{"聊天", "主播", "排行", "贵宾"};
    private ImageView ivBack;
    private TextView tvSend;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_stream);

        roomId = getIntent().getStringExtra("roomId");
        url = getIntent().getStringExtra("url");
        init();
    }

    private void init() {
        livePlayer = new TXLivePlayer(this);
        videoView = findViewById(R.id.videoview_pull);
        ivBack = findViewById(R.id.ivBack);
        tvSend = findViewById(R.id.tvSend);
        etContent = findViewById(R.id.etContent);

        livePlayer.setPlayerView(videoView);
        enterRoom(roomId);

        tabLayout = findViewById(R.id.tablayout);
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }

        ivBack.setOnClickListener(v -> finish());

        tvSend.setOnClickListener(v -> {
            sendMessage(etContent.getText().toString());
        });

        observePlayerEvent();
    }

    private void enterRoom(String roomId) {
//        MLVBLiveRoom.sharedInstance(getApplicationContext()).enterRoom(roomId, videoView, new IMLVBLiveRoomListener.EnterRoomCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Toast.makeText(getApplicationContext(), "进入房间失败 errCode=" + errCode + "  " + errInfo, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "进入房间成功", Toast.LENGTH_SHORT).show();
//                startLivePlay(url);
//            }
//        });
    }

    /**
     * 开启直播播放
     */
    private void startLivePlay(String playUrl) {
        //第二个参数为播放地址类型
        livePlayer.startPlay(playUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV);
        // 设置填充模式
        livePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
        // 设置画面渲染方向
        livePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        livePlayer.stopPlay(true);  //true代表清除最后一帧画面
        videoView.onDestroy();

        exitRoom();
    }

    /**
     * 设置视频播放状态监听
     */
    private void observePlayerEvent() {
//        livePlayer.setPlayListener(new ITXLivePlayListener() {
//            @Override
//            public void onPlayEvent(int event, Bundle bundle) {
//                switch (event) {
//                    case PLAY_ERR_NET_DISCONNECT:
//                    case PLAY_EVT_PLAY_END:
//                        Toast.makeText(PullStreamActivity.this, "视频播放已结束", Toast.LENGTH_SHORT).show();
//                        break;
//                    case PLAY_EVT_GET_MESSAGE:
//                        Toast.makeText(getApplicationContext(), "获取到消息", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNetStatus(Bundle bundle) {
//
//            }
//        });

//        MLVBLiveRoom.sharedInstance(getApplicationContext()).getCustomInfo(new GetCustomInfoCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//
//            }
//
//            @Override
//            public void onGetCustomInfo(Map<String, Object> customInfo) {
//                Toast.makeText(getApplicationContext(), "收到消息 " + customInfo.get(""), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 退出房间
     */
    private void exitRoom() {
//        MLVBLiveRoom.sharedInstance(getApplicationContext()).exitRoom(new IMLVBLiveRoomListener.ExitRoomCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Toast.makeText(getApplicationContext(), "退出房间失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "退出房间成功", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void sendMessage(String message) {
//        if (TextUtils.isEmpty(message)) {
//            return;
//        }

//        MLVBLiveRoom.sharedInstance(getApplicationContext()).sendRoomTextMsg(message, new IMLVBLiveRoomListener.SendRoomTextMsgCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Toast.makeText(getApplicationContext(), "发送消息失败", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "发送消息成功", Toast.LENGTH_SHORT).show();
//            }
//        });
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
        Toast.makeText(getApplicationContext(), "房间已被销毁", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {
        Toast.makeText(getApplicationContext(), "主播加入", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {
        Toast.makeText(getApplicationContext(), "主播退出", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), "收到消息 " + userName + ":" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {

    }
}
