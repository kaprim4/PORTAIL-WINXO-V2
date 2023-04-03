package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Requests.PriceRequest;
import com.winxo.portailwinxo.Service.PortailWinxoService;
import com.winxo.portailwinxo.Utilities.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApagSupervisorAllActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private CircleImageView avatar;
    private SessionManager sessionManager;
    private TextView navdisplay_name, navid_user, navid_city;
    private RequestQueue queue;
    private MainRequest mainRequest;
    private long backPressedTime;
    private Toast backToast;
    private Button refresh_btn;
    private ListView recycler_view;
    private PriceListAdapter adapter;
    private ArrayList<Price> priceList;
    private TextView total_list;
    private SearchView mSearchView;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private LinearLayout loading_bloc;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apag_supervision_all);
        initialiseVars();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mainRequest = new MainRequest(this, queue);

        total_list.setVisibility(View.INVISIBLE);
        unblocUserEventClick();

        if (sessionManager.getIs_logged()) {
            String id_user = sessionManager.getId_site();
            navdisplay_name.setText(sessionManager.getDisplay_name());
            navid_user.setText(sessionManager.getCode_sap());
            navid_city.setText(sessionManager.getId_city());
        }

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total_list.setVisibility(View.INVISIBLE);
                refreshList();
            }
        });

        blocUserEventClick();
        setListViewInitHeight(recycler_view);
        getPriceList(sessionManager.getId_animateur_site());
        priceList = new ArrayList<>();
        adapter = new PriceListAdapter(getApplicationContext(), priceList);
        recycler_view.setAdapter(adapter);
        setListViewHeight(recycler_view);
        recycler_view.setTextFilterEnabled(true);
        setupSearchView();

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
                    case R.id.apag_this_month:
                        gotoApagThisMonthActivity();
                        break;
                    case R.id.apag_all:
                        gotoApagAllActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Chercher ici...");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            recycler_view.clearTextFilter();
        } else {
            recycler_view.setFilterText(newText);
        }
        Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_SHORT).show();
        setListViewInitHeight(recycler_view);
        adapter.notifyDataSetChanged();
        setListViewHeight(recycler_view);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @SuppressLint("SetTextI18n")
    protected void refreshList() {
        blocUserEventClick();
        setListViewInitHeight(recycler_view);
        Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_SHORT).show();
        getPriceList(sessionManager.getId_animateur_site());
        priceList.clear();
        adapter = new PriceListAdapter(getApplicationContext(), priceList);
        recycler_view.setAdapter(adapter);
    }

    private void initialiseVars() {
        refresh_btn = findViewById(R.id.refresh_btn);
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.avatar);
        navdisplay_name = headerView.findViewById(R.id.navFullName);
        navid_user = headerView.findViewById(R.id.nav_code_sap);
        navid_city = headerView.findViewById(R.id.navCity);
        total_list = findViewById(R.id.total_list);
        recycler_view = findViewById(R.id.recycler_view);
        mSearchView = findViewById(R.id.searchView);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.press_back_again_to_exit, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void gotoMenuActivity() {
        if (Util.isMyServiceRunning(getApplicationContext())) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            stopService(service_intent);
        }
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoApagThisMonthActivity() {
        Intent intent = new Intent(this, ApagSupervisorActivity.class);
        startActivity(intent);
    }

    private void gotoApagAllActivity() {
        Intent intent = new Intent(this, ApagSupervisorAllActivity.class);
        startActivity(intent);
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        int adapter_count = 0;
        if (listAdapter == null) {

        } else {
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            adapter_count = listAdapter.getCount();
            Log.w("DESIRED WIDTH", String.valueOf(adapter_count));
            for (int i = 0; i < adapter_count; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
                Log.w("HEIGHT" + i, String.valueOf(totalHeight));
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter_count - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void setListViewInitHeight(ListView listView) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listView.getDividerHeight();
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void getPriceList(String id_animateur_site) {
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        PriceRequest request = new PriceRequest(this, getApplicationContext(), queue);
        request.getSupervisionPricesList(id_animateur_site, "all", new PriceRequest.SupervisionPriceCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ArrayList<Price> prices) {
                priceList.clear();
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