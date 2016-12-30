package com.duy.databaseservice;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.duy.databaseservice.items.MessengeItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Giao tiep bluetooth
 * Created by Duy on 19/7/2016
 */
public class Connector implements Serializable {
    private static final String TAG = Connector.class.getName();
    public BluetoothSocket socket = null;
    public ServerListener serverListener = null;
    private OutputStream out;
    private AtomicBoolean atomicBoolean = new AtomicBoolean(true);
    private String lastCommand = "";

    public Connector(BluetoothSocket socket) {
        this.socket = socket;
        try {
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect();
    }

    public boolean isConnect() {
        if (socket == null) return false;
        return socket.isConnected();
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    public void connect() {
        new ServerListenner().execute();
        if (serverListener != null) serverListener.connectStatusChange(true);

    }

    public void disconnect() {
        try {
            atomicBoolean.set(false);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serverListener != null) serverListener.connectStatusChange(false);
    }

    public void sendMessenge(String messenge, CommandAdapter messengeListAdapter) {
        if (true) {
            try {
                messenge += "\n";
                if (!messenge.equals(lastCommand)) {
                    byte[] msgBuffer = messenge.getBytes();           //converts entered String into bytes
                    try {
                        out.write(msgBuffer);                //write bytes over BT connection via outstream
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lastCommand = messenge;
                    String date = new Date().toString();
                    messengeListAdapter.addMessengeItem(new MessengeItem(date, MessengeItem.TYPE_OUT, messenge, false));
                    Log.e("Send command", messenge);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (serverListener != null) serverListener.connectStatusChange(false);
            String date = new Date().toString();
            messengeListAdapter.addMessengeItem(new MessengeItem(date, MessengeItem.TYPE_OUT, messenge, false));
            Log.e("Send Failed", messenge);
        }
    }

    public void sendMessenge(String messenge, CommandAdapter messengeListAdapter,
                             boolean debug, boolean isConnected) {
        if (!debug) {
            sendMessenge(messenge, messengeListAdapter);
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        String currentDateandTime = sdf.format(new Date());
        messengeListAdapter.addMessengeItem(new MessengeItem(currentDateandTime, MessengeItem.TYPE_OUT, messenge));
        if (!isConnected) return;
        if (socket.isConnected()) {
            try {
                messenge += "\n";
                byte[] msgBuffer = messenge.getBytes();           //converts entered String into bytes
                try {
                    out.write(msgBuffer);                //write bytes over BT connection via outstream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (serverListener != null) serverListener.connectStatusChange(false);
        }
    }

    public void sendMessenge(String messenge) {
        if (socket.isConnected()) {
            try {
                messenge += "\n";
                if (!messenge.equals(lastCommand)) {
                    byte[] msgBuffer = messenge.getBytes();           //converts entered String into bytes
                    try {
                        out.write(msgBuffer);                //write bytes over BT connection via outstream
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lastCommand = messenge;
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (serverListener != null) serverListener.connectStatusChange(false);
        }
    }

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public interface ServerListener {
        void connectStatusChange(boolean status);

        void newMessengeFromServer(String messenge);
    }

    class ServerListenner extends AsyncTask<Void, String, Void> {
        private InputStream inputStream;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            atomicBoolean.set(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            String msg = "";
            try {
                Log.d(TAG, "doInBackground");
                inputStream = socket.getInputStream();
                while (true) {
                    try {
                        bytes = inputStream.read(buffer);            //read bytes from input buffer
                        String readMessage = new String(buffer, 0, bytes);
                        char[] c = readMessage.toCharArray();
                        for (int i = 0; i < c.length; i++) {
                            if (c[i] == '\n') {
                                publishProgress(msg);
                                msg = "";
                            } else {
                                msg += Character.toString(c[i]);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            try {
                inputStream.close();
                socket.close();
                serverListener.connectStatusChange(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Log.e(TAG, values[0]);
            if (serverListener != null) serverListener.newMessengeFromServer(values[0]);
        }
    }
}
