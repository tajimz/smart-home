package com.tajimz.smarthome.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.activities.DeviceActivity;
import com.tajimz.smarthome.databinding.ItemRoom2Binding;
import com.tajimz.smarthome.databinding.ItemRoomBinding;
import com.tajimz.smarthome.model.RoomModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.List;

public class RecyclerRoomAdapter extends RecyclerView.Adapter<RecyclerRoomAdapter.RoomViewHolder> {
    List<RoomModel> list;
    Context context;
    Boolean forHome;
    OnItemClickListener onItemClickListener;
    int selectedPosition = 0;
    Boolean firstLoad = false;
    SqliteDB sqliteDB;

    public RecyclerRoomAdapter(Context context, List<RoomModel> list, Boolean forHome, OnItemClickListener onItemClickListener){
        this.list = list;
        this.context = context;
        this.forHome = forHome;
        this.onItemClickListener = onItemClickListener;
        this.sqliteDB = new SqliteDB(context);

    }
    public interface OnItemClickListener {
            void onItemSelected(RoomModel roomModel);
    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        if (forHome) return new RoomViewHolder(ItemRoomBinding.inflate(inflater, parent, false));
        else return new RoomViewHolder(ItemRoom2Binding.inflate(inflater, parent, false));




    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomModel roomModel = list.get(position);

        if (forHome){
            ItemRoomBinding binding = (ItemRoomBinding) holder.binding;
            binding.tvRoomName.setText(roomModel.getRoomName());
            binding.imgRoom.setImageResource(Integer.parseInt(roomModel.getRoomIcon()));
            if (position == selectedPosition){
                binding.tvRoomName.setTextColor(ContextCompat.getColor(context, R.color.cream));
                ImageViewCompat.setImageTintList(binding.imgRoom, ContextCompat.getColorStateList(context, R.color.cream));
                binding.roomParent.setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_bg));

            }else {
                binding.tvRoomName.setTextColor(ContextCompat.getColor(context, R.color.black));
                ImageViewCompat.setImageTintList(binding.imgRoom, ContextCompat.getColorStateList(context, R.color.lightBlack));
                binding.roomParent.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            }


            binding.roomParent.setOnClickListener(v->{
                selectRoom(position);

            });

        }else {
            ItemRoom2Binding binding = (ItemRoom2Binding) holder.binding;
            binding.tvRoomName.setText(roomModel.getRoomName());
            binding.imgRoom2.setImageResource(Integer.parseInt(roomModel.getRoomIcon()));
            binding.tvRoomDevices.setText(sqliteDB.getDeviceCount(roomModel.getRoomId())+" Devices");
            binding.imgMenu.setOnClickListener(v->{
                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenuInflater().inflate(R.menu.menu_room, menu.getMenu());
                menu.show();
            });

            binding.roomParent.setOnClickListener(v->{
                Intent intent = new Intent(context, DeviceActivity.class);
                intent.putExtra("roomId", roomModel.getRoomId());
                context.startActivity(intent);
            });
        }










    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
            ViewBinding binding;

        public RoomViewHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void selectRoom(int position){
        if (list == null || list.isEmpty()) return;
        int previous = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previous);
        notifyItemChanged(selectedPosition);

        if (onItemClickListener != null) onItemClickListener.onItemSelected(list.get(position));

    }

}
