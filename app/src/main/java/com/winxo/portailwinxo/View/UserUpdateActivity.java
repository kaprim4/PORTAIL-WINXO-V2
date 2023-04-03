package com.winxo.portailwinxo.View;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;

public class UserUpdateActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText til_username, til_displayName, til_email, til_tel;
    private RequestQueue queue;
    private MainRequest mainRequest;
    private SecurePreferences preferences;
    private String spref_username;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        initialiseVars();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        getUserInfoByUserName(spref_username);
    }

    private void initialiseVars(){
        toolbar = findViewById(R.id.toolbar);
        til_username = findViewById(R.id.username);
        til_displayName = findViewById(R.id.displayName);
        til_email = findViewById(R.id.email);
        til_tel = findViewById(R.id.tel);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void getUserInfoByUserName(String username) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mainRequest = new MainRequest(this, queue);
        mainRequest.getSiteByUserName(username, new MainRequest.SiteCallback(){
            @Override
            public void onSuccess(String SiteId, String id_profile, String id_animateur, String id_animateur_site, String superviseur_name, String fullname, String nb_totale, String nb_active, String nb_standby, String code_sap, String username, String password, String libelle, String tel, String email, String city, String id_company, String address_ip, String imei, String GradeId_list, String date_upd, String statut, Boolean hasFusion, String prix_saisis, String prix_appliques, String prix_annules) {
                til_username.setText(username);
                til_displayName.setText(fullname);
                til_email.setText(email);
                til_tel.setText(tel);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

