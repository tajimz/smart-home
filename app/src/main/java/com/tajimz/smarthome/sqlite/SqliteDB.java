package com.tajimz.smarthome.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tajimz.smarthome.helper.CONSTANTS;
import com.tajimz.smarthome.model.DeviceModel;
import com.tajimz.smarthome.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

public class SqliteDB extends SQLiteOpenHelper {
    public SqliteDB(Context context) {
        super(context, CONSTANTS.sqliteDbName, null, CONSTANTS.sqliteDbNVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS roomList (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, roomName TEXT, roomIcon TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS deviceList (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, deviceName TEXT, deviceIcon TEXT,deviceType TEXT,turnOnCMD TEXT, turnOfCMD TEXT, roomID TEXT, alarm INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS roomList");
        db.execSQL("DROP TABLE IF EXISTS deviceList");
        onCreate(db);

    }

    public List<RoomModel> getRooms(){
        List<RoomModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM roomList", null);

        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String icon = cursor.getString(2);
            String id = cursor.getString(0);
            list.add(new RoomModel(id, name, icon));
        }
        return list;


    }

    public void insertRoom(String roomName, String roomIcon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("roomName", roomName);
        contentValues.put("roomIcon", roomIcon);
        db.insert("roomList", null, contentValues);

    }

    public List<DeviceModel> getDevices (String roomId){
        List<DeviceModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deviceList WHERE roomID ="+roomId, null);

        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String icon = cursor.getString(2);
            String type = cursor.getString(3);
            String onCMD = cursor.getString(4);
            String offCMD = cursor.getString(5);
            String roomID = cursor.getString(6);
            long alarm = cursor.getLong(7);
            list.add(new DeviceModel(id, name, icon, type, onCMD, offCMD, roomID, alarm));
        }
        return list;



    }

    public int getDeviceCount(String roomId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM deviceList WHERE roomID ="+roomId, null);
        return cursor.getCount();
    }

    public void insertDevice(String name, String icon, String type, String onCmd, String offCmd, String roomId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("deviceName", name);
        contentValues.put("deviceIcon", icon);
        contentValues.put("deviceType", type);
        contentValues.put("turnOnCMD", onCmd);
        contentValues.put("turnOfCMD", offCmd);
        contentValues.put("roomID", roomId);
        contentValues.put("alarm", 0);
        db.insert("deviceList", null, contentValues);
    }

    public void setAlarm(String id, long timeInMs){
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE deviceList SET alarm = " + timeInMs +
                " WHERE id = " + id;

        db.execSQL(sql);
    }

    public void deleteRoom(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM roomList WHERE id = " + id;
        db.execSQL(sql);
        sql = "DELETE FROM deviceList WHERE roomID = " + id;
        db.execSQL(sql);

    }

    public void editRoom(String roomId, String roomName, String roomIcon){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE roomList SET roomName = '" + roomName + "', roomIcon = '" + roomIcon + "' WHERE id = '" + roomId + "'";
        db.execSQL(sql);
    }





}
