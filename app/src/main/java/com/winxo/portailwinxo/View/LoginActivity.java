package com.winxo.portailwinxo.View;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;
import static com.winxo.portailwinxo.Utilities.Constants.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.Site;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Utilities.Util;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private FloatingActionButton biometric_btn;
    private TextInputLayout til_username, til_password;
    private TextView version_name, id_support;
    private CheckBox remember;
    private Button login_button;
    private BottomNavigationView b_nav_view;
    private RequestQueue queue;
    private MainRequest mainRequest;
    private SessionManager sessionManager;
    private SecurePreferences preferences;
    private String spref_username;
    private String spref_password;
    private String spref_build_version;
    private String spref_hashed_pws_db;
    private String spref_hashed_pws;
    private String spref_fingerprint_action;
    private String spref_activate_gps;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location lastLocation;
    private LinearLayout loading_bloc;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialiseVars();

        Util.updateParametersFromDB(getApplicationContext());
        sessionManager = new SessionManager(this);
        blocUserEventClick();

        preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        spref_password = preferences.getString("password");
        spref_build_version = preferences.getString("build_version");
        spref_hashed_pws_db = preferences.getString("hashed_pws_db");
        spref_hashed_pws = preferences.getString("hashed_pws");
        String spref_remember = preferences.getString("remember");
        String spref_id_support = preferences.getString("id_support");
        String spref_version = preferences.getString("version");
        String spref_imei = preferences.getString("imei");
        //params
        spref_fingerprint_action = preferences.getString("fingerprint");
        String spref_activate_alerts = preferences.getString("activate_alerts");
        String spref_activate_notification_push = preferences.getString("activate_notification_push");
        spref_activate_gps = preferences.getString("activate_gps");

        id_support.setText("SUPPORT ID: " + spref_id_support);
        version_name.setText("VERSION: " + spref_version);

        if (spref_fingerprint_action == null) {
            preferences.put("fingerprint", "0");
            spref_fingerprint_action = preferences.getString("fingerprint");
        }
        if (spref_remember == null) {
            preferences.put("remember", "0");
            spref_remember = preferences.getString("remember");
        }

        if (spref_activate_gps == null) {
            preferences.put("activate_gps", "-1");
            spref_activate_gps = preferences.getString("activate_gps");
        }

        initLocationRetreiver();

        unblocUserEventClick();

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                loginToApp(spref_username, spref_password, spref_fingerprint_action);
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //Toast.makeText(LoginActivity.this, errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Toast.makeText(LoginActivity.this, "FAILED", Toast.LENGTH_LONG).show();
            }
        });

        biometric_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("ID tactile requis")
                        .setDescription("Touchez le capteur d'identification tactile")
                        .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                        .build();
                biometricPrompt.authenticate(promptInfo);
            }
        });


        if (spref_username != null && !spref_username.equals("")) {
            Objects.requireNonNull(til_username.getEditText()).setText(spref_username);
            if (spref_password != null && !spref_password.equals("")) {
                Objects.requireNonNull(til_password.getEditText()).setText(spref_password);
            }
            if (spref_fingerprint_action != null && !spref_fingerprint_action.equals("0")) {
                biometric_btn.setVisibility(View.VISIBLE);
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("ID tactile requis")
                        .setDescription("Touchez le capteur d'identification tactile")
                        .setNegativeButtonText("Annuler")
                        .build();
                biometricPrompt.authenticate(promptInfo);
            } else {
                biometric_btn.setVisibility(View.INVISIBLE);
            }
        }

        if (spref_remember != null && !spref_remember.equals("0")) {
            remember.setChecked(true);
            til_username.setEnabled(false);
            Objects.requireNonNull(til_username.getEditText()).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGrey));
            til_password.requestFocus();
        } else {
            remember.setChecked(false);
            til_username.setEnabled(true);
            Objects.requireNonNull(til_username.getEditText()).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
            til_username.requestFocus();
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preferences.put("remember", "1");
                    remember.setChecked(true);
                    til_username.setEnabled(false);
                    til_username.getEditText().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGrey));
                    til_password.requestFocus();
                } else {
                    preferences.put("remember", "0");
                    remember.setChecked(false);
                    til_username.setEnabled(true);
                    til_username.getEditText().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
                    til_username.requestFocus();
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userUsername = til_username.getEditText().getText().toString();
                final String userPaswd = Objects.requireNonNull(til_password.getEditText()).getText().toString().trim();

                if (userUsername.isEmpty()) {
                    til_username.setError("L'identifiant est vide.");
                    til_username.requestFocus();
                    unblocUserEventClick();
                    Log.d("APAGLOGIN", "identifiant est vide.");

                } else if (userPaswd.isEmpty()) {
                    til_password.setError("Le mot de passe est vide.");
                    til_password.requestFocus();
                    unblocUserEventClick();
                    Log.d("APAGLOGIN", "Le mot de passe est vide.");

                } else {
                    unblocUserEventClick();
                    loginToApp(userUsername, userPaswd, "0");
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
                    case R.id.forgotten_password:
                        gotoForgottenPasswordActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });
    }

    private void initialiseVars() {
        version_name = findViewById(R.id.version_name);
        id_support = findViewById(R.id.id_support);
        til_username = findViewById(R.id.username);
        til_password = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        login_button = findViewById(R.id.login_button);
        sessionManager = new SessionManager(this);
        biometric_btn = findViewById(R.id.biometric_btn);
        b_nav_view = findViewById(R.id.b_nav_view);
        loading_bloc = findViewById(R.id.loading_bloc);
    }

    private void loginToApp(String username, String password, String fingerprint) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mainRequest = new MainRequest(this, queue);
        blocUserEventClick();
        mainRequest.login(username, password, fingerprint, new MainRequest.LoginCallback() {
            @Override
            public void onSuccess(String _id_user, String _username, String _password, String build_version, String hashed_pws_db, String hashed_pws) {
                sessionManager.updateIdUser(_id_user);
                getUserInfoByUserName(username, password);
                if (spref_build_version == null || spref_build_version.equals("")) {
                    preferences.put("build_version", spref_build_version);
                }
                if (spref_hashed_pws_db == null || spref_hashed_pws_db.equals("")) {
                    preferences.put("hashed_pws_db", spref_hashed_pws_db);
                }
                if (spref_hashed_pws == null || spref_hashed_pws.equals("")) {
                    preferences.put("hashed_pws", spref_hashed_pws);
                }
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                unblocUserEventClick();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserInfoByUserName(String username, String userPaswd) {
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mainRequest = new MainRequest(this, queue);
        mainRequest.getSiteByUserName(username, new MainRequest.SiteCallback() {
            @Override
            public void onSuccess(String id_site, String id_profile, String id_animateur, String id_animateur_site, String superviseur_name, String display_name, String nb_totale, String nb_active, String nb_standby, String code_sap, String _username, String password, String libelle, String tel, String email, String id_city, String id_company, String address_ip, String imei, String GradeId_list, String date_upd, String status, Boolean hasFusion, String prix_saisis, String prix_appliques, String prix_annules) {
                Site logedUser = new Site(id_site);
                logedUser.setId_profile(id_profile);
                logedUser.setId_animateur(id_animateur);
                logedUser.setId_animateur_site(id_animateur_site);
                logedUser.setSuperviseur_name(superviseur_name);
                logedUser.setDisplay_name(display_name);
                logedUser.setNb_totale(nb_totale);
                logedUser.setNb_active(nb_active);
                logedUser.setNb_standby(nb_standby);
                logedUser.setCode_sap(code_sap);
                logedUser.setUsername(username);
                logedUser.setPassword(password);
                logedUser.setLibelle(libelle);
                logedUser.setTel(tel);
                logedUser.setEmail(email);
                logedUser.setId_city(id_city);
                logedUser.setId_company(id_company);
                logedUser.setAddress_ip(address_ip);
                logedUser.setImei(imei);
                logedUser.setGradeId_list(GradeId_list);
                logedUser.setDate_upd(date_upd);
                logedUser.setStatus(status);
                logedUser.setHasFusion(hasFusion);
                sessionManager.insertUser(logedUser);
                if (remember.isChecked()) {
                    preferences.put("username", username);
                    preferences.put("password", userPaswd);
                    preferences.put("remember", "1");
                } else {
                    preferences.put("username", "");
                    preferences.put("password", "");
                    preferences.put("remember", "0");
                }
                unblocUserEventClick();
                Intent intent;
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra("id_profile", id_profile);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                unblocUserEventClick();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Voulez-vous fermer l'application ?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Se déconnecter",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDisconnected();
                    }
                });
        builder.setNegativeButton(
                "Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getDisconnected() {
        stopLocationUpdates();
        sessionManager.logOut();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    initLocationRetreiver();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> {
            Log.e(TAG, "Location setting are OK...");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }).addOnFailureListener(e -> {
            int statusCode = ((ApiException) e).getStatusCode();
            Log.e(TAG, "Inside error: " + statusCode);
        });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(task -> {
            Log.e(TAG, "Stop Location updates");
        });
    }

    private void receiveLocation(LocationResult locationResult) {
        lastLocation = locationResult.getLastLocation();
        assert lastLocation != null;
        sessionManager.updateGPS(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));

        if (spref_activate_gps.equals("-1") || spref_activate_gps.equals("1")) {
            final LocationManager manager = (LocationManager) LoginActivity.this.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LoginActivity.this)) {
                Log.e(TAG, "Gps already enabled");
                sessionManager.updateGPS(String.valueOf(lastLocation.getLatitude()), String.valueOf(lastLocation.getLongitude()));
            }
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(LoginActivity.this)) {
                Log.e(TAG, "Gps not enabled");
                gotoActivateGpsActivity();
            } else {
                Log.e(TAG, "Gps already enabled");
            }
        } else {
            Log.e(TAG, "Gps not enabled & parametered to 0");
            sessionManager.updateGPS("0", "0");
        }
    }

    private void initLocationRetreiver() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LoginActivity.this);
        mSettingsClient = LocationServices.getSettingsClient(LoginActivity.this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                receiveLocation(locationResult);
            }
        };
        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setMinUpdateIntervalMillis(500)
                .setMinUpdateDistanceMeters(1)
                .setWaitForAccurateLocation(true)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        startLocationUpdates();
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void gotoForgottenPasswordActivity() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void gotoActivateGpsActivity() {
        Intent intent = new Intent(this, ActivateGpsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("from", "Login");
        startActivity(intent);
        finish();
    }

    private void gotoSplashScreenActivity() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoNeedHelpActivity() {
        final ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.need_help, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        TextView mail_support = dialogView.findViewById(R.id.mail_support);
        TextView tel_support = dialogView.findViewById(R.id.tel_support);
        Button close_btn = dialogView.findViewById(R.id.close_btn);
        mail_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = "Besoin d'aide";
                String body = "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + mail_support.getText().toString() + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body));
                intent.setData(data);
                startActivity(intent);
            }
        });
        tel_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tel_support.getText().toString()));
                startActivity(callIntent);
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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