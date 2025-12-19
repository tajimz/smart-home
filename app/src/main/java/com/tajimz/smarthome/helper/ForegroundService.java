package com.tajimz.smarthome.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tajimz.smarthome.R;

import java.io.OutputStream;
import java.util.UUID;

import ai.bongotech.bt.BongoBT;

public class ForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,createForegroundNotification() );
        Log.d("bongoBT", "foreground created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("bongoBT", "I am here");
        String command = intent.getStringExtra("command");
        Log.d("bongoBT","command "+command);
        sendCommand("98:DA:60:0C:3B:89", command);

        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createForegroundNotification(){
        NotificationChannel channel = new NotificationChannel("bluetooth_channel", "Device Connected", NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        return new NotificationCompat.Builder(this, "bluetooth_channel")
                .setContentTitle("Device Connected")
                .setContentText("IoT device is connected, Tap to disconnect")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .build();
    }

    private void sendCommand(String mac, String command) {
        new Thread(() -> {
            try {
                // Get Bluetooth adapter
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter == null) {
                    Log.e("bongoBT", "Bluetooth not supported");
                    return;
                }

                // Get remote device by MAC
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mac);

                // Standard HC-06 UUID
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);

                // Cancel discovery (improves connection)
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }

                // Connect
                socket.connect();
                Log.d("bongoBT", "Connected to " + mac);

                // Send command
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(command.getBytes());
                outputStream.flush();
                Log.d("bongoBT", "Command sent: " + command);

                // Close connection
                outputStream.close();
                socket.close();
                Log.d("bongoBT", "Disconnected from device");
                stopSelf();

            } catch (Exception e) {
                Log.e("bongoBT", "Error sending command: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }


}
