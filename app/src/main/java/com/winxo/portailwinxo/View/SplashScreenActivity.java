package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.winxo.portailwinxo.App;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private TextView version_name, id_support;
    private long backPressedTime;
    private Toast backToast;
    private static final int REQUEST_CODE = 101;
    private AppUpdateManager appUpdateManager;
    private LinearLayout blocmodule_01, blocmodule_02, blocmodule_03;
    private String spref_terms_policies;
    private SecurePreferences preferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initialiseVars();

        SessionManager sessionManager = new SessionManager(this);

        Util.updateParametersFromDB(getApplicationContext());
        appUpdateManager = AppUpdateManagerFactory.create(SplashScreenActivity.this);
        preferences = new SecurePreferences(getApplicationContext(), true);

        @SuppressLint("HardwareIds") String uniqueID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        List<String> stringsUniqueID = new ArrayList<String>();
        int index = 0;
        while (index < uniqueID.length()) {
            stringsUniqueID.add(uniqueID.substring(index, Math.min(index + 4, uniqueID.length())));
            index += 4;
        }
        String uniqueIdResult = TextUtils.join("-", stringsUniqueID).toUpperCase();
        preferences.put("id_support", uniqueIdResult);

        String version_name_ = BuildConfig.VERSION_NAME;
        preferences.put("version", version_name_);

        String imei = getIMEI();
        preferences.put("imei", imei);

        String MacWlanAddress = Util.getMACAddress("wlan0");
        String MacEthAddress = Util.getMACAddress("eth0");
        String ipAddressV4 = Util.getIPAddress(true);
        String ipAddressV6 = Util.getIPAddress(false);

        preferences.put("MacWlanAddress", MacWlanAddress);
        preferences.put("MacEthAddress", MacEthAddress);
        preferences.put("ipAddressV4", ipAddressV4);
        preferences.put("ipAddressV6", ipAddressV6);

        String spref_id_support = preferences.getString("id_support");
        String spref_version = preferences.getString("version");
        String spref_imei = preferences.getString("imei");

        id_support.setText("SUPPORT ID: " + spref_id_support);
        version_name.setText("VERSION: " + spref_version);

        if (sessionManager.getENABLE_APAG()) {
            blocmodule_01.setVisibility(View.VISIBLE);
        } else {
            blocmodule_01.setVisibility(View.GONE);
        }

        if (sessionManager.getENABLE_ORDERS()) {
            blocmodule_02.setVisibility(View.VISIBLE);
        } else {
            blocmodule_02.setVisibility(View.GONE);
        }

        if (sessionManager.getENABLE_CLAIM()) {
            blocmodule_03.setVisibility(View.VISIBLE);
        } else {
            blocmodule_03.setVisibility(View.GONE);
        }

        spref_terms_policies = preferences.getString("terms_policies");
        if (spref_terms_policies == null) {
            preferences.put("terms_policies", "0");
            spref_terms_policies = preferences.getString("terms_policies");
        }

        //VersionChecker();
        gotoPolicyActivity();
    }

    private void initialiseVars() {
        version_name = findViewById(R.id.version_name);
        id_support = findViewById(R.id.id_support);
        blocmodule_01 = findViewById(R.id.blocmodule_01);
        blocmodule_02 = findViewById(R.id.blocmodule_02);
        blocmodule_03 = findViewById(R.id.blocmodule_03);
    }

    public String getIMEI() {
        return Util.getDeviceId(getApplicationContext());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission accordée.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission refusée.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void VersionChecker() {
        Log.e("VersionChecker", "Checking for updates");
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        Log.e("VersionChecker", "appUpdateInfoTask: " + appUpdateInfoTask.toString());
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, Constants.UPDATE_REQUEST_CODE);
                    Log.e("VersionChecker", "startUpdateFlowForResult");
                } catch (IntentSender.SendIntentException e) {
                    Log.e("VersionChecker", "SendIntentException: " + e.getMessage());
                }
                Log.e("VersionChecker", "Update available");
            } else {
                Log.e("VersionChecker", "No Update available");
                gotoPolicyActivity();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.UPDATE_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.e("VersionChecker", "Update flow OK! Result code: " + resultCode);
                    break;
                case RESULT_CANCELED:
                    Log.e("VersionChecker", "Update flow CANCELED! Result code: " + resultCode);
                    break;
                case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                    Log.e("VersionChecker", "Update flow FAILED! Result code: " + resultCode);
                    break;
            }
            retartApplication();
        }
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void gotoPermissionRequestActivity() {
        Intent intent = new Intent(this, PermissionRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void gotoSplashScreenActivity() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void retartApplication() {
        Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(App.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager mgr = (AlarmManager) App.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        System.exit(2);
        ((ActivityManager) SplashScreenActivity.this.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
    }

    @SuppressLint("SetTextI18n")
    private void gotoPolicyActivity() {
        if (spref_terms_policies == null || Objects.equals(spref_terms_policies, "0")) {
            final ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_policy, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();

            NestedScrollView scrollView = dialogView.findViewById(R.id.text_scroller);
            WebView legal_mention_txt = dialogView.findViewById(R.id.legal_mention_txt);
            Button accept_btn = dialogView.findViewById(R.id.accept_btn);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams vc = scrollView.getLayoutParams();
            vc.height = height / 2;
            scrollView.setLayoutParams(vc);

            String url = Constants.WEB_SERVICE_URL + "legal_mention.html";
            legal_mention_txt.setPadding(0, 0, 0, 0);
            legal_mention_txt.setInitialScale(200);
            legal_mention_txt.loadUrl(url);
            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preferences.put("terms_policies", "1");
                    gotoPermissionRequestActivity();
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            gotoPermissionRequestActivity();
        }
    }
}