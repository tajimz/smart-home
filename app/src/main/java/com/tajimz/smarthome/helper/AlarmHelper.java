package com.tajimz.smarthome.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tajimz.smarthome.MainActivity;

public class AlarmHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String cmdToSend = intent.getStringExtra("commandToSend");
        Log.d("bongoBT", cmdToSend);

    }
}
