package com.winxo.portailwinxo.View;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.winxo.portailwinxo.Adapter.StationListAdapter;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.Station;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Utilities.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TabLayout tlUserProfileTabs;
    private CircleImageView ivUserProfilePhoto;
    private Button update_btn;
    private TextView fullname, username, userEmail, nbr_ps, nbr_pa, nbr_pan;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private ListView station_rv;
    private StationListAdapter adapter;
    private ArrayList<Station> stationList;
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String latitude, longitude;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initialiseVars();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        if (sessionManager.getIs_logged()) {
            String id_user = sessionManager.getId_site();
            if (!sessionManager.getProfile_pic().isEmpty()) {
                Picasso.get().load(sessionManager.getProfile_pic()).into(ivUserProfilePhoto);
            } else {
                Picasso.get().load(R.drawable.logo_profile).into(ivUserProfilePhoto);
            }
            String u_fullname = sessionManager.getDisplay_name();
            fullname.setText(u_fullname);
            username.setText(sessionManager.getUsername());
            userEmail.setText(sessionManager.getEmail());
            nbr_ps.setText(sessionManager.getPrix_saisis());
            nbr_pa.setText(sessionManager.getPrix_appliques());
            nbr_pan.setText(sessionManager.getPrix_annules());
        }

        Util.updateStatisticsFromDB(UserProfileActivity.this, getApplicationContext(), sessionManager.getId_site());

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserUpdateActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("UPDATE_USER")){
            String message = intent.getStringExtra("UPDATE_USER");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

        stationList = new ArrayList<>();
        getStationList(sessionManager.getLatitude(), sessionManager.getLongitude());
        adapter = new StationListAdapter(getApplicationContext(), stationList);
        station_rv.setAdapter(adapter);
    }

    private void initialiseVars(){
        toolbar = findViewById(R.id.toolbar);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        userEmail = findViewById(R.id.userEmail);
        nbr_ps = findViewById(R.id.nbr_ps);
        nbr_pa = findViewById(R.id.nbr_pa);
        nbr_pan = findViewById(R.id.nbr_pan);
        ivUserProfilePhoto = findViewById(R.id.ivUserProfilePhoto);
        update_btn = findViewById(R.id.update_btn);
        station_rv = findViewById(R.id.station_list);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    public void getStationList(String Latitude, String Longitude){
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        MainRequest mainRequest = new MainRequest(this, queue);
        mainRequest.getStationList(Latitude, Longitude, new MainRequest.StationCallback() {
            @Override
            public void onSuccess(ArrayList<Station> stations) {
                stationList.addAll(stations);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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