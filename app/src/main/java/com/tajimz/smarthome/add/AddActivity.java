package com.tajimz.smarthome.add;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.adapter.RecyclerIconAdapter;
import com.tajimz.smarthome.databinding.ActivityAddBinding;
import com.tajimz.smarthome.model.IconModel;
import com.tajimz.smarthome.sqlite.SqliteDB;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    String reason, roomId;
    RecyclerIconAdapter recyclerIconAdapter;
    List<IconModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
         }

    }
    private void setupClickListeners(){
        SqliteDB sqliteDB = new SqliteDB(this);

        binding.btnAdd.setOnClickListener(v->{
            String name = binding.edName.getText().toString();
            String type = binding.edType.getText().toString();
            String onCmd = binding.edTurnOn.getText().toString();
            String ofCmd = binding.edTurnOff.getText().toString();
            String icon = recyclerIconAdapter.getSelected();
            if (name.isEmpty()) return;

            if (reason.equals("device")){
                if (type.isEmpty() || onCmd.isEmpty() || ofCmd.isEmpty()) return;
                sqliteDB.insertDevice(name, icon, type, onCmd, ofCmd,roomId );
                Toast.makeText(this, "Created Device : "+name, Toast.LENGTH_SHORT).show();

            }else if (reason.equals("room")){
                sqliteDB.insertRoom(name, icon);
                Toast.makeText(this, "Created Room : "+name, Toast.LENGTH_SHORT).show();

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