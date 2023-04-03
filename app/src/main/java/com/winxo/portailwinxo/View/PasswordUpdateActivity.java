package com.winxo.portailwinxo.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Utilities.Util;

public class PasswordUpdateActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout til_current_password, til_new_password, til_repeat_password;
    private Button update_btn;
    private RequestQueue queue;
    private SessionManager sessionManager;
    private FloatingActionButton home_btn;
    private String id_profile, id_station;
    private String generatedHashedPassword = "";
    private String spref_EmailAddress, spref_password;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update);

        initialiseVars();
        unblocUserEventClick();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), true);
        spref_EmailAddress = preferences.getString("EmailAddress");
        spref_password = preferences.getString("password");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        MainRequest request = new MainRequest(this, queue);

        if (getIntent().hasExtra("id_profile")) {
            id_profile = getIntent().getStringExtra("id_profile");
        } else if (getIntent().hasExtra("id_station")) {
            id_station = getIntent().getStringExtra("id_station");
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blocUserEventClick();
                generatedHashedPassword = "";
                spref_password = preferences.getString("password");
                String userCurrentPassword = til_current_password.getEditText().getText().toString();
                String userNewPassword = til_new_password.getEditText().getText().toString().trim();
                String userRepeatPassword = til_repeat_password.getEditText().getText().toString().trim();
                if (userCurrentPassword.isEmpty()) {
                    til_current_password.setError("Le mot de passe actuel est vide.");
                    til_current_password.requestFocus();
                    unblocUserEventClick();
                    Log.d("generatedHashedPassword", "Le mot de passe actuel est vide.");
                } else {
                    til_current_password.setErrorEnabled(false);
                    Log.d("generatedHashedPassword", "generatedHashedPassword: " + generatedHashedPassword);
                    Log.d("generatedHashedPassword", "spref_password: " + spref_password);
                    Log.d("generatedHashedPassword", "userCurrentPassword: " + userCurrentPassword);
                    Log.d("generatedHashedPassword", "userNewPassword: " + userNewPassword);
                    Log.d("generatedHashedPassword", "userRepeatPassword: " + userRepeatPassword);
                    if (!userCurrentPassword.equals(spref_password)) {
                        til_current_password.setError("Le mot de passe actuel est incorrect.");
                        til_current_password.requestFocus();
                        unblocUserEventClick();
                        Log.d("generatedHashedPassword", "Le mot de passe actuel est incorrect.");
                        unblocUserEventClick();
                    } else {
                        Log.d("generatedHashedPassword", "Le mot de passe actuel correct." + "\n\n");
                        if (userNewPassword.isEmpty()) {
                            til_new_password.setError("Le nouveau mot de passe est vide.");
                            til_new_password.requestFocus();
                            unblocUserEventClick();
                            Log.d("generatedHashedPassword", "Le nouveau mot de passe est vide.");
                        } else {
                            til_new_password.setErrorEnabled(false);
                            if (userRepeatPassword.isEmpty()) {
                                til_repeat_password.setError("Re-saisissez le nouveau mot de passe.");
                                til_repeat_password.requestFocus();
                                unblocUserEventClick();
                                Log.d("generatedHashedPassword", "Re-saisissez le nouveau mot de passe.");
                            } else {
                                til_repeat_password.setErrorEnabled(false);
                                if (!userNewPassword.equals(userRepeatPassword)) {
                                    til_repeat_password.setError("Les mots de passe ne correspondent pas.");
                                    til_repeat_password.requestFocus();
                                    unblocUserEventClick();
                                    Log.d("generatedHashedPassword", "Les mots de passe ne correspondent pas.");
                                } else {
                                    til_repeat_password.setErrorEnabled(false);
                                    request.updateUserPassword(spref_EmailAddress, userNewPassword, new MainRequest.UpdateUserPasswordCallback() {
                                        @Override
                                        public void onSuccess(String _hashed_pws, String _new_password, String _message) {
                                            Util.correctDialog(PasswordUpdateActivity.this, _message);
                                            preferences.put("password", _new_password);
                                            Log.d("generatedHashedPassword", "password: " + _new_password);
                                            unblocUserEventClick();
                                        }

                                        @Override
                                        public void onError(String message) {
                                            Util.errorDialog(PasswordUpdateActivity.this, message);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHomeActivity();
            }
        });
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        til_current_password = findViewById(R.id.current_password);
        til_new_password = findViewById(R.id.new_password);
        til_repeat_password = findViewById(R.id.repeat_password);
        update_btn = findViewById(R.id.update_btn);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("id_profile", id_profile);
        intent.putExtra("id_station", id_station);
        startActivity(intent);
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

