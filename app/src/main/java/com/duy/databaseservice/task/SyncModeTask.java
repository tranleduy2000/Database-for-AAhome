package com.duy.databaseservice.task;

import android.util.Log;

import com.duy.databaseservice.EventListener;
import com.duy.databaseservice.FirebaseListener;
import com.duy.databaseservice.utils.Protocol;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.duy.databaseservice.utils.Protocol.AUTO_ROOF;
import static com.duy.databaseservice.utils.Protocol.FIRE_ALRAM_SYSTEM;

/**
 * Mode listener ...
 * Created by edoga on 22-Oct-16.
 */

public class SyncModeTask {
    private EventListener listener;
    private FirebaseListener mFirebase;
    private String TAG = SyncModeTask.class.getName();

    public SyncModeTask(EventListener listener, FirebaseListener mFirebase) {
        this.listener = listener;
        this.mFirebase = mFirebase;
    }

    /**
     * cài đặt cho tất cả các hệ thống.
     */
    public void doExecute() {
        setupAutoLight();
        setupFireAlarm();
        setupSecurity();
        setUpAutoRoof();
        setUpAutoDoor();
    }

    /**
     * cài đặt sự kiện  mái che tự động
     */
    private void setUpAutoRoof() {
        String url = "users/" + mFirebase.getUid() + "/" + "mode/" + AUTO_ROOF;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_AUTO_ROOF +
                            (b ? "1" : "0"));
                    Log.w(TAG, "AUTO_ROOF " + b);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * cài đặt sự kiện cửa tự động
     */
    private void setUpAutoDoor() {
        String url = "users/" + mFirebase.getUid() + "/" + "mode/" + Protocol.AUTO_DOOR;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_AUTO_DOOR +
                            (b ? "1" : "0"));
                    Log.w(TAG, "AUTO_DOOR " + b);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * cài đặt sự kiện cho hệ thống báo động
     */
    private void setupFireAlarm() {
        String url = "users/" + mFirebase.getUid() + "/" + "mode/" + FIRE_ALRAM_SYSTEM;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_FIRE_ALRAM_SYSTEM +
                            (b ? "1" : "0"));
                    Log.w(TAG, "FIRE_ALRAM_SYSTEM " + b);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * cài đặt sự kiện cho hệ thống đèn tự động
     */
    private void setupAutoLight() {
        String url2 = "users/" + mFirebase.getUid() + "/" + "mode/" + Protocol.AUTO_LIGHT_DIGITAL;
        final DatabaseReference db2 = FirebaseDatabase.getInstance().getReference(url2);
        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_AUTO_LIGHT_DIGITAL +
                            (b ? "1" : "0"));
                    Log.w(TAG, "AUTO_LIGHT_DIGITAL " + b);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String url = "users/" + mFirebase.getUid() + "/" + "mode/" + Protocol.AUTO_LIGHT_ANALOG;
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_AUTO_LIGHT_ANALOG +
                            (b ? "1" : "0"));
                    Log.w(TAG, "AUTO_LIGHT_ANALOG " + b);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * cài đặt sự kiện cho hệ thống bảo mật
     */
    private void setupSecurity() {
        String url = "users/" + mFirebase.getUid() + "/" + "mode/" + Protocol.SECURITY;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    listener.sendCommand(Protocol.POST + Protocol.SET_SECURITY +
                            (b ? "1" : "0"));
                    Log.w(TAG, "SECURITY " + b);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
