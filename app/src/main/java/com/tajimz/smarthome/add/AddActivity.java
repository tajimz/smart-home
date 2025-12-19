package com.tajimz.smarthome.add;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.adapter.RecyclerIconAdapter;
import com.tajimz.smarthome.databinding.ActivityAddBinding;
import com.tajimz.smarthome.helper.BaseActivity;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.model.IconModel;
import com.tajimz.smarthome.model.RoomModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends BaseActivity {
    ActivityAddBinding binding;
    String reason, roomId;
    RecyclerIconAdapter recyclerIconAdapter;
    List<IconModel> list = new ArrayList<>();
    RoomModel roomModel;
    DeviceModel deviceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupEdgeToEdge();


        handleLogic();
        setupClickListeners();
        setupRecycler();
    }


    private void handleLogic(){
         reason = getIntent().getStringExtra("reason");
         roomId = getIntent().getStringExtra("roomId");
         if (reason.equals("device")) binding.tvReason.setText("Add New Device: ");
         else if (reason.equals("room")) {
             binding.tvReason.setText("Add New Room: ");
             binding.edLayType.setVisibility(GONE);
             binding.edLayTurnOn.setVisibility(GONE);
             binding.edLayTurnOff.setVisibility(GONE);
         }else if (reason.equals("room_edit")){
             binding.edLayType.setVisibility(GONE);
             binding.edLayTurnOn.setVisibility(GONE);
             binding.edLayTurnOff.setVisibility(GONE);
              roomModel = (RoomModel) getIntent().getSerializableExtra("roomModel");
             binding.tvReason.setText("Edit Room: "+roomModel.getRoomName());
             binding.edName.setText(roomModel.getRoomName());

         }else if (reason.equals("device_edit")){
             deviceModel = (DeviceModel) getIntent().getSerializableExtra("deviceModel");
             binding.tvReason.setText("Edit Device: "+deviceModel.getDeviceName());
             binding.edName.setText(deviceModel.getDeviceName());
             binding.edTurnOn.setText(deviceModel.getTurnOnCMD());
             binding.edTurnOff.setText(deviceModel.getTurnOffCMD());

         }

    }
    private void setupClickListeners(){
        SqliteDB sqliteDB = new SqliteDB(this);

        binding.btnAdd.setOnClickListener(v->{
            String name = binding.edName.getText().toString();
            String type = binding.spinCategory.getSelectedItem().toString();
            String onCmd = binding.edTurnOn.getText().toString();
            String ofCmd = binding.edTurnOff.getText().toString();
            String icon = recyclerIconAdapter.getSelected();
            if (name.isEmpty()) return;

            if (reason.equals("device")){
                if (type.isEmpty() || onCmd.isEmpty() || ofCmd.isEmpty() || type.equals("Select Category")) return;
                sqliteDB.insertDevice(name, icon, type, onCmd, ofCmd,roomId );
                Toast.makeText(this, "Created Device : "+name, Toast.LENGTH_SHORT).show();

            }else if (reason.equals("room")){
                sqliteDB.insertRoom(name, icon);
                Toast.makeText(this, "Created Room : "+name, Toast.LENGTH_SHORT).show();

            }else if (reason.equals("room_edit")){
                sqliteDB.editRoom(roomModel.getRoomId(), name, icon);
                Toast.makeText(this, "Edited Room : "+name, Toast.LENGTH_SHORT).show();

            }else if (reason.equals("device_edit")){
                if (type.isEmpty() || onCmd.isEmpty() || ofCmd.isEmpty() || type.equals("Select Category")) return;
                sqliteDB.editDevice(deviceModel.getDeviceId(), name,  type,icon, onCmd, ofCmd );
                Toast.makeText(this, "Edited Device : "+name, Toast.LENGTH_SHORT).show();

            }

            onBackPressed();
        });

        binding.imgBack.setOnClickListener(v->{onBackPressed();});

    }

    private void setupRecycler(){

        //add every icons from list started with baseline_
        Field[] drawables = R.drawable.class.getFields();
        for (Field f : drawables){
            try {
                if (f.getName().startsWith("baseline_")) {
                    int id = f.getInt(null);
                    list.add(new IconModel(id));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        recyclerIconAdapter = new RecyclerIconAdapter(this, list);
        binding.recyclerIcon.setAdapter(recyclerIconAdapter);
        binding.recyclerIcon.setLayoutManager(new GridLayoutManager(this, 5));

    }
}