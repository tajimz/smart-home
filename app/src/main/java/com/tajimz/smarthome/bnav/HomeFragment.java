package com.tajimz.smarthome.bnav;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tajimz.smarthome.adapter.RecyclerDeviceAdapter;
import com.tajimz.smarthome.adapter.RecyclerRoomAdapter;
import com.tajimz.smarthome.databinding.FragmentHomeBinding;
import com.tajimz.smarthome.helper.AlarmHelper;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.model.RoomModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    SqliteDB sqliteDB;
    List<RoomModel> list;
    List<DeviceModel> deviceList;
    RecyclerRoomAdapter recyclerRoomAdapter;
    Boolean recyclerInited = false;
    RecyclerDeviceAdapter recyclerDeviceAdapter;

    String lastRoomId = "0";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setupRecyclerRoom();
        return binding.getRoot();
    }
    private void setupRecyclerRoom(){
        sqliteDB = new SqliteDB(getContext());
        list = sqliteDB.getRooms();
        recyclerRoomAdapter = new RecyclerRoomAdapter(getContext(), list, true, new RecyclerRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(RoomModel roomModel) {
            deviceList = sqliteDB.getDevices(roomModel.getRoomId());
                 recyclerDeviceAdapter = new RecyclerDeviceAdapter(getContext(), deviceList, new RecyclerDeviceAdapter.OnDeviceClickListener() {
                     @Override
                     public void onDeviceClick(DeviceModel deviceModel) {
                         showDatePicker(deviceModel);
                     }
                 });
                 binding.recyclerDevice.setAdapter(recyclerDeviceAdapter);
                 binding.recyclerDevice.setLayoutManager(new GridLayoutManager(getContext(), 2));
                lastRoomId = roomModel.getRoomId();
                if (deviceList.isEmpty()) binding.tvNoDeviceFound.setVisibility(VISIBLE);
                else binding.tvNoDeviceFound.setVisibility(GONE);
            }
        });
        binding.recyclerRoom.setAdapter(recyclerRoomAdapter);
        binding.recyclerRoom.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (!recyclerInited) recyclerRoomAdapter.selectRoom(0);
        recyclerInited = true;


    }


    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        list.addAll(sqliteDB.getRooms());

        if (!recyclerInited || list.isEmpty() ) {
            binding.tvNoRoomsFound.setVisibility(VISIBLE);
            binding.tvNoDeviceFound.setVisibility(VISIBLE);
            return;
        }
        binding.tvNoRoomsFound.setVisibility(GONE);
        recyclerRoomAdapter.notifyDataSetChanged();




        try {
            deviceList.clear();
            deviceList.addAll(sqliteDB.getDevices(lastRoomId));
            recyclerDeviceAdapter.notifyDataSetChanged();
            if (!deviceList.isEmpty()) binding.tvNoDeviceFound.setVisibility(GONE);
            else binding.tvNoDeviceFound.setVisibility(VISIBLE);

        } catch (RuntimeException e) {
            binding.tvNoDeviceFound.setVisibility(VISIBLE);

        }


    }


    Calendar calendar = Calendar.getInstance();
    private void showDatePicker(DeviceModel deviceModel){
        if (!checkAlarmPermission()){
            sendToGrantAlarm();
            Toast.makeText(getContext(), "You must allow alarm permission to set schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth)->{

                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    showTimePicker(deviceModel);

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void showTimePicker(DeviceModel deviceModel){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (timeView, hourOfDay, minute)->{

                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    setAlarm(calendar, deviceModel);

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();

    }

    private void setAlarm(Calendar calendar, DeviceModel deviceModel){

        Intent intent = new Intent(getContext(), AlarmHelper.class);
        intent.putExtra("time", calendar.getTimeInMillis());
        intent.putExtra("commandToSend", deviceModel.getTurnOnCMD());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(getContext(), "Alarm Has set successfully", Toast.LENGTH_SHORT).show();






    }

    private Boolean checkAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = requireContext().getSystemService(AlarmManager.class);
            if (alarmManager != null) {
                return alarmManager.canScheduleExactAlarms();
            } else {
                return false;
            }
        } else {

            return true;
        }
    }

    private void sendToGrantAlarm(){
        Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent);

    }

}