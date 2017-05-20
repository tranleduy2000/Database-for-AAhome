package com.duy.databaseservice.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Duy on 19/7/2016
 */
public abstract class BaseActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
    }

    /**
     * show dialog
     *
     * @param title - title for dialog
     * @param msg   - messenge for dialog
     */
    protected void showDialog(String title, String msg) {
        builder.setTitle(title).setMessage(msg).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();
    }

    /**
     * show process dialog
     *
     * @param msg        -  messenge for dialog
     * @param cancelable - cancelable dialog
     */
    protected void showProgressDialog(String msg, boolean cancelable) {
        dialog.setCancelable(cancelable);
        dialog.setMessage(msg);
        dialog.show();
    }

    /**
     * Hide process dialog
     */
    protected void hideProgress() {
        try {
            if (dialog.isShowing())
                dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dismiss dialog
     */
    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }
}
