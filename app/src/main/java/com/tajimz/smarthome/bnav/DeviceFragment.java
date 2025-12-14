package com.tajimz.smarthome.bnav;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tajimz.smarthome.MainActivity;
import com.tajimz.smarthome.adapter.RecyclerRoomAdapter;
import com.tajimz.smarthome.add.AddActivity;
import com.tajimz.smarthome.databinding.FragmentDeviceBinding;
import com.tajimz.smarthome.helper.CONSTANTS;
import com.tajimz.smarthome.model.RoomModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.List;

import ai.bongotech.bt.BongoBT;


public class DeviceFragment extends Fragment {
    FragmentDeviceBinding binding;
    SqliteDB sqliteDB;
    List<RoomModel> list;
    RecyclerRoomAdapter recyclerRoomAdapter;
    Boolean recyclerInited = false;
    BongoBT bongoBT;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        setupRecycler();
        setupClickListeners();


        return binding.getRoot();
    }

    private void setupRecycler(){
        bongoBT = MainActivity.bongoBT;
        sqliteDB = new SqliteDB(getContext());
         list = sqliteDB.getRooms();
        if (list.isEmpty()){
            binding.tvRoomNotFound.setVisibility(VISIBLE);

        }
        recyclerRoomAdapter = new RecyclerRoomAdapter(getContext(), list, false,null);
        binding.recyclerDevice.setAdapter(recyclerRoomAdapter);
        binding.recyclerDevice.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerInited = true;







    }

    private void setupClickListeners(){
        binding.btnAdd.setOnClickListener(v->{

            Intent intent = new Intent(requireContext(), AddActivity.class);
            intent.putExtra("reason","room");
            startActivity(intent);


        });

        binding.tvConnectController.setOnClickListener(v->{
            if ((boolean) binding.tvConnectController.getTag()) {
                //disconnect
            }else {
                //connect
                connectController();
            }
        });
    }

    private void checkControllerStatus(){
        if (MainActivity.deviceConnected){
            binding.tvController.setText("Controller Connected");
            binding.tvConnectController.setText("Disconnect");
            // false if controller not connected
            binding.tvConnectController.setTag(true);
        }else {
            binding.tvController.setText("Controller Not Connected");
            binding.tvConnectController.setText("Connect");
            binding.tvConnectController.setTag(false);
        }
    }

    private void connectController(){
        bongoBT.connectTo(CONSTANTS.CONTROLLER_MAC, new BongoBT.BtConnectListener() {
            @Override
            public void onConnected() {
                MainActivity.deviceConnected = true;
                checkControllerStatus();
                Log.d("bongoBT", "connected");
                bongoBT.sendCommand("off");
            }

            @Override
            public void onReceived(String message) {

            }

            @Override
            public void onError(String reason) {
                Log.d("bongoBT", reason);
                Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show();
                MainActivity.deviceConnected = false;
                checkControllerStatus();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //refreshRecycler
        checkControllerStatus();
        if (!recyclerInited) return;
        list.clear();
        list.addAll(sqliteDB.getRooms());
        recyclerRoomAdapter.notifyDataSetChanged();

        if (list.isEmpty()) binding.tvRoomNotFound.setVisibility(VISIBLE);
        else binding.tvRoomNotFound.setVisibility(GONE);


    }
}