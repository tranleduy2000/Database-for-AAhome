package com.duy.databaseservice.items;

/**
 * Created by edoga on 15-Oct-16.
 */

public class ItemTemperature {
    int temp;
    long date;

    public ItemTemperature(int temp, long date) {
        this.temp = temp;
        this.date = date;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

}
