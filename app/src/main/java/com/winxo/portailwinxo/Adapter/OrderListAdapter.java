package com.winxo.portailwinxo.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.RequestQueue;
import com.winxo.portailwinxo.Model.Order;
import com.winxo.portailwinxo.Model.OrderDetail;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.OrderRequest;
import com.winxo.portailwinxo.Utilities.Util;
import com.winxo.portailwinxo.View.OrderListActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderListAdapter extends BaseAdapter implements Filterable {

    private final Activity activity;
    private ArrayList<Order> orderList;
    private final LayoutInflater layoutInflater;
    public ArrayList<Order> orig;
    static int id = 1;
    private final ArrayList<Integer> OrderRowArray = new ArrayList<Integer>();
    public int count_list = 0;
    private SessionManager sessionManager;
    int cancel_delay;

    @SuppressLint("SimpleDateFormat")
    public OrderListAdapter(Activity activity, ArrayList<Order> orderList) {
        this.activity = activity;
        this.orderList = orderList;
        layoutInflater = LayoutInflater.from(activity.getApplicationContext());
        sessionManager = new SessionManager(activity.getApplicationContext());
        cancel_delay = Integer.parseInt(sessionManager.getORDER_CANCEL_DELAY());
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    public int getListCount() {
        return count_list;
    }

    @Override
    public Object getItem(int i) {
        return orderList.get(i);
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
                final ArrayList<Order> results = new ArrayList<Order>();
                if (orig == null)
                    orig = orderList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Order g : orig) {
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
                orderList = (ArrayList<Order>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @SuppressLint({"InflateParams", "UseCompatLoadingForDrawables", "SetTextI18n"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Order order = orderList.get(position);
        String status_txt = "";
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.order_row, null);
            holder = new ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.ref_cmd = (TextView) convertView.findViewById(R.id.ref_cmd);
            holder.doc_sale = (TextView) convertView.findViewById(R.id.doc_sale);
            holder.date_add_title = (TextView) convertView.findViewById(R.id.date_add_title);
            holder.date_add_value = (TextView) convertView.findViewById(R.id.date_add_value);
            holder.heure_add_value = (TextView) convertView.findViewById(R.id.heure_add_value);
            holder.date_application_title = (TextView) convertView.findViewById(R.id.date_application_title);
            holder.date_shipping_value = (TextView) convertView.findViewById(R.id.date_shipping_value);
            holder.status_value = (TextView) convertView.findViewById(R.id.status_value);
            holder.customer_cmd_bloc = (LinearLayout) convertView.findViewById(R.id.customer_cmd_bloc);
            holder.sap_cmd_bloc = (LinearLayout) convertView.findViewById(R.id.sap_cmd_bloc);
            holder.date_heure_add_bloc = (LinearLayout) convertView.findViewById(R.id.date_heure_add_bloc);
            holder.status_bloc = (LinearLayout) convertView.findViewById(R.id.status_bloc);
            holder.status_bloc_value = (LinearLayout) convertView.findViewById(R.id.status_bloc_value);
            holder.status_ico = (ImageView) convertView.findViewById(R.id.status_ico);
            holder.customer_cmd_title = (TextView) convertView.findViewById(R.id.customer_cmd_title);
            holder.sap_cmd_title = (TextView) convertView.findViewById(R.id.sap_cmd_title);

            holder.operation_bloc = (LinearLayout) convertView.findViewById(R.id.operation_bloc);
            holder.cancel_btn = (Button) convertView.findViewById(R.id.cancel_btn);
            holder.cancel_val = (TextView) convertView.findViewById(R.id.cancel_val);

            holder.site_bloc = (LinearLayout) convertView.findViewById(R.id.site_bloc);
            holder.site_name = (TextView) convertView.findViewById(R.id.site_name);
            holder.doc_vente_bloc = (LinearLayout) convertView.findViewById(R.id.doc_vente_bloc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.status_bloc_value.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.rounded_red_bloc));
        switch (order.getOrder_status()) {
            case 0:
                status_txt = "Saisie";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pencil_alt));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_0));
                break;
            case 1:
                status_txt = "Crée";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pencil_alt));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_1));
                break;
            case 2:
                status_txt = "Planifiée";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_calendar_check));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_2));
                break;
            case 3:
                status_txt = "Chargée";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_production_quantity_limits_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_3));
                break;
            case 4:
                status_txt = "Livraison complète";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_local_shipping_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_4));
                break;
            case 5:
                status_txt = "Annulée";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_ban));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_5));
                break;
            case 6:
                status_txt = "Invalide";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_contact_support_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_6));
                break;
            case 7:
                status_txt = "Abandonnée";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_delete_forever_16));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_7));
                break;
            case 8:
                status_txt = "Livraison partielle";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_time_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_8));
                break;
            case 9:
                status_txt = "En attente";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_time_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_9));
                break;
            default:
                status_txt = "En file d'attente";
                holder.status_ico.setBackground(activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_time_24));
                holder.status_bloc_value.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.order_99));
                break;
        }

        holder.ref_cmd.setText(String.format(holder.ref_cmd.getText().toString(), order.getSell_doc_web()));
        holder.doc_sale.setText(String.format(holder.doc_sale.getText().toString(), order.getSell_doc_sap()));
        holder.number.setText(String.format(holder.number.getText().toString(), String.valueOf(order.getId_order())));

        // qty desired
        holder.customer_cmd_bloc.removeAllViews();
        holder.sap_cmd_bloc.removeAllViews();
        try {
            if (order.getOrder_posts().size() > 0) {
                for (int i = 0; i < order.getOrder_posts().size(); i++) {
                    OrderDetail orderDetail = order.getOrder_posts().get(i);
                    generateOrderDetailRow(holder.customer_cmd_bloc, orderDetail.getProduct_name(), String.valueOf(orderDetail.getQty()), R.color.colorOrange);
                    if (orderDetail.getPlanned_qty() > 0)
                        generateOrderDetailRow(holder.sap_cmd_bloc, orderDetail.getProduct_name(), String.valueOf(orderDetail.getPlanned_qty()), R.color.colorGreen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.date_add_value.setText(order.getDate_add());
        holder.heure_add_value.setText(order.getTime_add());
        holder.date_shipping_value.setText(order.getDesired_delivery_date());
        holder.status_value.setText(status_txt);

        Typeface poppins_bold = ResourcesCompat.getFont(activity.getApplicationContext(), R.font.poppins_bold);
        holder.doc_sale.setTypeface(poppins_bold);
        holder.date_add_title.setTypeface(poppins_bold);
        holder.date_application_title.setTypeface(poppins_bold);
        holder.status_value.setTypeface(poppins_bold);
        holder.customer_cmd_title.setTypeface(poppins_bold);
        holder.sap_cmd_title.setTypeface(poppins_bold);
        holder.site_name.setTypeface(poppins_bold);

        holder.status_value.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorWhite));

        holder.cancel_val.setVisibility(View.GONE);
        holder.cancel_btn.setVisibility(View.GONE);

        try {
            if (order.getOrder_status() == 99) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                Calendar c = Calendar.getInstance();
                Date d1 = c.getTime();
                Date d2 = sdf.parse(order.getOrder_after_elapsed());
                long different = 0;
                if (d2 != null) {
                    different = d2.getTime() - d1.getTime();
                }
                if (different > 0) {
                    holder.cancel_val.setVisibility(View.VISIBLE);
                    holder.cancel_btn.setVisibility(View.VISIBLE);
                }
                new CountDownTimer(different, 1000) {
                    public void onTick(long millisUntilFinished) {
                        holder.cancel_val.setText("Délai restant pour annuler: " + Util.convertDuration(millisUntilFinished));
                    }

                    public void onFinish() {
                        holder.cancel_val.setVisibility(View.GONE);
                        holder.cancel_btn.setVisibility(View.GONE);
                        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
                        OrderRequest orderRequest = new OrderRequest(activity.getApplicationContext(), queue);
                        orderRequest.ApplyOrder(String.valueOf(order.getId_order()), new OrderRequest.ApplyOrderCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Util.displayPopupAndGoTo(activity, message, 3000, true, OrderListActivity.class);
                            }
                            @Override
                            public void onError(String message) {
                                Util.correctDialog(activity, message);
                            }
                        });
                    }
                }.start();
            }
        } catch (Exception e) {
            holder.cancel_val.setText("Erreur : " + e.getMessage());
        }

        holder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setIcon(R.drawable.ic_baseline_warning_24);
                builder.setTitle("Etes-vous sûr de vouloir annuler cette commande ?");
                builder.setPositiveButton(R.string.validate_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface diag, int id) {
                        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
                        OrderRequest orderRequest = new OrderRequest(activity.getApplicationContext(), queue);
                        orderRequest.cancelOrder(String.valueOf(order.getId_order()), new OrderRequest.cancelOrderCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Util.displayPopupAndGoTo(activity, message, 3000, true, OrderListActivity.class);
                            }

                            @Override
                            public void onError(String message) {
                                Util.correctDialog(activity, message);
                            }
                        });
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface diag, int id) {
                        diag.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        if(sessionManager.getId_profile().equals("1")){
            holder.site_bloc.setVisibility(View.VISIBLE);
            holder.site_name.setText(String.format(holder.site_name.getText().toString(), order.getSiteName()));
            holder.doc_vente_bloc.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView number, ref_cmd, doc_sale, date_add_title, date_add_value, heure_add_value, date_application_title, date_shipping_value, status_value, customer_cmd_title, sap_cmd_title, cancel_val, site_name;
        Button cancel_btn;
        LinearLayout customer_cmd_bloc, sap_cmd_bloc, date_heure_add_bloc, status_bloc, status_bloc_value, operation_bloc, site_bloc, doc_vente_bloc;
        ImageView status_ico;
    }

    @SuppressLint("SetTextI18n")
    private void generateOrderDetailRow(LinearLayout holder, String product_name, String qty, int color) {
        View order_row = null;
        TextView product_name_tv, qty_tv;
        order_row = LayoutInflater.from(activity).inflate(R.layout.order_qty_row, holder, false);
        View.generateViewId();
        OrderRowArray.add(order_row.getId());
        product_name_tv = order_row.findViewById(R.id.product_name);
        qty_tv = order_row.findViewById(R.id.qty);
        product_name_tv.setText(String.format(product_name_tv.getText().toString(), product_name));
        qty_tv.setBackgroundColor(activity.getApplicationContext().getResources().getColor(color));
        qty_tv.setText(qty);
        holder.addView(order_row);
    }
}