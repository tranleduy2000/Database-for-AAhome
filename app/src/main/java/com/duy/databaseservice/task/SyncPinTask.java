package com.duy.databaseservice.task;

import com.duy.databaseservice.FirebaseListener;
import com.duy.databaseservice.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Sync Pin arduino and upload it to server
 * Created by Duy on 27-Oct-16.
 */

public class SyncPinTask {
    private final int mCountPin = 53;
    private MainActivity context;
    private FirebaseListener mFirebase;

    public SyncPinTask(MainActivity context) {
        this.context = context;
        mFirebase = new FirebaseListener(context);
    }

    protected Void doInBackground(Void... params) {
        String url = "users/" + mFirebase.getUid() + "/" + "devices/";
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(url);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList deviceItems = (ArrayList) dataSnapshot.getValue();
                for (int i = 0; i < deviceItems.size(); i++) {
                    HashMap map = (HashMap) deviceItems.get(i);
                    String pin = String.valueOf(map.get("pin"));
                    boolean status = Boolean.valueOf(String.valueOf(map.get("on")));
                /*    context.sendCommand(Protocol.POST + pin + " " +
                            (status ? "1" : "0"));*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return null;
    }
}
