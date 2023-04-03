package com.winxo.portailwinxo.View;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Service.PortailWinxoService;

import java.util.List;
import java.util.concurrent.Executor;

public class ParamsActivity extends AppCompatActivity implements LocationListener {

    private Toolbar toolbar;
    private TextView password_update_action, disconnect;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch activate_gps, activate_alerts, activate_notification_push, fingerprint_action;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private FloatingActionButton home_btn;
    private SessionManager sessionManager;
    private SecurePreferences preferences;
    private String spref_username, spref_password, spref_build_version, spref_hashed_pws_db, spref_hashed_pws, spref_remember, spref_fingerprint_action, spref_activate_alerts, spref_activate_notification_push, spref_activate_gps, spref_id_support, spref_version, spref_imei, id_station;
    protected static final String TAG = "LocationOnOff";
    private Location location;
    private LinearLayout loading_bloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

        initialiseVars();

        preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        spref_password = preferences.getString("password");
        spref_build_version = preferences.getString("build_version");
        spref_hashed_pws_db = preferences.getString("hashed_pws_db");
        spref_hashed_pws = preferences.getString("hashed_pws");
        spref_remember = preferences.getString("remember");
        spref_id_support = preferences.getString("id_support");
        spref_version = preferences.getString("version");
        spref_imei = preferences.getString("imei");
        //params
        spref_fingerprint_action = preferences.getString("fingerprint");
        spref_activate_alerts = preferences.getString("activate_alerts");
        spref_activate_notification_push = preferences.getString("activate_notification_push");
        spref_activate_gps = preferences.getString("activate_gps");

        sessionManager = new SessionManager(this);

        if (spref_fingerprint_action != null && !spref_fingerprint_action.equals("0")) {
            fingerprint_action.setChecked(true);
        } else {
            fingerprint_action.setChecked(false);
        }

        if (spref_activate_alerts != null && !spref_activate_alerts.equals("0")) {
            activate_alerts.setChecked(true);
        } else {
            activate_alerts.setChecked(false);
        }

        if (spref_activate_notification_push != null && !spref_activate_notification_push.equals("0")) {
            activate_notification_push.setChecked(true);
        } else {
            activate_notification_push.setChecked(false);
        }

        if (spref_activate_gps != null && !spref_activate_gps.equals("0")) {
            activate_gps.setChecked(true);
        } else {
            activate_gps.setChecked(false);
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                preferences.put("fingerprint", "1");
                fingerprint_action.setChecked(true);
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                preferences.put("fingerprint", "0");
                fingerprint_action.setChecked(false);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                preferences.put("fingerprint", "0");
                fingerprint_action.setChecked(false);
            }
        });

        fingerprint_action.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Validation d'utilisateur requise")
                            .setDescription("Touchez le capteur d'identification tactile")
                            //.setNegativeButtonText("Annuler")
                            .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                            .build();
                    biometricPrompt.authenticate(promptInfo);
                } else {
                    preferences.put("fingerprint", "0");
                    fingerprint_action.setChecked(false);
                }
            }
        });

        activate_gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    final LocationManager manager = (LocationManager) ParamsActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(ParamsActivity.this)) {
                        Log.e(TAG, "Gps already enabled");
                        if (getLocationActuelle())
                            sessionManager.updateGPS(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        else
                            sessionManager.updateGPS("0", "0");
                        preferences.put("activate_gps", "1");
                        activate_gps.setChecked(true);
                    }
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(ParamsActivity.this)) {
                        Log.e(TAG, "Gps not enabled");
                        gotoActivateGpsActivity();
                    } else {
                        Log.e(TAG, "Gps already enabled");
                        preferences.put("activate_gps", "1");
                        activate_gps.setChecked(true);
                    }
                } else {
                    preferences.put("activate_gps", "0");
                    activate_gps.setChecked(false);
                }
            }
        });

        activate_alerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preferences.put("activate_alerts", "1");
                    activate_alerts.setChecked(true);
                } else {
                    preferences.put("activate_alerts", "0");
                    activate_alerts.setChecked(false);
                }
            }
        });

        activate_notification_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preferences.put("activate_notification_push", "1");
                    activate_notification_push.setChecked(true);
                } else {
                    preferences.put("activate_notification_push", "0");
                    activate_notification_push.setChecked(false);
                }
            }
        });

        password_update_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPasswordUpdateActivity();
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDisconnected();
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
        activate_gps = findViewById(R.id.activate_gps);
        activate_alerts = findViewById(R.id.activate_alerts);
        activate_notification_push = findViewById(R.id.activate_notification_push);
        fingerprint_action = findViewById(R.id.fingerprint_action);
        password_update_action = findViewById(R.id.password_update_action);
        disconnect = findViewById(R.id.disconnect);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
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

    private boolean getLocationActuelle() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager != null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        return true;
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        return true;
                    }
                } else {
                    Toast.makeText(this, "Location actuelle non disponible", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    private void gotoPasswordUpdateActivity() {
        Intent intent = new Intent(getApplicationContext(), PasswordUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoActivateGpsActivity() {
        Intent intent = new Intent(this, ActivateGpsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("from", "Params");
        startActivity(intent);
        finish();
    }

    private void getDisconnected() {
        sessionManager.logOut();
        if (isMyServiceRunning()) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            stopService(service_intent);
        }
        gotoLoginActivity();
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (PortailWinxoService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        sessionManager.updateGPS(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}