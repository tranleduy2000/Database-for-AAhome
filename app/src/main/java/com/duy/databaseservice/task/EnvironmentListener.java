package com.duy.databaseservice.task;

/**
 * Created by edoga on 15-Oct-16.
 */

public interface EnvironmentListener {

    void onTemperature(int temp);

    void onHumidityChange(int humi);

    void onLightChange(int valueLight);

    void onGasChange(int valueGas);
}
