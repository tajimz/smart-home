package com.tajimz.smarthome.model;

public class DeviceModel {
    String deviceName, deviceId, deviceType, turnOnCMD, turnOffCMD, deviceIcon, roomId;
    long alarm;

    public DeviceModel(String deviceId,String deviceName, String deviceIcon, String deviceType,
                       String turnOnCMD, String turnOffCMD,  String roomId, long alarm) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.turnOnCMD = turnOnCMD;
        this.turnOffCMD = turnOffCMD;
        this.deviceIcon = deviceIcon;
        this.roomId = roomId;
        this.alarm = alarm;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getTurnOnCMD() {
        return turnOnCMD;
    }

    public String getTurnOffCMD() {
        return turnOffCMD;
    }

    public String getDeviceIcon() {
        return deviceIcon;
    }

    public String getRoomId() {
        return roomId;
    }

    public long getAlarm() {
        return alarm;
    }
}
