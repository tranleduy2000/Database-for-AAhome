package com.duy.databaseservice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.duy.databaseservice.data.Preferences;
import com.duy.databaseservice.utils.JsonReader;
import com.duy.databaseservice.utils.Protocol;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by edoga on 13-Oct-16.
 */

public class ProcessCommandArduino {
    private final String TAG = ProcessCommandArduino.class.getSimpleName();
    private long timeLastCall = 0;
    private MainActivity activity;
    private Context context;
    private Preferences preferences;

    public ProcessCommandArduino(MainActivity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        preferences = new Preferences(context);
    }

    public void doProcess(String cmd) {
        try {
            JSONObject data = JsonReader.parseJsonFromText(cmd);
            Log.e("JSON", data.toString());
            if (data.has(Protocol.TEMPERATURE)) {
                int tempC = Integer.parseInt(data.getString(Protocol.TEMPERATURE));
                activity.onTemperature(tempC);
                Log.w("TEMP ", data.getString(Protocol.TEMPERATURE));
                preferences.putInt(Preferences.TEMP_LAST, tempC);
            }
            if (data.has(Protocol.HUMIDITY)) {
                int progress = Integer.parseInt(data.getString(Protocol.HUMIDITY));
                Log.w("HUMI", data.getString(Protocol.HUMIDITY));
                activity.onHumidityChange(progress);
                preferences.putInt(Preferences.HUMI_LAST, progress);
            }
            if (data.has(Protocol.PIN)) {
                int pin = data.getInt(Protocol.PIN);
                boolean isOn = data.getBoolean(Protocol.VALUE_PIN);
                activity.setPin(pin, isOn);
            }

            if (data.has(Protocol.VALUE_LIGHT_SENSOR)) {
                int s = Integer.parseInt(data.getString(Protocol.VALUE_PIN));
                activity.onLightChange(s);
                preferences.putInt(Preferences.VALUE_LIGHT_SENSOR_LAST, data.getInt(Protocol.VALUE_PIN));
            }

            if (data.has(Protocol.CALL_PHONE)) {
                Log.d(TAG, "doProcess: has call phone");
                long time = new Date().getTime();
                Log.d(TAG, "doProcess: time = " + time + " | " + timeLastCall);
                if (time - timeLastCall > 5000) {
                    timeLastCall = time;
                    callPhone();
                } else {
                    Log.d(TAG, "doProcess: not enough time");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callPhone() {
        Log.d(TAG, "callPhone: begin method");
        try {
            String number = Preferences.getString(context, Preferences.NUMBER_PHONE);
            Log.d(TAG, "callPhone: number phone: " + number);
            if (!number.isEmpty()) {
                Log.d(TAG, "doCall: " + number);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d(TAG, "callPhone: can not call because not permission");
                    return;
                }
                activity.startActivityForResult(callIntent, 1212);
            } else Toast.makeText(context, "Nullable phone number!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
