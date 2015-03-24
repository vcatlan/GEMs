package com.unccstudio.gems.gems;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_ENABLE_BT = 1001;
    private Button myFridge, tips, game, settings, demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFridge = (Button) findViewById(R.id.myFridgeButton);
        tips = (Button) findViewById(R.id.tipsButton);
        game = (Button) findViewById(R.id.gameButton);
        settings = (Button) findViewById(R.id.settingsButton);
        demo = (Button) findViewById(R.id.demoButton);

        myFridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyFridgeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        // If Bluetooth is not enabled, let user enable it.
        if (!isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //connectToService();
        }

        super.onStart();
    }

    private boolean isBluetoothEnabled() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
            getSupportActionBar().setSubtitle("Bluetooth not supported");
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return false;
            }
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                //connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getSupportActionBar().setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

}
