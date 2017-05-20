package com.duy.databaseservice.utils;

import com.duy.databaseservice.R;

import java.util.UUID;

/**
 * Variable
 * Created by Duy on 01-Jun-16.
 */
public class Variable {
    public static final int[] IMG_DEVICE = {
            R.drawable.ic_air_conditioner,
            R.drawable.ic_boiler,
            R.drawable.ic_cleaner,
            R.drawable.ic_lamp,
            R.drawable.ic_light_bulb,
            R.drawable.ic_light_bulb_1,
            R.drawable.ic_light_bulb_2,
            R.drawable.ic_microwave,
            R.drawable.ic_television,
            R.drawable.ic_washing_machine,
            R.drawable.ic_window
    };

    public static final int ACTIVITY_DEVICE = 1212;
    public static final int ACTIVITY_ADD_DEVICE = 1215;
    public static final int ACTIVITY_CHANGE_INFO_DEVICE = 1216;
    public static final int ACTIVITY_ADD_MODE = 1214;
    public static final int REQUEST_CODE_CHOOSE_DEVICE = 1001;
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

}
