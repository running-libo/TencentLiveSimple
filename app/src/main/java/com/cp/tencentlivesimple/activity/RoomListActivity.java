package com.cp.tencentlivesimple.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.adapter.RoomAdapter;
import com.cp.tencentlivesimple.livingroom.CallService;
import com.cp.tencentlivesimple.login.model.RoomManager;
import com.cp.tencentlivesimple.login.model.TCConstants;
import com.cp.tencentlivesimple.model.TRTCLiveRoom;
import com.cp.tencentlivesimple.model.TRTCLiveRoomDef;
import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private RoomAdapter roomAdapter;
    private RecyclerView rvRoom;
    private List<RoomInfo> mRoomInfoList = new ArrayList<>();   //保存从网络侧加载到的直播间信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room_list);

        rvRoom = findViewById(R.id.recyclerview);
        rvRoom.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        roomAdapter = new RoomAdapter(this, mRoomInfoList);
        rvRoom.setAdapter(roomAdapter);

        getRoomList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CallService.start(this);
    }

    /**
     * 获取房间列表  index表第几页
     */
    private void getRoomList() {
        // 首先从后台获取 房间列表的id
        RoomManager.getInstance().getRoomList(TCConstants.TYPE_LIVE_ROOM, new RoomManager.GetRoomListCallback() {
            @Override
            public void onSuccess(final List<String> roomIdList) {
                // 从组件出获取房间信息
                List<Integer> roomList = new ArrayList<>();
                for (String id : roomIdList) {
                    try {
                        roomList.add(Integer.parseInt(id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                TRTCLiveRoom.sharedInstance(getApplicationContext()).getRoomInfos(roomList, (code, msg, list) -> {
                    if (code == 0) {
                        mRoomInfoList.clear();
                        for (TRTCLiveRoomDef.TRTCLiveRoomInfo trtcLiveRoomInfo : list) {
                            RoomInfo info = new RoomInfo();
                            info.anchorId = trtcLiveRoomInfo.ownerId;
                            info.anchorName = trtcLiveRoomInfo.ownerName;
                            info.roomName = trtcLiveRoomInfo.roomName;
                            info.roomId = trtcLiveRoomInfo.roomId;
                            info.coverUrl = trtcLiveRoomInfo.coverUrl;
                            info.audiencesNum = trtcLiveRoomInfo.memberCount;
                            info.playUrl = trtcLiveRoomInfo.streamUrl;
                            mRoomInfoList.add(info);
                        }

                        roomAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailed(int code, String msg) {
                Toast.makeText(RoomListActivity.this, "获取列表失败 " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class RoomInfo {
        public String roomName;
        public int roomId;
        public String anchorName;
        public String coverUrl;
        public int    audiencesNum;
        public String anchorId;
        public String playUrl;
    }

    /**
     * 跳转到开播页面
     * @param view
     */
    public void startLive(View view) {
        startActivity(new Intent(this, PushStreamActivity.class));
    }
}