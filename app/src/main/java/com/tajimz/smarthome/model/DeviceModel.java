package com.tajimz.smarthome.model;

public class DeviceModel {
    String deviceName, deviceId, deviceType, turnOnCMD, turnOffCMD, deviceIcon, roomId;

    public DeviceModel(String deviceId,String deviceName, String deviceIcon, String deviceType,
                       String turnOnCMD, String turnOffCMD,  String roomId) {
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.turnOnCMD = turnOnCMD;
        this.turnOffCMD = turnOffCMD;
        this.deviceIcon = deviceIcon;
        this.roomId = roomId;
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
}
