package com.duy.databaseservice.items;

/**
 * Item for command com.example.tranleduy.aahome.com.duy.aahomeinternet.adapter
 * <p/>
 * Created by duy on 2/14/2016.
 */
public class MessengeItem {
    public static final long serialVersionUID = 2L;
    public static final int TYPE_IN = 0;
    public static final int TYPE_OUT = 1;
    private int type;
    private String body;
    private String date = "";
    private boolean status = true;

    public MessengeItem() {
    }

    public MessengeItem(String date, int type, String body) {
        this.type = type;
        this.date = date;
        this.body = body;
    }

    public MessengeItem(String date, int type, String body, boolean status) {
        this.type = type;
        this.date = date;
        this.body = body;
        this.status = status;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
