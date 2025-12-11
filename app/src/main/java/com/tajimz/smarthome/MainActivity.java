package com.tajimz.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tajimz.smarthome.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // -------------------------
        // ROOM LIST (HORIZONTAL)
        // -------------------------
        ArrayList<RoomModel> roomList = new ArrayList<>();
        roomList.add(new RoomModel("Bedroom"));
        roomList.add(new RoomModel("Living Room"));
        roomList.add(new RoomModel("Kitchen"));
        roomList.add(new RoomModel("Bathroom"));
        roomList.add(new RoomModel("Store"));

        binding.recyclerRoom.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.recyclerRoom.setAdapter(new RoomAdapter(roomList));


        // -------------------------
        // DEVICE LIST (VERTICAL)
        // -------------------------
        ArrayList<DeviceModel> deviceList = new ArrayList<>();
        deviceList.add(new DeviceModel("Lamp Home", "Smart Bulb"));
        deviceList.add(new DeviceModel("Fan", "Smart Fan"));
        deviceList.add(new DeviceModel("AC", "Air Conditioner"));
        deviceList.add(new DeviceModel("TV", "Smart TV"));
        deviceList.add(new DeviceModel("Router", "WiFi"));
        deviceList.add(new DeviceModel("Lamp Home", "Smart Bulb"));
        deviceList.add(new DeviceModel("Fan", "Smart Fan"));
        deviceList.add(new DeviceModel("AC", "Air Conditioner"));
        deviceList.add(new DeviceModel("TV", "Smart TV"));
        deviceList.add(new DeviceModel("Router", "WiFi"));

        deviceList.add(new DeviceModel("Lamp Home", "Smart Bulb"));
        deviceList.add(new DeviceModel("Fan", "Smart Fan"));
        deviceList.add(new DeviceModel("AC", "Air Conditioner"));
        deviceList.add(new DeviceModel("TV", "Smart TV"));
        deviceList.add(new DeviceModel("Router", "WiFi"));
        deviceList.add(new DeviceModel("Lamp Home", "Smart Bulb"));
        deviceList.add(new DeviceModel("Fan", "Smart Fan"));
        deviceList.add(new DeviceModel("AC", "Air Conditioner"));
        deviceList.add(new DeviceModel("TV", "Smart TV"));
        deviceList.add(new DeviceModel("Router", "WiFi"));

        binding.recyclerDevice.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerDevice.setAdapter(new DeviceAdapter(deviceList));

    }

    // ----------------------------------------------------------------
    // ROOM MODEL
    // ----------------------------------------------------------------
    class RoomModel {
        String name;
        RoomModel(String name) { this.name = name; }
    }

    // ----------------------------------------------------------------
    // ROOM ADAPTER (HORIZONTAL)
    // ----------------------------------------------------------------
    class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {

        ArrayList<RoomModel> list;

        RoomAdapter(ArrayList<RoomModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_room, parent, false);
            return new RoomHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
            RoomModel model = list.get(position);
            //holder.title.setText(model.name);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class RoomHolder extends RecyclerView.ViewHolder {
            TextView title;
            RoomHolder(@NonNull View itemView) {
                super(itemView);
                // change ID to your TextView ID inside item_room.xml
                //title = itemView.findViewById(R.id.textView);
            }
        }
    }

    // ----------------------------------------------------------------
    // DEVICE MODEL
    // ----------------------------------------------------------------
    class DeviceModel {
        String name, type;

        DeviceModel(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    // ----------------------------------------------------------------
    // DEVICE ADAPTER (VERTICAL)
    // ----------------------------------------------------------------
    class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

        ArrayList<DeviceModel> list;

        DeviceAdapter(ArrayList<DeviceModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_device, parent, false);
            return new DeviceHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
            DeviceModel model = list.get(position);

            holder.name.setText(model.name);
            holder.type.setText(model.type);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class DeviceHolder extends RecyclerView.ViewHolder {

            TextView name, type;

            DeviceHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.itemName);
                type = itemView.findViewById(R.id.itemType);
            }
        }
    }

}
