package com.tajimz.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tajimz.smarthome.databinding.ItemDeviceBinding;
import com.tajimz.smarthome.model.DeviceModel;

import java.util.List;

public class RecyclerDeviceAdapter extends RecyclerView.Adapter<RecyclerDeviceAdapter.DeviceViewHolder> {
    Context context;
    List<DeviceModel> list;

    public RecyclerDeviceAdapter(Context context, List<DeviceModel> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeviceBinding binding = ItemDeviceBinding.inflate(LayoutInflater.from(context), parent, false);

        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        DeviceModel deviceModel = list.get(position);
        holder.binding.itemName.setText(deviceModel.getDeviceName());
        holder.binding.itemType.setText(deviceModel.getDeviceType());
        holder.binding.imgItem.setImageResource(Integer.parseInt(deviceModel.getDeviceIcon()));




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        ItemDeviceBinding binding;
        public DeviceViewHolder(ItemDeviceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
