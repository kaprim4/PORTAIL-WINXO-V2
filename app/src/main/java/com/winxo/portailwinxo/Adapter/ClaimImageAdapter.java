package com.winxo.portailwinxo.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winxo.portailwinxo.Model.ClaimImage;
import com.winxo.portailwinxo.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ClaimImageAdapter extends BaseAdapter {

    private final Activity activity;
    private final ArrayList<ClaimImage> claimImageList;
    private final ListView listView;
    private final Button take_photo_btn, pick_photo_btn;
    private final TextView list_size;

    public ClaimImageAdapter(Activity activity, ArrayList<ClaimImage> claimImageList, ListView listView, Button take_photo_btn, Button pick_photo_btn, TextView list_size) {
        this.activity = activity;
        this.claimImageList = claimImageList;
        this.listView = listView;
        this.take_photo_btn = take_photo_btn;
        this.pick_photo_btn = pick_photo_btn;
        this.list_size = list_size;
    }

    @Override
    public int getCount() {
        return claimImageList.size();
    }

    @Override
    public Object getItem(int i) {
        return claimImageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.claim_add_item, parent, false);
            holder = new ViewHolder();
            holder.claim_img = (ImageView) row.findViewById(R.id.claim_img);
            holder.id_claim = (TextView) row.findViewById(R.id.id_claim);
            holder.date_capture = (TextView) row.findViewById(R.id.date_capture);
            holder.heure_capture = (TextView) row.findViewById(R.id.heure_capture);
            holder.delete_btn = (ImageButton) row.findViewById(R.id.delete_btn);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        ClaimImage picture = claimImageList.get(position);
        byte[] outImage = picture.get_image();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.claim_img.setImageBitmap(theImage);
        holder.claim_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.id_claim.setText(String.format(holder.id_claim.getText().toString(), picture.getId_claim()));
        holder.date_capture.setText(String.format(holder.date_capture.getText().toString(), picture.getDate_capture()));
        holder.heure_capture.setText(String.format(holder.heure_capture.getText().toString(), picture.getHeure_capture()));
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claimImageList.remove(position);
                setListViewHeight(listView);
                notifyDataSetChanged();
                if(getCount() <= 4){
                    take_photo_btn.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.colorOrange));
                    pick_photo_btn.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.colorOrange));
                } else {
                    take_photo_btn.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.colorGrey));
                    pick_photo_btn.setBackgroundColor(activity.getApplicationContext().getResources().getColor(R.color.colorGrey));
                }
                list_size.setText(String.format(list_size.getText().toString(), String.valueOf(getCount()), "4"));
            }
        });
        return row;
    }

    static class ViewHolder {
        ImageView claim_img;
        TextView id_claim;
        TextView date_capture;
        TextView heure_capture;
        ImageButton delete_btn;
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        Log.w("DESIRED WIDTH", String.valueOf(listAdapter.getCount()));
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
