package com.tajimz.smarthome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.activities.DeviceActivity;
import com.tajimz.smarthome.databinding.ItemRoom2Binding;
import com.tajimz.smarthome.model.RoomModel;

import java.util.List;

public class RecyclerRoomAdapter extends RecyclerView.Adapter<RecyclerRoomAdapter.RoomViewHolder> {
    List<RoomModel> list;
    Context context;
    public RecyclerRoomAdapter(Context context, List<RoomModel> list){
        this.list = list;
        this.context = context;

    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        ItemRoom2Binding binding = ItemRoom2Binding.inflate(inflater, parent, false);

        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomModel roomModel = list.get(position);
        holder.binding.tvRoomName.setText(roomModel.getRoomName());
        holder.binding.imgRoom2.setImageResource(Integer.parseInt(roomModel.getRoomIcon()));


        holder.binding.imgMenu.setOnClickListener(v->{
            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenuInflater().inflate(R.menu.menu_room, menu.getMenu());
            menu.show();
        });

        holder.binding.roomParent.setOnClickListener(v->{
            Intent intent = new Intent(context, DeviceActivity.class);
            intent.putExtra("roomId", roomModel.getRoomId());
            context.startActivity(intent);
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
            ItemRoom2Binding binding;
        public RoomViewHolder(ItemRoom2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
