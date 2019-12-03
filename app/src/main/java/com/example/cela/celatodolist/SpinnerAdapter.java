package com.example.cela.celatodolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class SpinnerAdapter extends BaseAdapter {
    // 顏色選取
    ArrayList<ColorData> colorlist;
    LayoutInflater inflater;
    Context context;
    public SpinnerAdapter(@NonNull Context context, ArrayList<ColorData> colorlist) {
        this.colorlist = colorlist;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return colorlist.size();
    }

    @Override
    public Object getItem(int position) {
        return colorlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return colorlist.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColorData item = (ColorData) getItem(position);
        convertView = inflater.inflate(R.layout.color_view,null);
        ImageView ticket = convertView.findViewById(R.id.ticket);
        TextView color_name = convertView.findViewById(R.id.color_name);
        ticket.setBackgroundColor(Color.parseColor(item.code));
        color_name.setText(item.name);

        return convertView;
    }
}
