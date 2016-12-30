package com.duy.databaseservice;


import com.duy.databaseservice.items.DeviceItem;

public interface EventListener {


    void deviceChangeInfo(int pos, DeviceItem deviceItem);

    void deviceDelete(int pos, DeviceItem deviceItem);

    void onDeviceClick(DeviceItem deviceItem);

    void sendCommand(String cmd);


}