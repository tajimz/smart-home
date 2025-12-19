package com.tajimz.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.smarthome.databinding.ItemRoom2Binding;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerScheduleAdapter extends RecyclerView.Adapter<RecyclerScheduleAdapter.ScheduleViewHolder> {
    Context context;
    List<DeviceModel> list;
    SqliteDB sqliteDB;
    Boolean isEmpty;

    public RecyclerScheduleAdapter(Context context){
        this.context = context;
        sqliteDB = new SqliteDB(context);
        list = sqliteDB.getDevicesScheduled();
        if (list.isEmpty()) isEmpty = true;
        else isEmpty = false;

    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(ItemRoom2Binding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        DeviceModel deviceModel = list.get(position);
        holder.binding.tvRoomName.setText(deviceModel.getDeviceName());
        holder.binding.imgRoom2.setImageResource(Integer.parseInt(deviceModel.getDeviceIcon()));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(deviceModel.getAlarm()));
        holder.binding.tvRoomDevices.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        ItemRoom2Binding binding;
        public ScheduleViewHolder(ItemRoom2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void refresh(){
        list.clear();
        list.addAll(sqliteDB.getDevicesScheduled());
        notifyDataSetChanged();
        if (list.isEmpty()) isEmpty = true;
        else isEmpty = false;
    }

    public boolean isEmptyRecycler(){
        return isEmpty;
    }
}
