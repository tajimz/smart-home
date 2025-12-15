package com.tajimz.smarthome.helper;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import ai.bongotech.bt.BongoBT;

public class BluetoothHelper {
    BongoBT bongoBT;
    Context context;
      String temp = "null";

    public BluetoothHelper(Context context){
        bongoBT = new BongoBT(context);
        this.context = context;
    }

    public Boolean isConnected(){
        BluetoothDevice device = bongoBT.getConnectedDevice();
        return device!=null;
    }

    public void sendCommand(String text){
        bongoBT.sendCommand(text);
    }

    public void connectToController(String mac, BongoBT.BtConnectListener listener){
        bongoBT.connectTo(mac, listener);
    }

    public void setTemp(String temp){
        this.temp = temp;
    }
    public String getTemp(){
        return temp;
    }








}
