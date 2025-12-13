package com.tajimz.smarthome;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationBarView;
import com.tajimz.smarthome.adapter.ViewpagerMain;
import com.tajimz.smarthome.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setupEdgeToEdgePadding();


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
                else if (postion==R.id.settings) postion = 2;

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
                else if (position == 2) menuId = R.id.settings;
                binding.bnavMain.setSelectedItemId(menuId);
            }
        });

    }
    private void setupEdgeToEdgePadding(){
        EdgeToEdge.enable(this);
        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
    }
}