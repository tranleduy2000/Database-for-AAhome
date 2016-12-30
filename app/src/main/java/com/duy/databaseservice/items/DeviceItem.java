package com.duy.databaseservice.items;

import java.io.Serializable;

/**
 * Created by tranleduy on 22-May-16.
 */
public class DeviceItem implements Serializable {
    public static final long serialVersionUID = 2L;
    private static final int TYPE_SYSTEM = 1; //không thể xóa
    private static final int TYPE_USER = 0;
    private int type;
    private long dateCreate;

    //dành cho thiết bị đèn
    private boolean isLight = false;
    private int valueLight = 0;
    private boolean isEnable = true;

    private String name = "", des = "";
    private boolean on = false;
    private String command = "";
    private int id;
    private int img = -1;

    public DeviceItem() {

    }

    public DeviceItem(String name, String des, String command, int id, int img) {
        this.name = name;
        this.des = des;
        this.command = command;
        this.id = id;
        this.img = img;
    }

    public DeviceItem(String name, String des, String command, int id, boolean on, int img) {
        this(name, des, command, id, img);

        this.on = on;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
