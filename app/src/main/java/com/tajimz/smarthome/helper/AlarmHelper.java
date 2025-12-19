package com.tajimz.smarthome.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String cmdToSend = intent.getStringExtra("commandToSend");
        Log.d("bongoBT", cmdToSend);
        Intent intent1 = new Intent(context, ForegroundService.class);
        intent1.putExtra("command", cmdToSend);
        context.startForegroundService(intent1);


    }
}
