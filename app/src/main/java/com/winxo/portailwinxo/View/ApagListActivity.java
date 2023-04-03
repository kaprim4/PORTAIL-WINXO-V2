package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.winxo.portailwinxo.Adapter.PriceListAdapter;
import com.winxo.portailwinxo.Model.Price;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.PriceRequest;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApagListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private SessionManager sessionManager;
    private TextView code_sap, libelle, id_city, total_list, subtitle;
    private LinearLayout actual_bloc;
    private ListView recycler_view;
    private PriceListAdapter adapter;
    private ArrayList<Price> priceList;
    private Button refresh_btn, all_list_btn;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;
    private String limit = "";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apag_list);

        initialiseVars();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        total_list.setVisibility(View.INVISIBLE);
        blocUserEventClick();

        sessionManager = new SessionManager(this);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        PriceRequest priceRequest = new PriceRequest(this, getApplicationContext(), queue);

        if (sessionManager.getIs_logged()) {
            code_sap.setText(sessionManager.getCode_sap());
            libelle.setText(sessionManager.getDisplay_name());
            id_city.setText(sessionManager.getId_city());
        }

        if (getIntent().hasExtra("limit")) {
            limit = getIntent().getStringExtra("limit");
            all_list_btn.setText("Mois en cours");
        }else{
            all_list_btn.setText("Liste complète");
        }

        all_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!limit.equals("")) {
                    gotoApagListActivity();
                } else {
                    gotoApagListAllActivity();
                }
            }
        });

        priceRequest.getCurrentPriceByid_site(sessionManager.getId_site(), new PriceRequest.CurrentPriceByid_siteCallback() {
            @Override
            public void onSuccess(JSONArray gradelist_prices) {
                try {
                    actual_bloc.removeAllViews();
                    for (int i = 0; i < gradelist_prices.length(); i++) {
                        JSONObject melangeObject = gradelist_prices.getJSONObject(i);
                        View gradeline = null;
                        Typeface poppins_bold = ResourcesCompat.getFont(getApplicationContext(), R.font.poppins_bold);
                        gradeline = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grade_line, actual_bloc, false);
                        TextView grade_name, grade_actual_price, grade_price_unit;
                        grade_name = gradeline.findViewById(R.id.grade_name);
                        grade_actual_price = gradeline.findViewById(R.id.grade_actual_price);
                        grade_price_unit = gradeline.findViewById(R.id.price_unit);
                        grade_name.setTypeface(poppins_bold);
                        grade_name.setText(melangeObject.getString("name"));
                        grade_actual_price.setText(melangeObject.getString("price"));
                        grade_price_unit.setTypeface(poppins_bold);
                        actual_bloc.addView(gradeline);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        blocUserEventClick();
        getPriceList(sessionManager.getId_site());
        priceList = new ArrayList<>();
        adapter = new PriceListAdapter(getApplicationContext(), priceList);
        recycler_view.setAdapter(adapter);

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocUserEventClick();
                Util.updateParametersFromDB(getApplicationContext());
                Util.updateSiteInfo(getApplicationContext());
                getPriceList(sessionManager.getId_site());
                priceList = new ArrayList<>();
                adapter = new PriceListAdapter(getApplicationContext(), priceList);
                recycler_view.setAdapter(adapter);
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMenuActivity();
            }
        });

        b_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.apag_add:
                        gotoApagActivity();
                        break;
                    case R.id.apag_list:
                        gotoApagListActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        refresh_btn = findViewById(R.id.refresh_btn);
        code_sap = findViewById(R.id.code_sap);
        libelle = findViewById(R.id.libelle);
        id_city = findViewById(R.id.city);
        toolbar = findViewById(R.id.toolbar);
        recycler_view = findViewById(R.id.recycler_view);
        actual_bloc = findViewById(R.id.actual_bloc);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
        total_list = findViewById(R.id.total_list);
        all_list_btn = findViewById(R.id.all_list_btn);
        subtitle = findViewById(R.id.subtitle);
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("SetTextI18n")
    public void getPriceList(String id_site) {
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        PriceRequest request = new PriceRequest(this, getApplicationContext(), queue);
        request.getPricesList(id_site, limit, new PriceRequest.PriceCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ArrayList<Price> prices) {
                priceList.addAll(prices);
                setListViewHeight(recycler_view);
                adapter.notifyDataSetChanged();
                total_list.setText(priceList.size() + " entrée(s)");
                total_list.setVisibility(View.VISIBLE);
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });

        if (limit.equals("")) {
            subtitle.setText("Mes demandes de changement de prix durant les " + sessionManager.getDAY_LIMIT_FOR_LISTING() + " derniers Jours");
        } else {
            subtitle.setText("Toutes mes demandes de changement de prix durant les 2 derniers mois.");
        }
    }

    private void gotoMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoApagActivity() {
        Intent intent = new Intent(this, ApagActivity.class);
        startActivity(intent);
    }

    private void gotoApagListActivity() {
        Intent intent = new Intent(this, ApagListActivity.class);
        startActivity(intent);
    }

    private void gotoApagListAllActivity() {
        Intent intent = new Intent(this, ApagListActivity.class);
        intent.putExtra("limit", "all");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_presentation) {
            Intent intent = new Intent(getApplicationContext(), PresentationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fb) {
            try {
                getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1392305551029582"));
                startActivity(facebookIntent);
            } catch (Exception e) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WinxoOfficiel"));
                startActivity(facebookIntent);
            }
        } else if (id == R.id.nav_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.winxo.com/")));
        } else if (id == R.id.nav_mentions_legales) {
            Intent intent = new Intent(getApplicationContext(), LegalMentionActivity.class);
            startActivity(intent);
        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void blocUserEventClick() {
        loading_bloc.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unblocUserEventClick() {
        loading_bloc.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}