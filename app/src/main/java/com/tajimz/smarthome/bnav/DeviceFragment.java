package com.tajimz.smarthome.bnav;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.adapter.RecyclerRoomAdapter;
import com.tajimz.smarthome.add.AddActivity;
import com.tajimz.smarthome.databinding.FragmentDeviceBinding;
import com.tajimz.smarthome.model.RoomModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.List;


public class DeviceFragment extends Fragment {
    FragmentDeviceBinding binding;
    SqliteDB sqliteDB;
    List<RoomModel> list;
    RecyclerRoomAdapter recyclerRoomAdapter;
    Boolean recyclerInited = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        setupRecycler();
        setupClickListeners();


        return binding.getRoot();
    }

    private void setupRecycler(){
        sqliteDB = new SqliteDB(getContext());
         list = sqliteDB.getRooms();
        if (list.isEmpty()){
            binding.tvRoomNotFound.setVisibility(VISIBLE);

        }
        recyclerRoomAdapter = new RecyclerRoomAdapter(getContext(), list);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        //refreshRecycler
        if (!recyclerInited) return;
        list.clear();
        list.addAll(sqliteDB.getRooms());
        recyclerRoomAdapter.notifyDataSetChanged();

        if (list.isEmpty()) binding.tvRoomNotFound.setVisibility(VISIBLE);


    }
}