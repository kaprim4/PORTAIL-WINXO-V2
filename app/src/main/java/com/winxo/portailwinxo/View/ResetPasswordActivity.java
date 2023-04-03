package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout til_username, til_tel;
    private TextView version_name, id_support;
    private Button reset_button;
    private LinearLayout loading_bloc;
    private RequestQueue queue;
    private MainRequest mainRequest;
    private SessionManager sessionManager;
    private String spref_username, spref_password, spref_build_version, spref_hashed_pws_db, spref_hashed_pws, spref_remember, spref_fingerprint_action, spref_id_support, spref_version, spref_imei;
    private BottomNavigationView b_nav_view;

    private static final String TAG = "ResetPasswordActivity";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initialiseVars();

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        spref_password = preferences.getString("password");
        spref_build_version = preferences.getString("build_version");
        spref_hashed_pws_db = preferences.getString("hashed_pws_db");
        spref_hashed_pws = preferences.getString("hashed_pws");
        spref_remember = preferences.getString("remember");
        spref_fingerprint_action = preferences.getString("fingerprint");
        spref_id_support = preferences.getString("id_support");
        spref_version = preferences.getString("version");
        spref_imei = preferences.getString("imei");

        id_support.setText("SUPPORT ID: " + spref_id_support);
        version_name.setText("VERSION: " + spref_version);

        sessionManager = new SessionManager(this);

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userUsername = Objects.requireNonNull(til_username.getEditText()).getText().toString().trim();
                final String userCodeSAP = Objects.requireNonNull(til_tel.getEditText()).getText().toString().trim();
                if (userUsername.isEmpty()) {
                    til_username.setError("Saisissez votre identifiant.");
                    til_username.requestFocus();
                } else if (userCodeSAP.isEmpty()) {
                    til_tel.setError("Saisissez votre Code Station.");
                    til_tel.requestFocus();
                } else {
                    sendmail(userUsername, userCodeSAP);
                }
            }
        });

        b_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.need_help:
                        gotoNeedHelpActivity();
                        break;
                    case R.id.login_btn:
                        gotoLoginActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void sendmail(String username, String code_sap) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mainRequest = new MainRequest(this, queue);
        blocUserEventClick();
        mainRequest.ResetPassword(username, code_sap, new MainRequest.ResetPasswordCallback(){
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });
    }

    private void initialiseVars(){
        version_name = findViewById(R.id.version_name);
        id_support = findViewById(R.id.id_support);
        til_username = findViewById(R.id.username);
        til_tel = findViewById(R.id.tel);
        reset_button = findViewById(R.id.reset_button);
        b_nav_view = findViewById(R.id.b_nav_view);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void gotoLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoNeedHelpActivity(){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
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