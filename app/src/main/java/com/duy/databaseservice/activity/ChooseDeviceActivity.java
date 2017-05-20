package com.duy.databaseservice.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.duy.databaseservice.R;
import com.duy.databaseservice.utils.Variable;

import java.util.ArrayList;
import java.util.Set;

public class ChooseDeviceActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayList<String> arrayList;
    private ListView listView;
    private Boolean isBluetoothEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        init();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                isBluetoothEnable = true;
                getData();
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1112);
                isBluetoothEnable = true;
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Không hỗ trợ bluetooth!",
                    Toast.LENGTH_LONG).show();
            this.finish();
        }
        addEvent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1112) {
            if (resultCode == RESULT_OK){
                getData();
            }
        }
    }

    private void addEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = arrayList.get(position);
                s = s.substring(s.length() - 17);
                Intent intent = getIntent();
                intent.putExtra("data", s);
                setResult(Variable.REQUEST_CODE_CHOOSE_DEVICE, intent);
                finish();
            }
        });
    }

    private void getData() {
        if (isBluetoothEnable) {
            arrayList = new ArrayList<>();
            Set<BluetoothDevice> bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
            if (bluetoothDeviceSet.size() > 0) {
                for (BluetoothDevice device : bluetoothDeviceSet) {
                    String s = device.getName() + " - " + device.getAddress();
                    arrayList.add(s);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Chưa có thiết bị được ghép nối ", Toast.LENGTH_LONG).show();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
        }
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
    }
}
