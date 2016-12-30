package com.duy.databaseservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.databaseservice.data.Database;
import com.duy.databaseservice.data.Preferences;
import com.duy.databaseservice.door.FragmentEnterPassDoor;
import com.duy.databaseservice.items.DeviceItem;
import com.duy.databaseservice.items.MessengeItem;
import com.duy.databaseservice.task.EnvironmentListener;
import com.duy.databaseservice.task.EnvironmentTask;
import com.duy.databaseservice.task.SyncModeTask;
import com.duy.databaseservice.utils.Math;
import com.duy.databaseservice.utils.Protocol;
import com.duy.databaseservice.utils.Variable;

import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements
        EventListener, Connector.ServerListener,
        EnvironmentListener {
    public static final String TAG = MainActivity.class.getName();
    //public DeviceAdapter deviceAdapter;
    public TextView txtLight, txtAutoLight, txtTempC, txtTempF, txtHumi;
    private RecyclerView rcCommand;
    private CommandAdapter commandAdapter;
    private EditText editCommand;
    private Connector connector = null;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private FirebaseListener mFirebase;
    private Database database;
    private Button btnConnect;
    private ProcessCommandArduino mProcess;
    private Preferences preferences;
    private EnvironmentTask environmentTask;
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
    private SyncModeTask syncModeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init data
        database = new Database(this);
        mFirebase = new FirebaseListener(this, this);
        preferences = new Preferences(this);
        //set view
        setContentView(R.layout.activity_main);
        initView();

        syncModeTask = new SyncModeTask(this, mFirebase);
        syncModeTask.doExecute();
        environmentTask = new EnvironmentTask(mFirebase);
        tryConnect();
        mProcess = new ProcessCommandArduino(this);
    }


    public void initView() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void tryConnect() {
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connector != null) {
                    if (!connector.isConnect()) {
                        Intent intent = new Intent(MainActivity.this, ChooseDeviceActivity.class);
                        startActivityForResult(intent, Variable.MO_ACTIVITY_CHON_THIET_BI);
                    } else {
                        connector.disconnect();
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, ChooseDeviceActivity.class);
                    startActivityForResult(intent, Variable.MO_ACTIVITY_CHON_THIET_BI);
                }
            }
        });
        if (!Preferences.getString(this, Preferences.ID_DEVICE_BLUE).trim().isEmpty()) {
            new ConnectBluetoothTask().execute(Preferences.getString(this, Preferences.ID_DEVICE_BLUE));
        }
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
        Log.d(TAG, "sendCommand: " + cmd);
        if (connector != null)
            connector.sendMessenge(cmd, commandAdapter);
        else Log.d(TAG, "sendCommand: socket is null");
    }

    public void msg(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Variable.MO_ACTIVITY_CHON_THIET_BI) {
            String address = data.getStringExtra("data");
            msg(address);
            Preferences.putString(this, Preferences.ID_DEVICE_BLUE, address);
            new ConnectBluetoothTask().execute(address);
        }
    }

    @Override
    public void connectStatusChange(boolean status) {
        if (!status) {
            btnConnect.setText(getString(R.string.not_connect));
        } else {

        }
    }

    @Override
    public void newMessengeFromServer(String messenge) {
        Log.d(TAG, messenge);
        mProcess.doProcess(messenge);
        String date = new Date().toString();
        commandAdapter.addMessengeItem(new MessengeItem(date, MessengeItem.TYPE_IN, messenge));
        rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);
    }

    @Override
    public void onTemperature(int temp) {
        int tempF = Math.convertToF(String.valueOf(temp));
        //set text
        txtTempC.setText(String.valueOf(temp));
        txtTempF.setText(String.valueOf(tempF));

        environmentTask.saveTemp(temp);
    }

    @Override
    public void onHumidityChange(int humi) {
        txtHumi.setText(String.valueOf(humi));
        environmentTask.saveHumi(humi);
    }

    @Override
    public void onLightChange(int valueLight) {
        environmentTask.saveLightValue(valueLight);
    }

    @Override
    public void onGasChange(int valueGas) {
        environmentTask.saveGasValue(valueGas);
    }

    public void setPin(int pin, boolean isOn) {
        mFirebase.setPin(pin, isOn);
    }

    private void syncPin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i < 53; i++) {
                    String cmd = Protocol.GET + Protocol.PIN + i;
                    sendCommand(cmd);
                    SystemClock.sleep(100);
                }
            }
        });
        thread.start();
    }

    /**
     * Created by edoga on 13-Oct-16.
     */
    public class FragmentInfor extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.info_layout, container, false);
            txtLight = (TextView) view.findViewById(R.id.txt_light);
            txtAutoLight = (TextView) view.findViewById(R.id.txt_auto_light);
            txtTempC = (TextView) view.findViewById(R.id.txt_temp_c);
            txtTempF = (TextView) view.findViewById(R.id.txt_temp_f);
            txtHumi = (TextView) view.findViewById(R.id.txt_humi);
            EditText editPhone = (EditText) view.findViewById(R.id.edit_phone);
            String phone = new Preferences(MainActivity.this).getString(Preferences.NUMBER_PHONE);
            editPhone.setText(phone);
            editPhone.addTextChangedListener(mTextWatcher);
            return view;
        }
    }


    class SectionsPagerAdapter extends FragmentPagerAdapter {
        final int mCount = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) return new FragmentEnterPassDoor();
            if (position == 1) return new FragmentCommand();
            if (position == 2) return new FragmentInfor();
            return null;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.door_pass);
                case 1:
                    return getString(R.string.cmd);
                case 2:
                    return getString(R.string.info);
            }
            return null;
        }
    }

    class FragmentCommand extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view5 = inflater.inflate(R.layout.fragment_command, container, false);
            rcCommand = (RecyclerView) view5.findViewById(R.id.rc_conversation);
            rcCommand.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager2.setStackFromEnd(true);
            rcCommand.setLayoutManager(linearLayoutManager2);
            commandAdapter = new CommandAdapter(MainActivity.this, new Database(MainActivity.this));
            rcCommand.setAdapter(commandAdapter);
            rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);

            Button btnSend = (Button) view5.findViewById(R.id.bt_send_messenge);
            editCommand = (EditText) view5.findViewById(R.id.ed_input_messenge);
            commandAdapter.setEditTextCommand(editCommand);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String command = editCommand.getText().toString();
                    if (connector != null) {
                        connector.sendMessenge(command, commandAdapter);
                        editCommand.getText().clear();
                        rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);
                    }
                }
            });
            return view5;
        }
    }


    class ConnectBluetoothTask extends AsyncTask<String, Void, BluetoothSocket> {
        private BluetoothSocket bluetoothSocket;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnConnect.setText(getString(R.string.connecting));
        }

        @Override
        protected BluetoothSocket doInBackground(String... params) {
            Log.d(TAG + " bluetooth", params[0]);
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(params[0]);
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(Variable.myUUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                bluetoothSocket.connect();
                return bluetoothSocket;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            super.onPostExecute(socket);
            if (socket == null) {
                msg(getString(R.string.text_1));
                btnConnect.setText(R.string.not_connect);
            } else {
                if (connector == null) {
                    connector = new Connector(socket);
                    connector.setServerListener(MainActivity.this);
                } else {
                    connector.setSocket(socket);
                    connector.connect();
                }
                msg(getString(R.string.connected));
                btnConnect.setText(R.string.dis_connect);
            }
        }
    }
}
