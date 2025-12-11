package com.tajimz.smarthome.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tajimz.smarthome.bnav.DeviceFragment;
import com.tajimz.smarthome.bnav.HomeFragment;
import com.tajimz.smarthome.bnav.SettingsFragment;

public class ViewpagerMain extends FragmentStateAdapter {
    public ViewpagerMain(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();

            case 1:
                return new DeviceFragment();

            case 2:
                return new SettingsFragment();

            default:
                return new HomeFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
