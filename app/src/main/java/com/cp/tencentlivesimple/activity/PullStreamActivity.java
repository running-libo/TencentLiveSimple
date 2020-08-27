package com.cp.tencentlivesimple.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.adapter.RoomAdapter;
import com.cp.tencentlivesimple.livingroom.GenerateTestUserSig;
import com.cp.tencentlivesimple.livingroom.IMLVBLiveRoomListener;
import com.cp.tencentlivesimple.livingroom.MLVBLiveRoom;
import com.cp.tencentlivesimple.roomutil.commondef.LoginInfo;
import com.cp.tencentlivesimple.roomutil.commondef.RoomInfo;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * create by libo
 * create on 2019-10-29
 * description 拉流页
 */
public class PullStreamActivity extends BasePermissionActivity {
    private TXLivePlayer livePlayer;
    private TXCloudVideoView videoView;
    private String roomId;
    private String url;

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
        livePlayer.setPlayerView(videoView);
        enterRoom(roomId);
    }

    private void enterRoom(String roomId) {
        MLVBLiveRoom.sharedInstance(getApplicationContext()).enterRoom(roomId, videoView, new IMLVBLiveRoomListener.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "进入房间失败 errCode=" + errCode + "  " + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "进入房间成功", Toast.LENGTH_SHORT).show();
                startLivePlay(url);
            }
        });
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
    }
}
