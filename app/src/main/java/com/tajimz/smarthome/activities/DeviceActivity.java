package com.tajimz.smarthome.activities;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.adapter.RecyclerDeviceAdapter;
import com.tajimz.smarthome.add.AddActivity;
import com.tajimz.smarthome.databinding.ActivityDeviceBinding;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.util.List;

public class DeviceActivity extends AppCompatActivity {
    SqliteDB sqliteDB;
    List<DeviceModel> list;
    String roomId;
    ActivityDeviceBinding binding;
    RecyclerDeviceAdapter recyclerDeviceAdapter;
    Boolean recyclerInited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupRecycler();
        setupClickListener();
    }

    private void setupRecycler(){
        roomId = getIntent().getStringExtra("roomId");
        sqliteDB = new SqliteDB(this);
        list = sqliteDB.getDevices(roomId);
        if (list.isEmpty()){
            binding.tvDeviceNotFound.setVisibility(VISIBLE);

        }
        recyclerDeviceAdapter = new RecyclerDeviceAdapter(this, list);
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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!recyclerInited) return;
        list.clear();
        list.addAll(sqliteDB.getDevices(roomId));
        recyclerDeviceAdapter.notifyDataSetChanged();
        if (list.isEmpty()) binding.tvDeviceNotFound.setVisibility(VISIBLE);

    }
}