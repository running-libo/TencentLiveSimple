package com.cp.tencentlivesimple.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cp.tencentlivesimple.R;
import com.cp.tencentlivesimple.activity.PullStreamActivity;
import com.cp.tencentlivesimple.activity.RoomListActivity;

import java.util.List;

/**
 * create by apple
 * create on 2020/8/27
 * description
 */
public class RoomAdapter extends BaseRvAdapter<RoomListActivity.RoomInfo, RecyclerView.ViewHolder> {

    public RoomAdapter(Context context, List<RoomListActivity.RoomInfo> datas) {
        super(context, datas);
    }

    @Override
    protected void onBindData(RecyclerView.ViewHolder holder, RoomListActivity.RoomInfo data, int position) {
        Glide.with(context).load(data.coverUrl).into(((RoomViewHolder)holder).ivCover);
        ((RoomViewHolder)holder).tvUserName.setText(data.anchorName);
        ((RoomViewHolder)holder).tvRoomName.setText(data.roomName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PullStreamActivity.class);
            intent.putExtra("roomId", data.roomId);
            intent.putExtra("url", data.playUrl);
            intent.putExtra("anchorId", data.anchorId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_room, parent, false));
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvUserName, tvRoomName;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCover = itemView.findViewById(R.id.ivCover);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
        }
    }
}
