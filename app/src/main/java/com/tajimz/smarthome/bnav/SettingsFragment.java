package com.tajimz.smarthome.bnav;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tajimz.smarthome.activities.DeviceActivity;
import com.tajimz.smarthome.adapter.RecyclerScheduleAdapter;
import com.tajimz.smarthome.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    RecyclerScheduleAdapter recyclerScheduleAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        setupRecycler();
        setupClickListeners();

        return binding.getRoot();
    }

    private void setupRecycler(){
        recyclerScheduleAdapter = new RecyclerScheduleAdapter(getContext());
        binding.recyclerSchedule.setAdapter(recyclerScheduleAdapter);
        binding.recyclerSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupClickListeners(){
        binding.btnAdd.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), DeviceActivity.class);
            intent.putExtra("roomId","all");
            intent.putExtra("title","Schedule Device");

            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerScheduleAdapter.refresh();
        if (recyclerScheduleAdapter.isEmptyRecycler()) binding.tvScheduleNotFound.setVisibility(VISIBLE);
        else binding.tvScheduleNotFound.setVisibility(GONE);
    }
}
