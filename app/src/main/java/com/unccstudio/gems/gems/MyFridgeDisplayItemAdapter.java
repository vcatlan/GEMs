package com.unccstudio.gems.gems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 3/24/2015.
 */
public class MyFridgeDisplayItemAdapter extends ArrayAdapter<GEM> {
    private List<GEM> mData;
    private Context mContext;
    private int mResource;

    public MyFridgeDisplayItemAdapter(Context context, int resource, List<GEM> objects) {
        super(context, resource, objects);
        mData = objects;
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        GEM item = mData.get(position);

        ImageView itemShelfStatus = (ImageView) convertView.findViewById(R.id.itemColorImageView);
        TextView itemName = (TextView) convertView.findViewById(R.id.itemNameTextView);
        TextView itemTime = (TextView) convertView.findViewById(R.id.itemTimeTextView);
        TextView itemMacAddress = (TextView) convertView.findViewById(R.id.itemMacAddressTextView);

        switch (item.getColor()){
            case "GREEN":

                break;

            case "YELLOW":

                break;

            case "ORANGE":

                break;

            case "RED":

                break;
        }

        itemName.setText(item.getName());
        itemTime.setText(Integer.toString(item.getAge()));
        itemMacAddress.setText("MAC: " + item.getMacAddress());

        return convertView;
    }
}
