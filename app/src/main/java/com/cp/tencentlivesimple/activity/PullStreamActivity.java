package com.cp.tencentlivesimple.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.livingroom.GenerateTestUserSig;
import com.cp.tencentlivesimple.livingroom.IMLVBLiveRoomListener;
import com.cp.tencentlivesimple.livingroom.MLVBLiveRoom;
import com.cp.tencentlivesimple.roomutil.commondef.LoginInfo;
import com.cp.tencentlivesimple.roomutil.commondef.RoomInfo;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import java.util.ArrayList;

/**
 * create by libo
 * create on 2019-10-29
 * description 拉流页
 */
public class PullStreamActivity extends BasePermissionActivity {
    private TXLivePlayer livePlayer;
    private TXCloudVideoView videoView;
    private String userId = "1234567890";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_stream);

        init();
        loginRoom();
    }

    private void init() {
        livePlayer = new TXLivePlayer(this);
        videoView = findViewById(R.id.videoview_pull);
        livePlayer.setPlayerView(videoView);
//        startLivePlay();
    }

    /**
     * 登录房间
     */
    private void loginRoom() {
        LoginInfo loginInfo = new LoginInfo(GenerateTestUserSig.SDKAPPID, userId,
                "libobo", "https://upload-images.jianshu.io/upload_images/8669504-e759203a15a1acee.jpeg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp", GenerateTestUserSig.genTestUserSig(userId));
        MLVBLiveRoom.sharedInstance(getApplicationContext()).login(loginInfo, new IMLVBLiveRoomListener.LoginCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "登录失败,errCode= " + errCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                getRoomList();
//                getAudienceLists();
            }
        });
    }

    /**
     * 获取房间列表  index表第几页
     */
    private void getRoomList() {
        MLVBLiveRoom.sharedInstance(getApplicationContext()).getRoomList(0, 10, new IMLVBLiveRoomListener.GetRoomListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Toast.makeText(getApplicationContext(), "获取房间失败,errCode=" + errCode + " " + errInfo, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<RoomInfo> roomInfoList) {
                Toast.makeText(getApplicationContext(), "获取房间成功", Toast.LENGTH_SHORT).show();
                enterRoom(roomInfoList.get(0).roomID);
                if (roomInfoList.size() >0) {
                    startLivePlay(roomInfoList.get(0).mixedPlayURL);
                }
            }
        });
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
