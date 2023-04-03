package com.winxo.portailwinxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winxo.portailwinxo.Model.Station;
import com.winxo.portailwinxo.R;

import java.util.ArrayList;

public class StationListAdapter extends BaseAdapter {

    private final ArrayList<Station> stationList;
    private final LayoutInflater layoutInflater;

    public StationListAdapter(Context context, ArrayList<Station> stationList) {
        this.stationList = stationList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stationList.size();
    }

    @Override
    public Object getItem(int i) {
        return stationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.station_row, null);
            holder = new ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.station_value = (TextView) convertView.findViewById(R.id.station_value);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.ville = (TextView) convertView.findViewById(R.id.ville);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Station station = this.stationList.get(position);
        holder.number.setText(station.getCodestation());
        holder.station_value.setText(station.getNamefr());
        holder.distance.setText(station.getDistance());
        holder.ville.setText(station.getVille());
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView station_value;
        TextView distance;
        TextView ville;
    }
}