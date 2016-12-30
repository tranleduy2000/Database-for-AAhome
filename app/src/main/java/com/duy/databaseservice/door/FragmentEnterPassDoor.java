package com.duy.databaseservice.door;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.databaseservice.MainActivity;
import com.duy.databaseservice.R;
import com.duy.databaseservice.data.Preferences;
import com.duy.databaseservice.utils.Protocol;

public class FragmentEnterPassDoor extends Fragment {
    private View mContainer;
    private Button btnOk, btnReset, btnForget;
    private EditText editInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = inflater.inflate(R.layout.fragment_enter_pass_door, container, false);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        editInput = (EditText) findViewById(R.id.edit_input);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editInput.getText().toString();
                if (s.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Mat khau khong duoc trong", Toast.LENGTH_SHORT).show();

                } else {
                    String p = Preferences.getString(getActivity().getApplicationContext(), Preferences.PASS_DOOR);
                    if (p.equals(s)) {
                        //do something
                        Toast.makeText(getActivity().getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity())
                                .sendCommand(Protocol.POST + Protocol.CLOSE_DOOR);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.wrong_pass, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ResetPassDoorActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btnForget = (Button) findViewById(R.id.btn_forget_pass);
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return mContainer;
    }


    private View findViewById(int id) {
        return mContainer.findViewById(id);
    }

}
