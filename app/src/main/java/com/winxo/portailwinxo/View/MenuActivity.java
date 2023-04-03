package com.winxo.portailwinxo.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Utilities.Util;

public class MenuActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;
    private RelativeLayout menu_btn_01, menu_btn_02, menu_btn_03;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private SessionManager sessionManager;
    private static final int FOREGROUND_SERVICE_PERMISSION_CODE = 301;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initialiseVars();

        Util.updateParametersFromDB(getApplicationContext());
        sessionManager = new SessionManager(this);

        if (sessionManager.getId_profile() == null) {
            gotoLoginActivity();
        }

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        MainRequest mainRequest = new MainRequest(getApplicationContext(), queue);
        mainRequest.getSiteByUserName(sessionManager.getUsername(), new MainRequest.SiteCallback() {
            @Override
            public void onSuccess(String _id_site, String _id_profile, String _id_animateur, String _id_animateur_site, String _superviseur_name, String _display_name, String _nb_totale, String _nb_active, String _nb_standby, String _code_sap, String _username, String _password, String _libelle, String _tel, String _email, String _id_city, String _id_company, String _address_ip, String _imei, String _GradeId_list, String _date_upd, String _status, Boolean _hasFusion, String _prix_saisis, String _prix_appliques, String _prix_annules) {
                sessionManager.updateSiteHasFusion(_hasFusion);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Mise à jour impossible.", Toast.LENGTH_SHORT).show();
            }
        });

        if (sessionManager.getENABLE_APAG()) {
            if(sessionManager.getHasFusion()){
                menu_btn_01.setVisibility(View.VISIBLE);
            }else{
                menu_btn_01.setVisibility(View.GONE);
            }
        } else {
            menu_btn_01.setVisibility(View.GONE);
        }

        if (sessionManager.getENABLE_ORDERS()) {
            menu_btn_03.setVisibility(View.VISIBLE);
        } else {
            menu_btn_02.setVisibility(View.GONE);
        }

        if (sessionManager.getENABLE_CLAIM()) {
            menu_btn_03.setVisibility(View.VISIBLE);
        } else {
            menu_btn_03.setVisibility(View.GONE);
        }

        menu_btn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (sessionManager.getId_profile().equals("2")) {
                    intent = new Intent(getApplicationContext(), ApagActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), ApagSupervisorActivity.class);
                }
                startActivity(intent);
            }
        });

        menu_btn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (sessionManager.getId_profile().equals("2")) {
                    intent = new Intent(getApplicationContext(), OrderActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), OrderSupervisorActivity.class);
                }
                startActivity(intent);
            }
        });

        menu_btn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (sessionManager.getId_profile().equals("2")) {
                    intent = new Intent(getApplicationContext(), ClaimActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), ClaimSupervisorActivity.class);
                }
                startActivity(intent);
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
                //                    case R.id.profile_btn:
                //                        gotoProfileActivity();
                //                        break;
                if (item.getItemId() == R.id.nav_preferences) {
                    gotoParamsActivity();
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE}, FOREGROUND_SERVICE_PERMISSION_CODE);
        }
    }

    private void initialiseVars() {
        menu_btn_01 = findViewById(R.id.menu_btn_01);
        menu_btn_02 = findViewById(R.id.menu_btn_02);
        menu_btn_03 = findViewById(R.id.menu_btn_03);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.press_back_again_to_exit, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoParamsActivity() {
        Intent intent = new Intent(this, ParamsActivity.class);
        startActivity(intent);
    }

    private void gotoProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void getDisconnected() {
        sessionManager.logOut();
        gotoLoginActivity();
    }
}