package com.cp.tencentlivesimple.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.xtablayout.XTabLayout;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.login.model.UserModel;
import com.cp.tencentlivesimple.model.TRTCLiveRoom;
import com.cp.tencentlivesimple.model.TRTCLiveRoomCallback;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * create by libo
 * create on 2019-10-29
 * description 拉流页
 */
public class PullStreamActivity extends BasePermissionActivity  {
    private TXCloudVideoView videoView;
    private int roomId;
    private String url;
    private XTabLayout tabLayout;
    private String[] titles = new String[]{"聊天", "主播", "排行", "贵宾"};
    private ImageView ivBack;
    private TextView tvSend;
    private String anchorId;
    private EditText etContent;
    /** 核心组件liveRoom */
    protected TRTCLiveRoom mLiveRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_stream);

        roomId = getIntent().getIntExtra("roomId", 0);
        url = getIntent().getStringExtra("url");
        anchorId = getIntent().getStringExtra("anchorId");

        mLiveRoom = TRTCLiveRoom.sharedInstance(this);

        init();
    }

    private void init() {
        videoView = findViewById(R.id.videoview_pull);
        ivBack = findViewById(R.id.ivBack);
        tvSend = findViewById(R.id.tvSend);
        etContent = findViewById(R.id.etContent);

        tabLayout = findViewById(R.id.tablayout);
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }

        ivBack.setOnClickListener(v -> finish());

        tvSend.setOnClickListener(v -> {

        });

        enterRoom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videoView.onDestroy();
        exitRoom();

    }

    private void enterRoom() {
        mLiveRoom.enterRoom(roomId, (code, msg) -> {

            if (code == 0) {
                Toast.makeText(PullStreamActivity.this, "进入房间成功", Toast.LENGTH_SHORT).show();

                mLiveRoom.startPlay(anchorId, videoView, (code1, msg1) -> {
                    if (code1 == 0) {
                        Toast.makeText(PullStreamActivity.this, "播放成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PullStreamActivity.this, "播放失败 " + msg1, Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Toast.makeText(PullStreamActivity.this, "进入房间失败 " + msg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * 退出房间
     */
    private void exitRoom() {
        mLiveRoom.exitRoom((code, msg) -> {
            if (code == 0) {
                Toast.makeText(PullStreamActivity.this, "退出房间成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PullStreamActivity.this, "退出房间失败 " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
