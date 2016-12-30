package com.duy.databaseservice.task;

import com.duy.databaseservice.FirebaseListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by edoga on 15-Oct-16.
 */

public class EnvironmentTask {
    private FirebaseListener mFirebase;

    public EnvironmentTask(FirebaseListener firebase) {
        this.mFirebase = firebase;
    }

    public void saveTemp(int temp) {
        String date = new Date().toString();
        String url = "users/" + mFirebase.getUid() + "/" + "temp/" + date;
        String url2 = "users/" + mFirebase.getUid() + "/" + "temp/" + "current";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(url2);
        mDatabase.setValue(temp);
        mDatabase2.setValue(temp);
    }

    public void saveHumi(int humi) {
        String date = new Date().toString();
        String url = "users/" + mFirebase.getUid() + "/" + "humi/" + date;
        String url2 = "users/" + mFirebase.getUid() + "/" + "humi/" + "current";

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(url2);

        mDatabase.setValue(humi);
        mDatabase2.setValue(humi);
    }

    public void saveLightValue(int valueLight) {
        String date = new Date().toString();
        String url = "users/" + mFirebase.getUid() + "/" + "value_light/" + date;
        String url2 = "users/" + mFirebase.getUid() + "/" + "value_light/" + "current";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(url2);

        mDatabase.setValue(valueLight);
        mDatabase2.setValue(valueLight);
    }

    public void saveGasValue(int valueGas) {
        String date = new Date().toString();
        String url = "users/" + mFirebase.getUid() + "/" + "value_gas/" + date;
        String url2 = "users/" + mFirebase.getUid() + "/" + "value_gas/" + "current";

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference(url2);

        mDatabase.setValue(valueGas);
        mDatabase2.setValue(valueGas);
    }
}
