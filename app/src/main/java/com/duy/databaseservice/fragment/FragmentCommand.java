package com.duy.databaseservice.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.databaseservice.CommandAdapter;
import com.duy.databaseservice.Connector;
import com.duy.databaseservice.EventListener;
import com.duy.databaseservice.FirebaseListener;
import com.duy.databaseservice.ProcessCommandArduino;
import com.duy.databaseservice.R;
import com.duy.databaseservice.activity.ChooseDeviceActivity;
import com.duy.databaseservice.data.Database;
import com.duy.databaseservice.data.Preferences;
import com.duy.databaseservice.items.DeviceItem;
import com.duy.databaseservice.items.MessengeItem;
import com.duy.databaseservice.task.EnvironmentListener;
import com.duy.databaseservice.utils.Variable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * Hiển thị các lệnh nhận được từ Arduino và gửi đi các lệnh
 * từ người dùng.
 */
public class FragmentCommand extends Fragment implements Connector.OnServerListener,
        EventListener, EnvironmentListener {

    private static final String TAG = "FragmentCommand";
    private Button btnConnect;
    @Nullable
    private Connector connector = null;
    private RecyclerView rcCommand;
    private CommandAdapter commandAdapter;
    private EditText editCommand;
    private ProcessCommandArduino mProcess;
    private FirebaseListener firebaseListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_command, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcCommand = (RecyclerView) view.findViewById(R.id.rc_conversation);
        rcCommand.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setStackFromEnd(true);
        rcCommand.setLayoutManager(linearLayoutManager2);
        commandAdapter = new CommandAdapter(getContext(), new Database(getContext()));
        rcCommand.setAdapter(commandAdapter);
        rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);

        Button btnSend = (Button) view.findViewById(R.id.bt_send_messenge);
        editCommand = (EditText) view.findViewById(R.id.ed_input_messenge);
        commandAdapter.setEditTextCommand(editCommand);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = editCommand.getText().toString();
                if (connector != null) {
                    connector.sendMessage(command, commandAdapter);
                    editCommand.getText().clear();
                    rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);
                }
            }
        });

        btnConnect = (Button) getActivity().findViewById(R.id.btnConnect);
        firebaseListener = new FirebaseListener(getContext());
        mProcess = new ProcessCommandArduino(this, firebaseListener, getContext());

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connector != null) {
                    if (!connector.isConnect()) {
                        Intent intent = new Intent(getActivity(), ChooseDeviceActivity.class);
                        startActivityForResult(intent, Variable.REQUEST_CODE_CHOOSE_DEVICE);
                    } else {
                        connector.disconnect();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), ChooseDeviceActivity.class);
                    startActivityForResult(intent, Variable.REQUEST_CODE_CHOOSE_DEVICE);
                }
            }
        });

        tryConnect();
    }

    private void tryConnect() {

        if (!Preferences.getString(getContext(), Preferences.ID_DEVICE_BLUE).trim().isEmpty()) {
            new ConnectBluetoothTask(btnConnect)
                    .execute(Preferences.getString(getContext(), Preferences.ID_DEVICE_BLUE));
        }
    }

    @Override
    public void onConnectChangeStatus(boolean isConnect) {
        if (!isConnect) {
            btnConnect.setText(getString(R.string.not_connect));
        } else {

        }
    }

    @Override
    public void newMessageFromServer(String message) {
        Log.d(TAG, message);
        mProcess.doProcess(message);
        String date = new Date().toString();
        commandAdapter.addMessengeItem(new MessengeItem(date, MessengeItem.TYPE_IN, message));
        rcCommand.scrollToPosition(commandAdapter.getItemCount() - 1);
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
        if (connector != null) {
            connector.sendMessage(cmd, commandAdapter);
        } else {
            Log.d(TAG, "sendCommand: socket is null");
        }
    }

    public void msg(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTemperature(int temp) {

    }

    @Override
    public void onHumidityChange(int humi) {

    }

    @Override
    public void onLightChange(int valueLight) {

    }

    @Override
    public void onGasChange(int valueGas) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode +
                "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (resultCode == Variable.REQUEST_CODE_CHOOSE_DEVICE) {
            String address = data.getStringExtra("data");
            msg(address);
            Preferences.putString(getContext(), Preferences.ID_DEVICE_BLUE, address);
            new ConnectBluetoothTask(btnConnect).execute(address);
        }
    }

    private class ConnectBluetoothTask extends AsyncTask<String, Void, BluetoothSocket> {
        private BluetoothSocket bluetoothSocket;
        private Button btnConnect;

        public ConnectBluetoothTask(Button btnConnect) {

            this.btnConnect = btnConnect;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnConnect.setText(getString(R.string.connecting));
        }

        @Override
        protected BluetoothSocket doInBackground(String... params) {
            Log.d(TAG, "doInBackground() called with: params = [" + Arrays.toString(params) + "]");
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
                    connector.setOnServerListener(FragmentCommand.this);
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
