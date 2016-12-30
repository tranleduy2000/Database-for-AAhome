package com.duy.databaseservice.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.duy.databaseservice.items.DeviceItem;
import com.duy.databaseservice.items.MessengeItem;
import com.duy.databaseservice.items.ModeItem;

import java.util.ArrayList;

/**
 * Created by tranleduy on 22-May-16.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "databaseManagers";

    private static final String TABLE_DEVICES = "devices";
    private static final String KEY_ID_DEVICE = "id";
    private static final String KEY_DEVICE_NAME = "deviceName";
    private static final String KEY_DEVICE_DES = "deviceDes";
    private static final String KEY_DEVICE_COMAND = "deviceComand";
    private static final String KEY_DEVICE_TURN_ON = "isTurnOn";
    private static final String KEY_DEVICE_IMAGE = "deviceImg";

    public static final String CREATE_TABLE_DEVICES =
            "CREATE TABLE " + TABLE_DEVICES +
                    "(" +
                    KEY_ID_DEVICE + " INTEGER PRIMARY KEY," +
                    KEY_DEVICE_NAME + " TEXT," +
                    KEY_DEVICE_DES + " TEXT," +
                    KEY_DEVICE_COMAND + " TEXT," +
                    KEY_DEVICE_TURN_ON + " INTEGER, " +
                    KEY_DEVICE_IMAGE + " INTEGER" +
                    ")";

    // TODO: 08-Jun-16
    private static final String TABLE_COMMANDE = "commande";
    private static final String KEY_CMD_DATE = "cmdDate";
    private static final String KEY_CMD_TYPE = "cmdType"; //0 in, 1 out
    private static final String KEY_CMD_MSG = "cmdMsg";

    public static final String CREATE_TABLE_CMD =
            "CREATE TABLE " + TABLE_COMMANDE +
                    "(" +
                    KEY_CMD_DATE + " TEXT PRIMARY KEY," +
                    KEY_CMD_TYPE + " INTEGER," +
                    KEY_CMD_MSG + " TEXT" +
                    ")";

    // TODO: 08-Jun-16
    private static final String TABLE_MODES = "mode";
    private static final String KEY_ID_MODE = "idMode";
    private static final String KEY_MODE_NAME = "modeName";
    private static final String KEY_MODE_DES = "modeDes";
    private static final String KEY_MODE_COMAND = "modeCommand";
    private static final String KEY_MODE_TURN_ON = "isTurnOn";

    public static final String CREATE_TABLE_MODES =
            "CREATE TABLE " + TABLE_MODES +
                    "(" +
                    KEY_ID_MODE + " INTEGER PRIMARY KEY," +
                    KEY_MODE_NAME + " TEXT," +
                    KEY_MODE_DES + " TEXT," +
                    KEY_MODE_COMAND + " TEXT," +
                    KEY_MODE_TURN_ON + " INTEGER" +
                    ")";

    // TODO: 18-Jun-16
    private static final String TABLE_CHART = "chart_data";
    private static final String KEY_VALUE_TEMP_CHART = "value_temp";
    private static final String KEY_VALUE_HUMI_CHART = "value_humi";
    private static final String KEY_COLOR_CHART = "color";
    private static final String KEY_WIDTH_CHART = "width";
    private static final String KEY_TIME_CHART = "time";
    public static final String CREATE_TABLE_CHART =
            "CREATE TABLE " + TABLE_CHART +
                    "(" +
                    KEY_TIME_CHART + " LONG PRIMARY KEY," +
                    KEY_VALUE_TEMP_CHART + " INTEGER," +
                    KEY_VALUE_HUMI_CHART + " INTEGER," +
                    KEY_COLOR_CHART + " INTEGER," +
                    KEY_WIDTH_CHART + " INTEGER" +
                    ")";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long addDevice(DeviceItem deviceItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DEVICE, deviceItem.getId());
        values.put(KEY_DEVICE_NAME, deviceItem.getName());
        values.put(KEY_DEVICE_DES, deviceItem.getDes());
        values.put(KEY_DEVICE_COMAND, deviceItem.getCommand());
        values.put(KEY_DEVICE_TURN_ON, deviceItem.isOn() ? 0 : 1);
        values.put(KEY_DEVICE_IMAGE, deviceItem.getImg());
        return sqLiteDatabase.insert(TABLE_DEVICES, null, values);
    }


    public long removeDevice(DeviceItem deviceItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = String.valueOf(deviceItem.getId());
        long r = sqLiteDatabase.delete(TABLE_DEVICES, KEY_ID_DEVICE + "=?", new String[]{id});
        return r;
    }

    public ArrayList<DeviceItem> getAllDevice() {
        ArrayList<DeviceItem> deviceItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DEVICES;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DeviceItem deviceItem = new DeviceItem();
                deviceItem.setId(cursor.getInt((cursor.getColumnIndex(KEY_ID_DEVICE))));
                deviceItem.setName(cursor.getString((cursor.getColumnIndex(KEY_DEVICE_NAME))));
                deviceItem.setDes(cursor.getString((cursor.getColumnIndex(KEY_DEVICE_DES))));
                deviceItem.setCommand(cursor.getString((cursor.getColumnIndex(KEY_DEVICE_COMAND))));
                deviceItem.setOn((cursor.getInt(cursor.getColumnIndex(KEY_DEVICE_TURN_ON)) == 1));
                deviceItem.setImg(
//                        (cursor.getInt(cursor.getColumnIndex(KEY_DEVICE_IMAGE)) != -1)
//                        ? R.drawable.ic_air_conditioner :
                        cursor.getInt(cursor.getColumnIndex(KEY_DEVICE_IMAGE)));
                deviceItems.add(deviceItem);
                Log.e("DEVICE", deviceItem.getName());
            } while (cursor.moveToNext());
        }

        return deviceItems;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DEVICES);
        db.execSQL(CREATE_TABLE_CMD);
        db.execSQL(CREATE_TABLE_MODES);
        db.execSQL(CREATE_TABLE_CHART);
    }

    public long updateDevice(DeviceItem deviceItem) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID_DEVICE, deviceItem.getId());
        values.put(KEY_DEVICE_NAME, deviceItem.getName());
        values.put(KEY_DEVICE_DES, deviceItem.getDes());
        values.put(KEY_DEVICE_COMAND, deviceItem.getCommand());
        values.put(KEY_DEVICE_TURN_ON, deviceItem.isOn() ? 1 : 0);
        values.put(KEY_DEVICE_IMAGE, deviceItem.getImg());
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.update(
                TABLE_DEVICES,
                values,
                KEY_ID_DEVICE + "=?",
                new String[]{String.valueOf(deviceItem.getId())});
    }

    public long addMsg(MessengeItem messengeItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CMD_DATE, messengeItem.getDate());
        values.put(KEY_CMD_TYPE, messengeItem.getType());
        values.put(KEY_CMD_MSG, messengeItem.getBody());
        return sqLiteDatabase.insert(TABLE_COMMANDE, null, values);
    }

    public long removeMsg(MessengeItem messengeItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = messengeItem.getDate();
        return sqLiteDatabase.delete(TABLE_COMMANDE, KEY_CMD_DATE + "=?", new String[]{id});
    }

    public ArrayList<MessengeItem> getAllMsg() {
        ArrayList<MessengeItem> messengeItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COMMANDE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MessengeItem messengeItem = new MessengeItem();
                messengeItem.setDate(cursor.getString(cursor.getColumnIndex(KEY_CMD_DATE)));
                messengeItem.setType(cursor.getInt(cursor.getColumnIndex(KEY_CMD_TYPE)));
                messengeItem.setBody(cursor.getString(cursor.getColumnIndex(KEY_CMD_MSG)));
                messengeItems.add(messengeItem);
            } while (cursor.moveToNext());
        }

        return messengeItems;
    }

    public long addMode(ModeItem modeItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID_MODE, modeItem.getId());
        contentValues.put(KEY_MODE_NAME, modeItem.getName());
        contentValues.put(KEY_MODE_DES, modeItem.getDes());
        contentValues.put(KEY_MODE_COMAND, modeItem.getCommand());
        contentValues.put(KEY_MODE_TURN_ON, modeItem.isOn() ? 0 : 1);
        Log.e("ID DATABASE", String.valueOf(modeItem.getId()));
        return sqLiteDatabase.insert(TABLE_MODES, null, contentValues);
    }

    public long removeMode(ModeItem modeItem) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String id = String.valueOf(modeItem.getId());
        return sqLiteDatabase.delete(TABLE_MODES, KEY_ID_MODE + "=?", new String[]{id});
    }

    public long removeMode(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_MODES, KEY_ID_MODE + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<ModeItem> getAllMode() {
        ArrayList<ModeItem> modeItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MODES;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ModeItem modeItem = new ModeItem();
                modeItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID_MODE)));
                modeItem.setName(cursor.getString((cursor.getColumnIndex(KEY_MODE_NAME))));
                modeItem.setDes(cursor.getString((cursor.getColumnIndex(KEY_MODE_DES))));
                modeItem.setCommand(cursor.getString((cursor.getColumnIndex(KEY_MODE_COMAND))));
                modeItem.setOn((cursor.getInt(cursor.getColumnIndex(KEY_MODE_TURN_ON)) == 1));
                modeItems.add(modeItem);
            } while (cursor.moveToNext());
        }
        return modeItems;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHART);
        onCreate(db);
    }
}
