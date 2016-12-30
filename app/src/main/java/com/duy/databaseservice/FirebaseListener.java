package com.duy.databaseservice;

import android.content.Context;
import android.util.Log;

import com.duy.databaseservice.data.Database;
import com.duy.databaseservice.items.DeviceItem;
import com.duy.databaseservice.utils.Protocol;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Duy on 19/7/2016
 */
public class FirebaseListener {
    private static final String PIN = "pin";
    private static final String USERS = "users";
    private static final String MODE = "mode";
    private static final String URL_FIREBASE = "https://smarthome-f6176.firebaseio.com";
    private static final String TAG = FirebaseListener.class.getName();
    private Context mContext;
    private FirebaseUser mUser;
    private Firebase mFirebase;
    private Database database;
    private EventListener eventListener = null;
    /**
     * Sync Pin of devices
     */
    private ArrayList<DeviceItem> arrayList = new ArrayList<>();

    public FirebaseListener(Context context, EventListener eventListener) {
        this.mContext = context;
        Firebase.setAndroidContext(context);
        mFirebase = new Firebase(URL_FIREBASE);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            Log.w(TAG, mUser.getUid());
        } else {
            Log.w(TAG, "None user");
        }
        this.eventListener = eventListener;
        this.database = new Database(context);
        doExecute();
    }

    public FirebaseListener(Context applicationContext) {
        this.mContext = applicationContext;
        Firebase.setAndroidContext(applicationContext);
        mFirebase = new Firebase(URL_FIREBASE);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            Log.w(TAG, mUser.getUid());
        } else {
            Log.w(TAG, "None user");
        }
    }

    private void doExecute() {
        arrayList.clear();
        String url = "users/" + getUid() + "/" + "devices/";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ArrayList deviceItems = (ArrayList) dataSnapshot.getValue();
                    for (int i = 0; i < deviceItems.size(); i++) {
                        HashMap map = (HashMap) deviceItems.get(i);
                        //get pin
                        final String pin = String.valueOf(map.get("id"));
                        //init for pin
                        addListenerToPin(pin);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addListenerToPin(final String pin) {
        String url = "users/" + getUid() + "/" + "pin/" + pin;
        //query to database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    eventListener.sendCommand(Protocol.POST + (b ? Protocol.SET_ON : Protocol.SET_OFF) + pin);
                    Log.d(TAG, "onDataChange: " + pin + " " + b);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setPin(int pin, boolean value) {
        mFirebase.child(USERS).child(mUser.getUid())
                .child(PIN).child(String.valueOf(pin)).setValue(value);
    }

    @Deprecated
    public void setMode(String mode, boolean value) {
        mFirebase.child(USERS).child(mUser.getUid())
                .child(MODE).child(String.valueOf(mode)).setValue(value);
    }

    public String getUid() {
        return mUser.getUid();
    }

    public boolean getStatusPin(int pin) {
        return true;
    }
}
