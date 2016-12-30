package com.duy.databaseservice.items;

import java.io.Serializable;

/**
 * Created by tranleduy on 24-May-16.
 */
public class ModeItem implements Serializable {    public static final long serialVersionUID = 2L;

    private int id;
    private String name;
    private String command;
    private String des;
    private boolean on = false;

    public ModeItem() {
    }

    public ModeItem(String name, String command, String des, int id) {
        this.name = name;
        this.command = command;
        this.des = des;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.name;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
