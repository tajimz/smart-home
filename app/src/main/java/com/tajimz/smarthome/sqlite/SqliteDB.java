package com.tajimz.smarthome.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.tajimz.smarthome.helper.CONSTANTS;

public class SqliteDB extends SQLiteOpenHelper {
    public SqliteDB(Context context) {
        super(context, CONSTANTS.sqliteDbName, null, CONSTANTS.sqliteDbNVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS roomList (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, roomName TEXT, roomIcon TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS deviceList (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, deviceName TEXT, deviceIcon TEXT,deviceType TEXT,turnOnCMD TEXT, turnOfCMD TEXT, roomID TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS roomList");
        db.execSQL("DROP TABLE IF EXISTS deviceList");
        onCreate(db);

    }


}
