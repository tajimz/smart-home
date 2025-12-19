package com.tajimz.smarthome;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationBarView;
import com.tajimz.smarthome.adapter.ViewpagerMain;
import com.tajimz.smarthome.databinding.ActivityMainBinding;
import com.tajimz.smarthome.helper.BaseActivity;
import com.tajimz.smarthome.helper.BluetoothHelper;
import com.tajimz.smarthome.helper.CONSTANTS;

import ai.bongotech.bt.BongoBT;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    public static BluetoothHelper bluetoothHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setupEdgeToEdgePadding();
        initBluetoothHelper();
        setupBottomNavigation();
    }

    private void setupBottomNavigation(){
        ViewpagerMain viewpagerMain = new ViewpagerMain(this);
        binding.pager2.setAdapter(viewpagerMain);

        binding.bnavMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int postion = menuItem.getItemId();

                if (postion==R.id.home) postion = 0;
                else if (postion==R.id.device) postion = 1;
                else if (postion==R.id.schedule) postion = 2;

                binding.pager2.setCurrentItem(postion);

                return true;
            }
        });
        binding.pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int menuId = R.id.home;
                if (position == 0) menuId = R.id.home;
                else if (position == 1) menuId = R.id.device;
                else if (position == 2) menuId = R.id.schedule;
                binding.bnavMain.setSelectedItemId(menuId);
            }
        });

    }
    private void setupEdgeToEdgePadding(){
        EdgeToEdge.enable(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
    }

    private void initBluetoothHelper(){
        bluetoothHelper = new BluetoothHelper(this);
       connectController(this,()->{});

    }
    public static void connectController(Context context, Runnable runnable){
        startLoading(context);
        bluetoothHelper.connectToController(CONSTANTS.CONTROLLER_MAC, new BongoBT.BtConnectListener() {
            @Override
            public void onConnected() {
                endLoading();
                Log.d("bongoBT", "connected");
                runnable.run();
            }

            @Override
            public void onReceived(String message) {

                Log.d("bongoBT", message);
                if (message.startsWith("TEMP: ")) {
                    bluetoothHelper.setTemp(message.substring(6));
                }
            }

            @Override
            public void onError(String reason) {
                endLoading();
                Log.d("bongoBT", reason);
                runnable.run();


            }
        });

    }

}