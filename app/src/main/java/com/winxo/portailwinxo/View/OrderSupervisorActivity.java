package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.winxo.portailwinxo.Adapter.OrderListAdapter;
import com.winxo.portailwinxo.Model.Order;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.OrderRequest;
import com.winxo.portailwinxo.Utilities.Util;

import java.util.ArrayList;

public class OrderSupervisorActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private SessionManager sessionManager;
    private TextView code_sap, libelle, id_city, total_list, subtitle;
    private ListView recycler_view;
    private OrderListAdapter adapter;
    private ArrayList<Order> orderList;
    private Button refresh_btn;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;
    private Button all_list_btn;
    private String limit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_supervision);

        initialiseVars();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        total_list.setVisibility(View.INVISIBLE);
        blocUserEventClick();

        sessionManager = new SessionManager(this);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        OrderRequest orderRequest = new OrderRequest(getApplicationContext(), queue);

        if(sessionManager.getIs_logged()){
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
                    gotoOrderListActivity();
                } else {
                    gotoOrderListAllActivity();
                }
            }
        });

        blocUserEventClick();
        getOrderList(sessionManager.getId_animateur_site());
        orderList = new ArrayList<>();
        adapter = new OrderListAdapter(OrderSupervisorActivity.this,orderList);
        recycler_view.setAdapter(adapter);

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocUserEventClick();
                Util.updateParametersFromDB(getApplicationContext());
                Util.updateSiteInfo(getApplicationContext());
                getOrderList(sessionManager.getId_animateur_site());
                orderList = new ArrayList<>();
                adapter = new OrderListAdapter(OrderSupervisorActivity.this,orderList);
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
                    case R.id.order_this_month:
                        gotoOrderThisMonthActivity();
                        break;
                    case R.id.order_all:
                        gotoOrderAllActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void gotoOrderThisMonthActivity() {
        Intent intent = new Intent(getApplicationContext(), OrderSupervisorActivity.class);
        startActivity(intent);
    }

    private void gotoOrderAllActivity() {
        Intent intent = new Intent(getApplicationContext(), OrderSupervisorActivity.class);
        startActivity(intent);
    }

    private void initialiseVars(){
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        refresh_btn = findViewById(R.id.refresh_btn);
        code_sap = findViewById(R.id.code_sap);
        libelle = findViewById(R.id.libelle);
        id_city = findViewById(R.id.city);
        toolbar = findViewById(R.id.toolbar);
        recycler_view = findViewById(R.id.recycler_view);
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
            Log.w("HEIGHT"+i, String.valueOf(totalHeight));
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
    public void getOrderList(String id_animateur_site){
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        OrderRequest request = new OrderRequest(getApplicationContext(), queue);
        request.getSupervisionOrderList(id_animateur_site, String.valueOf(sessionManager.getDAY_LIMIT_FOR_LISTING()), new OrderRequest.SupervisionOrderListCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ArrayList<Order> orders) {
                orderList.addAll(orders);
                setListViewHeight(recycler_view);
                adapter.notifyDataSetChanged();
                total_list.setText(orderList.size() + " entrée(s)");
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
            subtitle.setText("Mes commandes durant les " + sessionManager.getDAY_LIMIT_FOR_LISTING() + " derniers Jours");
        } else {
            subtitle.setText("Toutes mes commandes durant les 2 derniers mois.");
        }
    }

    private void gotoMenuActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoOrderListActivity() {
        Intent intent = new Intent(this, OrderSupervisorActivity.class);
        startActivity(intent);
    }

    private void gotoOrderListAllActivity() {
        Intent intent = new Intent(this, OrderSupervisorActivity.class);
        intent.putExtra("limit", "all");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

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