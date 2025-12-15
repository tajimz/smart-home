package com.tajimz.smarthome.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tajimz.smarthome.R;
import com.tajimz.smarthome.adapter.RecyclerDeviceAdapter;
import com.tajimz.smarthome.add.AddActivity;
import com.tajimz.smarthome.databinding.ActivityDeviceBinding;
import com.tajimz.smarthome.databinding.BottomItemDeviceBinding;
import com.tajimz.smarthome.helper.AlarmHelper;
import com.tajimz.smarthome.helper.BaseActivity;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.Calendar;
import java.util.List;

public class DeviceActivity extends BaseActivity {
    SqliteDB sqliteDB;
    List<DeviceModel> list;
    String roomId, title;
    ActivityDeviceBinding binding;
    RecyclerDeviceAdapter recyclerDeviceAdapter;
    Boolean recyclerInited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdgeToEdge();
        setupRecycler();
        setupClickListener();
    }

    private void setupRecycler(){
        roomId = getIntent().getStringExtra("roomId");
        title = getIntent().getStringExtra("title");
        binding.tvReason.setText(title);
        if (title.equals("Schedule Device")) binding.btnAdd.setVisibility(GONE);

        sqliteDB = new SqliteDB(this);
        list = sqliteDB.getDevices(roomId);
        if (list.isEmpty()){
            binding.tvDeviceNotFound.setVisibility(VISIBLE);

        }
        recyclerDeviceAdapter = new RecyclerDeviceAdapter(this, list, new RecyclerDeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(DeviceModel deviceModel) {
                BottomItemDeviceBinding deviceBinding = BottomItemDeviceBinding.inflate(getLayoutInflater());
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DeviceActivity.this);
                bottomSheetDialog.setContentView(deviceBinding.getRoot());

                deviceBinding.editDevice.setOnClickListener(v->{
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(DeviceActivity.this, AddActivity.class);
                    intent.putExtra("reason", "device_edit");
                    intent.putExtra("deviceModel", deviceModel);
                    startActivity(intent);

                });
                deviceBinding.deleteDevice.setOnClickListener(v->{
                    bottomSheetDialog.dismiss();
                    sqliteDB.deleteDevice(deviceModel.getDeviceId());
                    refresh();


                });

                if (title.equals("Schedule Device")){
                    deviceBinding.editDevice.setText("Schedule Device");
                    deviceBinding.editDevice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_stopwatch, 0, 0, 0);
                    deviceBinding.deleteDevice.setVisibility(GONE);

                    deviceBinding.editDevice.setOnClickListener(v->{
                        showDatePicker(deviceModel);
                        bottomSheetDialog.dismiss();

                    });
                }
                bottomSheetDialog.show();


            }
        });
        binding.recyclerDevice.setAdapter(recyclerDeviceAdapter);
        binding.recyclerDevice.setLayoutManager(new GridLayoutManager(this,2));
        recyclerInited = true;





    }

    private void setupClickListener(){
        binding.btnAdd.setOnClickListener(v->{
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra("reason", "device");
            intent.putExtra("roomId", roomId);
            startActivity(intent);
        });

        binding.imgBack.setOnClickListener(v->{onBackPressed();});
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!recyclerInited) return;
        refresh();


    }

    private void refresh(){
        list.clear();
        list.addAll(sqliteDB.getDevices(roomId));
        recyclerDeviceAdapter.notifyDataSetChanged();
        if (list.isEmpty()) binding.tvDeviceNotFound.setVisibility(VISIBLE);
        else binding.tvDeviceNotFound.setVisibility(GONE);
    }

    Calendar calendar = Calendar.getInstance();
    private void showDatePicker(DeviceModel deviceModel){
        if (!checkAlarmPermission()){
            sendToGrantAlarm();
            Toast.makeText(this, "You must allow alarm permission to set schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
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

        Intent intent = new Intent(this, AlarmHelper.class);
        intent.putExtra("time", calendar.getTimeInMillis());
        intent.putExtra("commandToSend", deviceModel.getTurnOnCMD());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(deviceModel.getDeviceId()), intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm Has set successfully", Toast.LENGTH_SHORT).show();
        sqliteDB.setAlarm(deviceModel.getDeviceId(), calendar.getTimeInMillis());
        onBackPressed();





    }

    private Boolean checkAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            AlarmManager alarmManager = getSystemService(AlarmManager.class);
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