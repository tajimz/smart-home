package com.tajimz.smarthome.add;

import static android.view.View.GONE;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.databinding.ActivityAddBinding;
import com.tajimz.smarthome.sqlite.SqliteDB;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    String reason, roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        handleLogic();
        setupClickListeners();
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
            String icon = String.valueOf(R.drawable.bed_solid_full);
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

    }
}