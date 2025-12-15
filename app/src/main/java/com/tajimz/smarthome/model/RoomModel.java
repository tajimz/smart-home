package com.tajimz.smarthome.model;

import java.io.Serializable;

public class RoomModel implements Serializable {
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
