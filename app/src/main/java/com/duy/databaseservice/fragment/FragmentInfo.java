package com.duy.databaseservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.databaseservice.EventListener;
import com.duy.databaseservice.FirebaseListener;
import com.duy.databaseservice.R;
import com.duy.databaseservice.data.Preferences;
import com.duy.databaseservice.items.DeviceItem;
import com.duy.databaseservice.task.EnvironmentListener;
import com.duy.databaseservice.task.EnvironmentManager;
import com.duy.databaseservice.task.SyncModeTask;
import com.duy.databaseservice.utils.Math;

/**
 * Created by Duy on 13-Oct-16.
 */
public class FragmentInfo extends Fragment implements EnvironmentListener, EventListener {
    public TextView txtLight, txtAutoLight, txtTempC, txtTempF, txtHumi;
    private Preferences preferences;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            preferences.putString(Preferences.NUMBER_PHONE, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private FirebaseListener mFirebase;
    private SyncModeTask syncModeTask;

    private EnvironmentManager environmentManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        preferences = new Preferences(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebase = new FirebaseListener(getContext(), this);
        syncModeTask = new SyncModeTask(this, mFirebase);
        syncModeTask.doExecute();
        environmentManager = new EnvironmentManager(mFirebase);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtLight = (TextView) view.findViewById(R.id.txt_light);
        txtAutoLight = (TextView) view.findViewById(R.id.txt_auto_light);
        txtTempC = (TextView) view.findViewById(R.id.txt_temp_c);
        txtTempF = (TextView) view.findViewById(R.id.txt_temp_f);
        txtHumi = (TextView) view.findViewById(R.id.txt_humi);

        EditText editPhone = (EditText) view.findViewById(R.id.edit_phone);
        String phone = new Preferences(getContext()).getString(Preferences.NUMBER_PHONE);
        editPhone.setText(phone);
        editPhone.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void deviceChangeInfo(int pos, DeviceItem deviceItem) {

    }

    @Override
    public void deviceDelete(int pos, DeviceItem deviceItem) {

    }

    @Override
    public void onDeviceClick(DeviceItem deviceItem) {

    }

    @Override
    public void sendCommand(String cmd) {

    }

    public void msg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onLightChange(int valueLight) {
        environmentManager.saveLightValue(valueLight);
    }

    @Override
    public void onGasChange(int valueGas) {
        environmentManager.saveGasValue(valueGas);
    }

    public void setPin(int pin, boolean isOn) {
        mFirebase.setPin(pin, isOn);
    }

    @Override
    public void onTemperature(int temp) {
        int tempF = Math.convertToF(String.valueOf(temp));
        //set text
        txtTempC.setText(String.valueOf(temp));
        txtTempF.setText(String.valueOf(tempF));

        environmentManager.saveTemp(temp);
    }

    @Override
    public void onHumidityChange(int humi) {
        txtHumi.setText(String.valueOf(humi));
        environmentManager.saveHumi(humi);
    }

}
