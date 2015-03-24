package com.unccstudio.gems.gems;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
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
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "";
    private Button Status;
    private ToggleButton red, yellow, green;
    private TextView Result;
    private Handler handler;
    private byte delimiter = 10;
    private boolean stopWorker = false;
    private int readBufferPosition = 0;
    private byte[] readBuffer = new byte[1024];
    private String dataToSend;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino);

        address = getIntent().getStringExtra(MyFridgeActivity.BT_DEVICE_MAC);
        handler = new Handler();

        red = (ToggleButton) findViewById(R.id.redToggleButton);
        yellow = (ToggleButton) findViewById(R.id.yellowToggleButton);
        green = (ToggleButton) findViewById(R.id.greenToggleButton);
        Result = (TextView) findViewById(R.id.statusTextView);
        Status = (Button) findViewById(R.id.statusButton);

        findViewById(R.id.motionImageButton).setBackgroundColor(getResources().getColor(R.color.orange));
        findViewById(R.id.lightImageButton).setBackgroundColor(getResources().getColor(R.color.darkpurple));
        findViewById(R.id.buttonImageButton).setBackgroundColor(getResources().getColor(R.color.blue));

        red.setOnClickListener(this);
        yellow.setOnClickListener(this);
        green.setOnClickListener(this);
        Status.setOnClickListener(this);

        new ConnectBtItem().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            stopWorker = true;
            btSocket.close();
        } catch (IOException e) {
            Intent intent = new Intent();
            intent.putExtra(MyFridgeActivity.ARDUINO_MESSAGE, "Socket creation failed");
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        }

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
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
            case R.id.redToggleButton:
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
            case R.id.yellowToggleButton:
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
            Intent intent = new Intent();
            intent.putExtra(MyFridgeActivity.ARDUINO_MESSAGE, "No Bluetooth connection..");
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
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
            if(btSocket.isConnected()) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Intent intent = new Intent();
                    intent.putExtra(MyFridgeActivity.ARDUINO_MESSAGE, "Unable to end the connection");
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                    Log.d(TAG, "Unable to end the connection");
                }
            }

            Intent intent = new Intent();
            intent.putExtra(MyFridgeActivity.ARDUINO_MESSAGE, "Socket creation failed");
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            Log.d(TAG, "Socket creation failed");
        }

        beginListenForData();
    }

    private void writeData(String data) {
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            Log.d(TAG, "Bug BEFORE Sending stuff", e);
        }

        try {
            outStream.write(data.getBytes());
        } catch (IOException e) {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            Log.d(TAG, "Bug while sending stuff", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            stopWorker = true;
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

    private class ConnectBtItem extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ArduinoActivity.this);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(btSocket.isConnected()){
                Toast.makeText(ArduinoActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            pd.setMessage("Checking bt connection..");
            CheckBt();
            pd.setMessage("Connecting..");
            Connect();

            return null;
        }
    }
}
