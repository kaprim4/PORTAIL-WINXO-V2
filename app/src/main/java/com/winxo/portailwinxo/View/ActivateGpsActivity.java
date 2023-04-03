package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.R;

import java.util.List;

public class ActivateGpsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private SessionManager sessionManager;
    private Button activate_gps, continue_btn, skip_step;
    private SecurePreferences preferences;
    private String spref_activate_gps;

    //Ma location actuelle
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private String from;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_gps);

        initialiseVars();

        sessionManager = new SessionManager(this);
        preferences = new SecurePreferences(getApplicationContext(), true);

        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }

        activate_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LocationManager manager = (LocationManager) ActivateGpsActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(ActivateGpsActivity.this)) {
                    Log.e(TAG, "Gps already enabled");
                    gotoLoginActivity();
                }
                if (!hasGPSDevice(ActivateGpsActivity.this)) {
                    Log.e(TAG, "Gps not Supported");
                }
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(ActivateGpsActivity.this)) {
                    Log.e(TAG, "Gps not enabled");
                    enableLoc();
                } else {
                    Log.e(TAG, "Gps already enabled");
                }
            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.put("activate_gps", "1");
                gotoLoginActivity();
            }
        });

        skip_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.put("activate_gps", "0");
                gotoLoginActivity();
            }
        });
    }

    private void initialiseVars() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        activate_gps = findViewById(R.id.activate_gps);
        continue_btn = findViewById(R.id.continue_btn);
        skip_step = findViewById(R.id.skip_step);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(getApplicationContext(), "La localisation GPS est activée !", Toast.LENGTH_SHORT).show();
                    activate_gps.setVisibility(View.GONE);
                    continue_btn.setVisibility(View.VISIBLE);
                    skip_step.setVisibility(View.GONE);
                    break;
                case Activity.RESULT_CANCELED:
                    activate_gps.setVisibility(View.GONE);
                    continue_btn.setVisibility(View.GONE);
                    skip_step.setVisibility(View.VISIBLE);
                    break;
            }
        }
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

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                            Log.d(TAG, "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(30 * 1000)
                    .setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest
                    .Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(ActivateGpsActivity.this, REQUEST_LOCATION);
                                //finish();
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG, "Location error " + e.getMessage());
                            }
                            break;
                    }
                }
            });
        }
    }

    private void gotoLoginActivity() {
        String from = getIntent().getStringExtra("from");
        if (from.equals("Params")) {
            Intent intent = new Intent(this, ParamsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }
}