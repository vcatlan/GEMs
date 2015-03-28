package com.unccstudio.gems.gems;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MyFridgeActivity extends ActionBarActivity {
    public static final String BT_DEVICE_MAC = "bt_device_mac";
    public static final String ARDUINO_MESSAGE = "arduino_message";
    private static final int ARDUINO_ACTIVITY = 1002;

    private MyFridgeDisplayItemAdapter adapter;
    private ArrayList<GEM> items;
    private BluetoothAdapter mBlueAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);

        items = new ArrayList<>();
        itemListView = (ListView) findViewById(R.id.myFridgeListView);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter == null) {
            // Device does not support Bluetooth
        }

        //Query Paired Devices
        pairedDevices = mBlueAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if(device.getName().equals("HC-06")) {
                    GEM item = new GEM();

                    item.setMacAddress(device.getAddress());
                    item.setName(device.getName());

                    items.add(item);
                }
            }
        }

        adapter = new MyFridgeDisplayItemAdapter(this, R.layout.display_myfridgeitem_layout, items);
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFridgeActivity.this, ArduinoActivity.class);
                intent.putExtra(BT_DEVICE_MAC, items.get(position).getMacAddress());
                startActivityForResult(intent, ARDUINO_ACTIVITY);
            }
        });

        adapter.setNotifyOnChange(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ARDUINO_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if(data != null) {
                    if (data.getStringExtra(ARDUINO_MESSAGE) != null) {
                        Toast.makeText(MyFridgeActivity.this, data.getStringExtra(ARDUINO_MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyFridgeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
