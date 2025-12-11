package com.tajimz.smarthome.model;

public class RoomModel {
    String roomName ;
    String roomIcon;
    String roomId;

    public RoomModel(String roomId, String RoomName, String RoomIcon) {
        this.roomName = RoomName;
        this.roomIcon = RoomIcon;
        this.roomId = roomId;
    }
    public String getRoomName() {
        return roomName;
    }

    public String getRoomIcon() {
        return roomIcon;
    }
    public String getRoomId() {
        return roomId;
    }




}
