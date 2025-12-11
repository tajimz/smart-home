package com.tajimz.smarthome.bnav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tajimz.smarthome.R;
import com.tajimz.smarthome.databinding.FragmentDeviceBinding;


public class DeviceFragment extends Fragment {
    FragmentDeviceBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        setupRecycler();


        return binding.getRoot();
    }

    private void setupRecycler(){

    }
}