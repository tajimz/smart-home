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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tajimz.smarthome.MainActivity;
import com.tajimz.smarthome.adapter.RecyclerDeviceAdapter;
import com.tajimz.smarthome.adapter.RecyclerRoomAdapter;
import com.tajimz.smarthome.databinding.FragmentHomeBinding;
import com.tajimz.smarthome.helper.AlarmHelper;
import com.tajimz.smarthome.helper.BluetoothHelper;
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
    BluetoothHelper bluetoothHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setupRecyclerRoom();
        return binding.getRoot();
    }
    private void setupRecyclerRoom(){
        bluetoothHelper = MainActivity.bluetoothHelper;
        sqliteDB = new SqliteDB(getContext());
        list = sqliteDB.getRooms();
        recyclerRoomAdapter = new RecyclerRoomAdapter(getContext(), list, true, new RecyclerRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(RoomModel roomModel) {
            deviceList = sqliteDB.getDevices(roomModel.getRoomId());
                 recyclerDeviceAdapter = new RecyclerDeviceAdapter(getContext(), deviceList, new RecyclerDeviceAdapter.OnDeviceClickListener() {
                     @Override
                     public void onDeviceClick(DeviceModel deviceModel) {

                     }
                 });
                 binding.recyclerDevice.setAdapter(recyclerDeviceAdapter);
                 binding.recyclerDevice.setLayoutManager(new GridLayoutManager(getContext(), 2));
                lastRoomId = roomModel.getRoomId();
                if (deviceList.isEmpty()) binding.tvNoDeviceFound.setVisibility(VISIBLE);
                else binding.tvNoDeviceFound.setVisibility(GONE);
            }

            @Override
            public void onPopupItemSelected(RoomModel roomModel, MenuItem menuItem) {
                //unnecessary here
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
        binding.tvTemp.setText(bluetoothHelper.getTemp().trim()+"° C");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.tvTemp.setText(bluetoothHelper.getTemp().trim()+"° C");
            }
        },4000);
        list.clear();
        list.addAll(sqliteDB.getRooms());

        if (!recyclerInited || list.isEmpty() ) {
            binding.tvNoRoomsFound.setVisibility(VISIBLE);
            binding.tvNoDeviceFound.setVisibility(VISIBLE);
            recyclerRoomAdapter.notifyDataSetChanged();
            try{
                deviceList.clear();
                recyclerDeviceAdapter.notifyDataSetChanged();
            }catch (RuntimeException e){

            }

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




}