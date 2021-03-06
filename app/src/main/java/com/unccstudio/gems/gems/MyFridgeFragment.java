package com.unccstudio.gems.gems;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFridgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFridgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFridgeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    public static final String BT_DEVICE_MAC = "bt_device_mac";
    private List<String> items;
    private ArrayAdapter<String> adapter;
    private BluetoothAdapter mBlueAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    private int mParam1;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyFridgeFragment.
     */

    public static MyFridgeFragment newInstance(int param1) {
        MyFridgeFragment fragment = new MyFridgeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyFridgeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

        items = new ArrayList<>();
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_my_fridge, container, false);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBlueAdapter == null) {
            // Device does not support Bluetooth
        }

        //Query Paired Devices
        pairedDevices = mBlueAdapter.getBondedDevices();
        // If there are paired devices
        items.add("Paired devices...");
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if(device.getName().equals("HC-06")) {
                    items.add(device.getAddress());
                }
            }
        }

//        items.add("Available devices...");

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) rootView.findViewById(R.id.myFridgeListView);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArduinoActivity.class);
                intent.putExtra(BT_DEVICE_MAC, items.get(position));
                startActivity(intent);
                Toast.makeText(getActivity(), items.get(position), Toast.LENGTH_SHORT).show();
            }
        });

//        // Register the BroadcastReceiver
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        return rootView;
    }

    @Override
    public void onStart() {
        //start searching for bt devices
//        if (mBlueAdapter.isDiscovering()) {
//            mBlueAdapter.cancelDiscovery();
//            Toast.makeText(getActivity(), "Discovery canceled...", Toast.LENGTH_SHORT).show();
//        }
//
//        Toast.makeText(getActivity(), "Starting discovery...", Toast.LENGTH_SHORT).show();
//        mBlueAdapter.startDiscovery();

        super.onStart();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if(device.getName() != null) {
                    if (device.getName().equals("HC-06")) {
                        items.add(device.getName() + "\n" + device.getAddress());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        getActivity().unregisterReceiver(mReceiver);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
