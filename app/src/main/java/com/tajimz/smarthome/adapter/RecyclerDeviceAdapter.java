package com.tajimz.smarthome.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.smarthome.MainActivity;
import com.tajimz.smarthome.databinding.ItemDeviceBinding;
import com.tajimz.smarthome.helper.BluetoothHelper;
import com.tajimz.smarthome.model.DeviceModel;

import java.util.Calendar;
import java.util.List;

import ai.bongotech.bt.BongoBT;

public class RecyclerDeviceAdapter extends RecyclerView.Adapter<RecyclerDeviceAdapter.DeviceViewHolder> {
    Context context;
    List<DeviceModel> list;
    BluetoothHelper bluetoothHelper = MainActivity.bluetoothHelper;
    OnDeviceClickListener onDeviceClickListener;
    Calendar calendar;


    public RecyclerDeviceAdapter(Context context, List<DeviceModel> list, OnDeviceClickListener onDeviceClickListener){
        this.context = context;
        this.list = list;
        this.onDeviceClickListener = onDeviceClickListener;

    }

    public interface OnDeviceClickListener{
        void onDeviceClick(DeviceModel deviceModel);
    }
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeviceBinding binding = ItemDeviceBinding.inflate(LayoutInflater.from(context), parent, false);

        return new DeviceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        calendar = Calendar.getInstance();
        DeviceModel deviceModel = list.get(position);
        long alarm = deviceModel.getAlarm();
        holder.binding.itemName.setText(deviceModel.getDeviceName());
        if (alarm > calendar.getTimeInMillis()) {
            holder.binding.timerOn.setVisibility(VISIBLE); // future alarm
        } else {
            holder.binding.timerOn.setVisibility(GONE);    // past/no alarm
        }

        Log.d("bongoBT", ""+calendar.getTimeInMillis());
        Log.d("bongoBT", ""+alarm);

        holder.binding.itemType.setText(deviceModel.getDeviceType());
        holder.binding.imgItem.setImageResource(Integer.parseInt(deviceModel.getDeviceIcon()));
        holder.binding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!bluetoothHelper.isConnected()) {
                Toast.makeText(context, "Controller Not connected", Toast.LENGTH_SHORT).show();
                buttonView.setChecked(!isChecked);
                return;
            }
            if (isChecked) {
                Toast.makeText(context, "on", Toast.LENGTH_SHORT).show();
                bluetoothHelper.sendCommand(deviceModel.getTurnOnCMD());
                Log.d("bongoBT", deviceModel.getTurnOnCMD());
            }
            else {
                bluetoothHelper.sendCommand(deviceModel.getTurnOffCMD());
                Log.d("bongoBT", deviceModel.getTurnOffCMD());
            }

        });


        holder.binding.deviceParent.setOnClickListener(v->{onDeviceClickListener.onDeviceClick(deviceModel);});



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
