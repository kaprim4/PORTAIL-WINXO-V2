package com.winxo.portailwinxo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.winxo.portailwinxo.Model.Price;
import com.winxo.portailwinxo.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.PriceViewHolder> {

    private final Context context;
    private final ArrayList<Price> priceList;
    private final RecyclerItemClickListener listener;
    private int v_Type = 0;
    public static final int GRID_VIEW = 0;
    public static final int LIST_VIEW = 1;

    public PriceAdapter(Context context, int v_Type, ArrayList<Price> priceList, RecyclerItemClickListener listener) {
        this.context = context;
        this.v_Type = v_Type;
        this.priceList = priceList;
        this.listener = listener;
    }

    public interface RecyclerItemClickListener {
        void onClickListener(Price video, int position);
    }

    public void setViewType(int v_Type) {
        this.v_Type = v_Type;
    }

    @NotNull
    @Override
    public PriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_row, parent, false);
        return new PriceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull final PriceViewHolder holder, int position) {
        Price price = priceList.get(position);
        String status_txt = "";

        if (price != null) {

            switch (price.getTreated()) {
                case "0":
                    status_txt = "Saisi";
                    holder.status_ico.setImageResource(R.drawable.ic_pencil_alt);
                    holder.status_bloc_value.setBackgroundResource(R.color.primary);
                    break;
                case "1":
                    status_txt = "Traité";
                    holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                    holder.status_bloc_value.setBackgroundResource(R.color.success);
                    break;
                case "2":
                    status_txt = "Annulé";
                    holder.status_ico.setImageResource(R.drawable.ic_ban);
                    holder.status_bloc_value.setBackgroundResource(R.color.warning);
                    break;
                case "3":
                    status_txt = "Envoyé";
                    holder.status_ico.setImageResource(R.drawable.ic_paper_plane);
                    holder.status_bloc_value.setBackgroundResource(R.color.primary);
                    break;
                default:
                    status_txt = "Appliqué";
                    holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                    holder.status_bloc_value.setBackgroundResource(R.color.success);
                    break;
            }
            holder.number.setText(price.getId_proposal());
            holder.date_application_value.setText(price.getApplication_date());
            holder.heure_application_value.setText(price.getApplication_time());
            holder.status_value.setText(status_txt);
            holder.bind(price, listener);
        }
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public static class PriceViewHolder extends RecyclerView.ViewHolder {
        public TextView number, date_application_value, heure_application_value, status_value;
        public LinearLayout status_bloc_value;
        public RelativeLayout status_bloc;
        public ImageView status_ico;

        public PriceViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            date_application_value = itemView.findViewById(R.id.date_application_value);
            heure_application_value = itemView.findViewById(R.id.heure_application_value);
            status_bloc = itemView.findViewById(R.id.status_bloc);
            status_bloc_value = itemView.findViewById(R.id.status_bloc_value);
            status_ico = itemView.findViewById(R.id.status_ico);
            status_value = itemView.findViewById(R.id.status_value);
        }

        public void bind(final Price price, final RecyclerItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickListener(price, getLayoutPosition());
                }
            });
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return false;
        }
    }
}