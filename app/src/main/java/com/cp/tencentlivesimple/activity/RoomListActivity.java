package com.cp.tencentlivesimple.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.adapter.RoomAdapter;
import com.cp.tencentlivesimple.livingroom.GenerateTestUserSig;
import com.cp.tencentlivesimple.livingroom.IMLVBLiveRoomListener;
//import com.cp.tencentlivesimple.livingroom.MLVBLiveRoom;
import com.cp.tencentlivesimple.roomutil.commondef.LoginInfo;
import com.cp.tencentlivesimple.roomutil.commondef.RoomInfo;

import java.util.ArrayList;

public class RoomListActivity extends AppCompatActivity {

    private ArrayList<RoomInfo> rooms = new ArrayList<>();
    private RoomAdapter roomAdapter;
    private RecyclerView rvRoom;
    private String userId = "1234567890";
    private String userName = "libobo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        rvRoom = findViewById(R.id.recyclerview);
        rvRoom.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        roomAdapter = new RoomAdapter(this, rooms);
        rvRoom.setAdapter(roomAdapter);

        loginRoom();
    }

    /**
     * 登录房间
     */
    private void loginRoom() {
        LoginInfo loginInfo = new LoginInfo(GenerateTestUserSig.SDKAPPID, userId,
                userName, "https://upload-images.jianshu.io/upload_images/8669504-e759203a15a1acee.jpeg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp", GenerateTestUserSig.genTestUserSig(userId));
//        MLVBLiveRoom.sharedInstance(getApplicationContext()).login(loginInfo, new IMLVBLiveRoomListener.LoginCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Toast.makeText(getApplicationContext(), "登录失败,errCode= " + errCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess() {
//                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
//                getRoomList();
////                getAudienceLists();
//            }
//        });
    }

    /**
     * 获取房间列表  index表第几页
     */
    private void getRoomList() {
//        MLVBLiveRoom.sharedInstance(getApplicationContext()).getRoomList(0, 10, new IMLVBLiveRoomListener.GetRoomListCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Toast.makeText(getApplicationContext(), "获取房间失败,errCode=" + errCode + " " + errInfo, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSuccess(ArrayList<RoomInfo> roomInfoList) {
//                Toast.makeText(getApplicationContext(), "获取房间成功", Toast.LENGTH_SHORT).show();
//                rooms.addAll(roomInfoList);
//                roomAdapter.notifyDataSetChanged();
//            }
//        });
    }

    /**
     * 跳转到开播页面
     * @param view
     */
    public void startLive(View view) {
        startActivity(new Intent(this, PushStreamActivity.class));
    }
}