package com.winxo.portailwinxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.winxo.portailwinxo.Model.Claim;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.View.ClaimDetailActivity;

import java.util.ArrayList;

public class ClaimListAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Claim> claimList;
    private final LayoutInflater layoutInflater;
    private SessionManager sessionManager;

    public ClaimListAdapter(Context context, SessionManager sessionManager, ArrayList<Claim> claimList) {
        this.context = context;
        this.claimList = claimList;
        layoutInflater = LayoutInflater.from(context);
        this.sessionManager = sessionManager;
    }

    @Override
    public int getCount() {
        return claimList.size();
    }

    @Override
    public Object getItem(int i) {
        return claimList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ResourceAsColor", "SetTextI18n"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Claim claim = claimList.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.claim_row, null);
            holder = new ViewHolder();
            holder.sitename = (TextView) convertView.findViewById(R.id.sitename);
            holder.ref = (TextView) convertView.findViewById(R.id.ref);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.date_add_value = (TextView) convertView.findViewById(R.id.date_add_value);
            holder.heure_add_value = (TextView) convertView.findViewById(R.id.heure_add_value);
            holder.claim_detail = (TextView) convertView.findViewById(R.id.claim_detail);
           /* holder.status_bloc_value = (LinearLayout) convertView.findViewById(R.id.status_bloc_value);
            holder.status_ico = (ImageView) convertView.findViewById(R.id.status_ico);
            holder.status_value = (TextView) convertView.findViewById(R.id.status_value);*/
            holder.more_btn = (ImageButton) convertView.findViewById(R.id.more_btn);
            holder.date_heure_add_title = (TextView) convertView.findViewById(R.id.date_heure_add_title);
            holder.claim_detail_title = (TextView) convertView.findViewById(R.id.claim_detail_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*switch (claim.getStatus()) {
            case "0":
                holder.status_ico.setImageResource(R.drawable.ic_pencil_alt);
                holder.status_bloc_value.setBackgroundColor(R.color.primary);
                holder.status_value.setText("Saisi");
                break;
            case "1":
                holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                holder.status_bloc_value.setBackgroundColor(R.color.colorYellow);
                holder.status_value.setTextColor(R.color.colorBlack);
                holder.status_value.setText("Attribué");
                break;
            case "2":
                holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                holder.status_bloc_value.setBackgroundColor(R.color.success);
                holder.status_value.setText("Traité");
                break;
        }*/
        holder.sitename.setText(claim.getId_site());
        holder.ref.setText(String.format(holder.ref.getText().toString(), claim.getRef()));
        holder.number.setText(String.format(holder.number.getText().toString(), claim.getId_claim()));
        holder.date_add_value.setText(claim.getDate_claim());
        holder.heure_add_value.setText(claim.getTime_claim());

        Typeface poppins_bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
        holder.date_heure_add_title.setTypeface(poppins_bold);
        holder.claim_detail_title.setTypeface(poppins_bold);

        String detail = claim.getDescription();
        if (detail.length() > 50)
            holder.claim_detail.setText(claim.getDescription().substring(0, 50) + "...");
        else
            holder.claim_detail.setText(claim.getDescription());

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClaimDetailActivity.class);
                intent.putExtra("id_claim", claim.getId_claim());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView sitename, ref, number, date_add_value, heure_add_value, claim_detail, status_value, date_heure_add_title, claim_detail_title;
        LinearLayout status_bloc_value;
        ImageView status_ico;
        ImageButton more_btn;
    }
}