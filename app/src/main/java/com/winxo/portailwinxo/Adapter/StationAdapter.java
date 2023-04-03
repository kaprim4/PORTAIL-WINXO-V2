package com.winxo.portailwinxo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.winxo.portailwinxo.Model.Station;
import com.winxo.portailwinxo.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private final Context context;
    private final ArrayList<Station> stationList;
    private final RecyclerItemClickListener listener;
    private int v_Type = 0;
    public static final int GRID_VIEW = 0;
    public static final int LIST_VIEW = 1;

    public StationAdapter(Context context, int v_Type, ArrayList<Station> stationList, RecyclerItemClickListener listener) {
        this.context = context;
        this.v_Type = v_Type;
        this.stationList = stationList;
        this.listener = listener;
    }

    public interface RecyclerItemClickListener {
        void onClickListener(Station station, int position);
    }

    public void setViewType(int v_Type) {
        this.v_Type = v_Type;
    }

    @NotNull
    @Override
    public StationViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = null;
        switch (v_Type) {
            case GRID_VIEW:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_row, parent, false);
                break;
            case LIST_VIEW:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_row, parent, false);
                break;
        }
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final StationViewHolder holder, int position) {
        Station station = stationList.get(position);
        if (station != null) {
            holder.number.setText(station.getCodestation());
            holder.station_value.setText(station.getNamefr());
            holder.distance.setText(station.getDistance());
            holder.ville.setText(station.getVille());
            holder.bind(station, listener);
        }
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {
        public TextView number, station_value, distance, ville;

        public StationViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            station_value = itemView.findViewById(R.id.station_value);
            distance = itemView.findViewById(R.id.distance);
            ville = itemView.findViewById(R.id.ville);
        }

        public void bind(final Station station, final RecyclerItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickListener(station, getLayoutPosition());
                }
            });
        }
    }

    /*** Showing popup menu when tapping on 3 dots */
    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.menu_video, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /*** Click listener for popup menu items */
    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            /*switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(context, "@string/action_add_favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(context, "@string/action_play_next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }*/
            return false;
        }
    }
}