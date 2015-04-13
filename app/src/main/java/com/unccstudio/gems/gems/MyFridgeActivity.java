package com.unccstudio.gems.gems;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


public class MyFridgeActivity extends ActionBarActivity {
    public static final String GEM_ITEM = "gem_item";
    public static final String GEM_ITEM_POSITION = "gem_item_position";
    public static final String ARDUINO_MESSAGE = "arduino_message";
    private static final int ARDUINO_ACTIVITY = 1002;

    private MyFridgeDisplayItemAdapter adapter;
    private ArrayList<GEM> items;
    private BluetoothAdapter mBlueAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private ListView itemListView;
    private String[] itemNames = new String[3];
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fridge);

        counter = 0;
        itemNames[0] = "Milk";
        itemNames[1] = "Tomato";
        itemNames[2] = "Leftover";

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
                if (device.getName().equals("HC-06")) {
                    GEM item = new GEM();

                    item.setMacAddress(device.getAddress());
                    item.setName(itemNames[counter]);
                    item.setAge(2+counter);
                    item.setAttached(true);
                    item.setShelfLife(5+counter);
                    item.setShelfLifeType("days");
                    item.setQuantity(1);
                    counter++;

                    items.add(item);
                }
            }
        }

        adapter = new MyFridgeDisplayItemAdapter(this, R.layout.display_myfridgeitem_layout, items);
        itemListView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyFridgeActivity.this, ArduinoActivity.class);
                intent.putExtra(GEM_ITEM, items.get(position));
                intent.putExtra(GEM_ITEM_POSITION, position);
                startActivityForResult(intent, ARDUINO_ACTIVITY);
            }
        });

//        // Register the BroadcastReceiver
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

//    // Create a BroadcastReceiver for ACTION_FOUND
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            // When discovery finds a device
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                if(device.getName() != null) {
//                    if (device.getName().equals("HC-06")) {
//                        GEM item = new GEM();
//
//                        item.setMacAddress(device.getAddress());
//                        item.setName(device.getName() + " -> unpaired");
//
//                        items.add(item);
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        }
//    };

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        //start searching for bt devices
//        if (mBlueAdapter.isDiscovering()) {
//            mBlueAdapter.cancelDiscovery();
//            items = new ArrayList<>();
//        }
//
//        Toast.makeText(this, "Starting discovery...", Toast.LENGTH_SHORT).show();
//        mBlueAdapter.startDiscovery();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ARDUINO_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK) {
                items.set(data.getIntExtra(GEM_ITEM_POSITION, 0), (GEM) data.getSerializableExtra(GEM_ITEM));
                adapter.notifyDataSetChanged();
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

//        //start searching for bt devices
//        if (mBlueAdapter.isDiscovering()) {
//            mBlueAdapter.cancelDiscovery();
//            items = new ArrayList<>();
//        }

        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        //start searching for bt devices
//        if (mBlueAdapter.isDiscovering()) {
//            mBlueAdapter.cancelDiscovery();
//        }
//
//        unregisterReceiver(mReceiver);
    }
}
