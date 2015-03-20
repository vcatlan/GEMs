package com.unccstudio.gems.gems;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ArduinoActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "Jon";
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "";
    Button Status;
    ToggleButton red, yellow, green;
    TextView Result;
    Handler handler;
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];
    private String dataToSend;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        address = getIntent().getStringExtra(MyFridgeFragment.BT_DEVICE_MAC);
        //display logo in the action bar
        ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        handler = new Handler();

        //Connect = (Button) findViewById(R.id.button);
        red = (ToggleButton) findViewById(R.id.redToggleButton);
        yellow = (ToggleButton) findViewById(R.id.yellowToggleButton);
        green = (ToggleButton) findViewById(R.id.greenToggleButton);
        Result = (TextView) findViewById(R.id.statusTextView);
        Status = (Button) findViewById(R.id.statusButton);

        findViewById(R.id.motionImageButton).setBackgroundColor(getResources().getColor(R.color.orange));
        findViewById(R.id.lightImageButton).setBackgroundColor(getResources().getColor(R.color.darkpurple));
        findViewById(R.id.buttonImageButton).setBackgroundColor(getResources().getColor(R.color.blue));

        //Connect.setOnClickListener(this);
        red.setOnClickListener(this);
        yellow.setOnClickListener(this);
        green.setOnClickListener(this);
        Status.setOnClickListener(this);

        CheckBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Jon", device.toString());

        Connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public void onClick(View control) {

        switch (control.getId()) {
            case R.id.statusButton:
                dataToSend = "CMD STATUS";
                dataToSend += "\n";
                writeData(dataToSend);
                break;
            case R.id.yellowToggleButton:
                if (red.isChecked()) {
                    dataToSend = "CMD RED=ON";
                    dataToSend += "\n";
                    writeData(dataToSend);
                } else if (!red.isChecked()) {
                    dataToSend = "CMD RED=OFF";
                    dataToSend += "\n";
                    writeData(dataToSend);
                }
                break;
            case R.id.redToggleButton:
                if (yellow.isChecked()) {
                    dataToSend = "CMD YELLOW=ON";
                    dataToSend += "\n";
                    writeData(dataToSend);
                } else if (!yellow.isChecked()) {
                    dataToSend = "CMD YELLOW=OFF";
                    dataToSend += "\n";
                    writeData(dataToSend);
                }
                break;
            case R.id.greenToggleButton:
                if (green.isChecked()) {
                    dataToSend = "CMD GREEN=ON";
                    dataToSend += "\n";
                    writeData(dataToSend);
                } else if (!green.isChecked()) {
                    dataToSend = "CMD GREEN=OFF";
                    dataToSend += "\n";
                    writeData(dataToSend);
                }
                break;
        }
    }

    private void CheckBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
                    Toast.LENGTH_SHORT).show();
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth null !", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void Connect() {
        Log.d(TAG, address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.d(TAG, "Connecting to ... " + device);
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            Log.d(TAG, "Connection made.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "Unable to end the connection");
            }
            Log.d(TAG, "Socket creation failed");
        }

        beginListenForData();
    }

    private void writeData(String data) {
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "Bug BEFORE Sending stuff", e);
        }

        try {
            outStream.write(data.getBytes());
        } catch (IOException e) {
            Log.d(TAG, "Bug while sending stuff", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            btSocket.close();
        } catch (IOException e) {

        }
    }

    public void beginListenForData() {
        try {
            inStream = btSocket.getInputStream();
        } catch (IOException e) {
        }

        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = inStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        public void run() {

//                                        if (Result.getText().toString().equals("..")) {
//                                            Result.setText(data);
//                                        } else {
//                                            Result.append("\n" + data);
//                                        }
                                        Result.append(data + "\n");
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }
}
