package com.winxo.portailwinxo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.winxo.portailwinxo.Model.Price;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Util;

import java.util.ArrayList;

public class PriceListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private ArrayList<Price> priceList;
    private final LayoutInflater layoutInflater;
    public ArrayList<Price> orig;
    static int id = 1;
    private final ArrayList<Integer> PriceRowArray = new ArrayList<Integer>();
    public int count_list = 0;

    public PriceListAdapter(Context context, ArrayList<Price> priceList) {
        this.context = context;
        this.priceList = priceList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return priceList.size();
    }

    public int getListCount() {
        return count_list;
    }

    @Override
    public Object getItem(int i) {
        return priceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Price> results = new ArrayList<Price>();
                if (orig == null)
                    orig = priceList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Price g : orig) {
                            if (g.getSiteName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                count_list = oReturn.count;
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                priceList = (ArrayList<Price>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Price price = priceList.get(position);
        String status_txt = "";
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.price_row, null);
            holder = new ViewHolder();
            holder.sitename = (TextView) convertView.findViewById(R.id.sitename);
            holder.superviseur_name = (TextView) convertView.findViewById(R.id.superviseur_name);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.date_add_value = (TextView) convertView.findViewById(R.id.date_add_value);
            holder.heure_add_value = (TextView) convertView.findViewById(R.id.heure_add_value);
            holder.date_application_value = (TextView) convertView.findViewById(R.id.date_application_value);
            holder.heure_application_value = (TextView) convertView.findViewById(R.id.heure_application_value);
            holder.status_bloc = (LinearLayout) convertView.findViewById(R.id.status_bloc);
            holder.status_bloc_value = (LinearLayout) convertView.findViewById(R.id.status_bloc_value);
            holder.status_ico = (ImageView) convertView.findViewById(R.id.status_ico);
            holder.status_value = (TextView) convertView.findViewById(R.id.status_value);
            holder.price_bloc = (LinearLayout) convertView.findViewById(R.id.price_bloc);
            holder.date_add_title = (TextView) convertView.findViewById(R.id.date_add_title);
            holder.date_application_title = (TextView) convertView.findViewById(R.id.date_application_title);
            holder.status_title = (TextView) convertView.findViewById(R.id.status_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface poppins_bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
        holder.superviseur_name.setTypeface(poppins_bold);
        holder.date_add_title.setTypeface(poppins_bold);
        holder.date_application_title.setTypeface(poppins_bold);
        holder.status_title.setTypeface(poppins_bold);

        holder.status_bloc_value.setBackground(context.getResources().getDrawable(R.drawable.round_button));
        switch (price.getTreated()) {
            case "0":
                status_txt = "Saisi";
                holder.status_ico.setImageResource(R.drawable.ic_pencil_alt);
                holder.status_bloc_value.setBackgroundColor(context.getResources().getColor(R.color.primary));
                break;
            case "1":
                status_txt = "Traité";
                holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                holder.status_bloc_value.setBackgroundColor(context.getResources().getColor(R.color.success));
                break;
            case "2":
                status_txt = "Annulé";
                holder.status_ico.setImageResource(R.drawable.ic_ban);
                holder.status_bloc_value.setBackgroundColor(context.getResources().getColor(R.color.warning));
                break;
            case "3":
                status_txt = "Envoyé";
                holder.status_ico.setImageResource(R.drawable.ic_paper_plane);
                holder.status_bloc_value.setBackgroundColor(context.getResources().getColor(R.color.primary));
                break;
            default:
                status_txt = "Appliqué";
                holder.status_ico.setImageResource(R.drawable.ic_calendar_check);
                holder.status_bloc_value.setBackgroundColor(context.getResources().getColor(R.color.success));
                break;
        }

        holder.sitename.setText(price.getSiteName());
        holder.superviseur_name.setText(String.format(holder.superviseur_name.getText().toString(), price.getSuperviseur_name()));
        holder.number.setText(String.format(holder.number.getText().toString(), String.valueOf(price.getId_proposal())));
        holder.price_bloc.removeAllViews();

        try {
            if (!price.getGrade1().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(1), price.getGrade1());
            }
            if (!price.getGrade2().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(2), price.getGrade2());
            }
            if (!price.getGrade3().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(3), price.getGrade3());
            }
            if (!price.getGrade4().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(4), price.getGrade4());
            }
            if (!price.getGrade5().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(5), price.getGrade5());
            }
            if (!price.getGrade6().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(6), price.getGrade6());
            }
            if (!price.getGrade7().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(7), price.getGrade7());
            }
            if (!price.getGrade8().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(8), price.getGrade8());
            }
            if (!price.getGrade9().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(9), price.getGrade9());
            }
            if (!price.getGrade10().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(10), price.getGrade10());
            }
            if (!price.getGrade11().equals("0.00")) {
                generatePriceRow(holder, Util.getGradeName(11), price.getGrade11());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.date_add_value.setText(price.getAdd_date());
        holder.heure_add_value.setText(price.getAdd_time());
        holder.date_application_value.setText(price.getApplication_date());
        holder.heure_application_value.setText(price.getApplication_time());
        holder.status_value.setText(status_txt);
        holder.status_value.setTypeface(poppins_bold);

        return convertView;
    }

    static class ViewHolder {
        TextView sitename, superviseur_name, number, date_add_value, heure_add_value, date_application_value, heure_application_value, status_value, date_add_title, date_application_title, status_title;
        LinearLayout price_bloc, status_bloc_value, status_bloc;
        ImageView status_ico;
    }

    private void generatePriceRow(ViewHolder holder, String grade_name_val, String grade_price_val) {
        View price_row = null;
        price_row = LayoutInflater.from(context).inflate(R.layout.h_price_row_add, holder.price_bloc, false);
        View.generateViewId();
        PriceRowArray.add(price_row.getId());
        Typeface poppins_bold = ResourcesCompat.getFont(context, R.font.poppins_bold);
        TextView grade_name, grade_price;
        grade_name = price_row.findViewById(R.id.grade_name);
        grade_price = price_row.findViewById(R.id.grade_price);
        grade_name.setTypeface(poppins_bold);
        grade_name.setText(grade_name_val);
        grade_price.setText(grade_price_val);
        holder.price_bloc.addView(price_row);
    }

    private void RemoveAllPriceRow(ViewHolder holder) {
        for (int elm : PriceRowArray) {
            holder.price_bloc.removeAllViews();
        }
    }
}