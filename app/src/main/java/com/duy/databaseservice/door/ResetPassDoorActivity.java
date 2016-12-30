package com.duy.databaseservice.door;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.duy.databaseservice.R;
import com.duy.databaseservice.data.Preferences;

public class ResetPassDoorActivity extends AppCompatActivity {
    private EditText editCurrent, editNew, editNew2;
    private Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_door);
        mPreferences = new Preferences(this);
        editCurrent = (EditText) findViewById(R.id.editText2);
        editNew = (EditText) findViewById(R.id.editText3);
        editNew2 = (EditText) findViewById(R.id.editText4);
        Button btnOk = (Button) findViewById(R.id.btnOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String p = mPreferences.getString(Preferences.PASS_DOOR).trim();
                final String s1 = editCurrent.getText().toString().trim();
                final String s2 = editNew.getText().toString().trim();
                final String s3 = editNew2.getText().toString().trim();
                if (!s1.equals(p)) {
                    showDialog(getString(R.string.wrong_pass));
                } else {
                    if (!s2.equals(s3)) {
                        editNew2.setError(getString(R.string.text_3));
                    } else {
                        mPreferences.putString(Preferences.PASS_DOOR, s2);
                        showDialog("Thành công!");
                    }
                }
            }
        });
    }

    private void showDialog(String s) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(s);
        b.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        b.create().show();
    }
}
