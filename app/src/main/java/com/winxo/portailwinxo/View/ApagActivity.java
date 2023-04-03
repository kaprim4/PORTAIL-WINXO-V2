package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.vicmikhailau.maskededittext.MaskedEditText;
import com.winxo.portailwinxo.Model.InputFilterMax;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.MainRequest;
import com.winxo.portailwinxo.Requests.PriceRequest;
import com.winxo.portailwinxo.Service.PortailWinxoService;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApagActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private CircleImageView avatar;
    private SessionManager sessionManager;
    private TextView navdisplay_name, nav_code_sap, navCity, code_sap, libelle, id_city, date_today, date_tomorrow, gasoil_1, gasoil_2, ssp_1, ssp_2, proposalAppTime_txt;
    private LinearLayout actual_bloc, gasoil_line2, ssp_line2, grade1_row_add, grade2_row_add, grade3_row_add, grade4_row_add, grade5_row_add, grade6_row_add, grade7_row_add, grade8_row_add, grade9_row_add, grade10_row_add, grade11_row_add, loading_bloc;
    private Button menu_btn, cancel_btn, confirm_btn;
    private ImageView date_popup;
    private EditText grade1_price, grade2_price, grade3_price, grade4_price, grade5_price, grade6_price, grade7_price, grade8_price, grade9_price, grade10_price, grade11_price;
    private MaskedEditText date_add_field;
    private RequestQueue queue;
    private PriceRequest priceRequest;
    public Thread thread;
    public int day, month, year;
    public String latitude = "", longitude = "", selected_hour, spref_username, spref_password, spref_id_support, spref_version, spref_imei;
    private Button refresh_btn;
    private Spinner time_add_spinner;
    private final String[] hours = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
    private final List<String> hour_arraylist = Arrays.asList(hours);
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_apag);

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        priceRequest = new PriceRequest(this, getApplicationContext(), queue);

        initialiseVars();
        Util.updateParametersFromDB(getApplicationContext());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (sessionManager.getIs_logged()) {
            String id_user = sessionManager.getId_site();
            code_sap.setText(sessionManager.getCode_sap());
            libelle.setText(sessionManager.getDisplay_name());
            id_city.setText(sessionManager.getId_city());
            navdisplay_name.setText(sessionManager.getDisplay_name());
            nav_code_sap.setText(sessionManager.getCode_sap());
            navCity.setText(sessionManager.getId_city());
        }

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), true);
        spref_username = preferences.getString("username");
        spref_password = preferences.getString("password");
        spref_id_support = preferences.getString("id_support");
        spref_version = preferences.getString("version");
        spref_imei = preferences.getString("imei");

        blocUserEventClick();

        refreshList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, hour_arraylist);
        //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_add_spinner.setAdapter(adapter);

        String _proposalAppTime_txt = proposalAppTime_txt.getText().toString();
        priceRequest.getNextCycle(new PriceRequest.getNextCycleCallback() {
            @Override
            public void onSuccess(String next_cycle) {
                proposalAppTime_txt.setText(_proposalAppTime_txt.replace("{proposalAppTime_txt}", next_cycle));

                int next_cycle_time = hour_arraylist.indexOf(next_cycle);
                time_add_spinner.setOnItemSelectedListener(ApagActivity.this);
                time_add_spinner.setSelection(next_cycle_time);
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                priceRequest.getNextCycle(new PriceRequest.getNextCycleCallback() {
                                    @Override
                                    public void onSuccess(String next_cycle) {
                                        proposalAppTime_txt.setText(_proposalAppTime_txt.replace("{proposalAppTime_txt}", next_cycle));
                                        //time_add_field.setText(next_cycle.replace(":", ""));
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.d(Constants.TAG, message);
                                    }
                                });
                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grade1_price.setText("");
                grade2_price.setText("");
                grade3_price.setText("");
                grade4_price.setText("");
                grade5_price.setText("");
                grade6_price.setText("");
                grade7_price.setText("");
                grade8_price.setText("");
                grade9_price.setText("");
                grade10_price.setText("");
                grade11_price.setText("");
                date_add_field.setText("");

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                String date = dateFormat.format(calendar.getTime());
                date_add_field.setText(date);
            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String p_id_site = sessionManager.getId_site();
                final String p_grade1_price = grade1_price.getText().toString().trim();
                final String p_grade2_price = grade2_price.getText().toString().trim();
                final String p_grade3_price = grade3_price.getText().toString().trim();
                final String p_grade4_price = grade4_price.getText().toString().trim();
                final String p_grade5_price = grade5_price.getText().toString().trim();
                final String p_grade6_price = grade6_price.getText().toString().trim();
                final String p_grade7_price = grade7_price.getText().toString().trim();
                final String p_grade8_price = grade8_price.getText().toString().trim();
                final String p_grade9_price = grade9_price.getText().toString().trim();
                final String p_grade10_price = grade10_price.getText().toString().trim();
                final String p_grade11_price = grade11_price.getText().toString().trim();
                final String p_date = Objects.requireNonNull(date_add_field.getText()).toString().trim();
                final String p_time = selected_hour.trim();
                double prix_revient_gasoil = 20;
                double prix_revient_ssp = 20;

                if(grade1_row_add.getVisibility() == View.VISIBLE){
                    prix_revient_ssp = Math.round(Double.parseDouble(ssp_1.getText().toString().replace(" ", "")) / 1000);
                }
                if(grade2_row_add.getVisibility() == View.VISIBLE){
                    prix_revient_gasoil = Math.round(Double.parseDouble(gasoil_1.getText().toString().replace(" ", "")) / 1000);
                }

                if (grade2_row_add.getVisibility() == View.VISIBLE) {
                    if (p_grade2_price.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Le prix du Gasoil est vide.", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(p_grade2_price) < (double) sessionManager.getPRICE_MIN_GASOIL()) {
                        Toast.makeText(getApplicationContext(), "Le prix saisi est inférieur à " + (double) sessionManager.getPRICE_MIN_GASOIL(), Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(p_grade2_price) > (double) sessionManager.getPRICE_MAX_GASOIL()) {
                        Toast.makeText(getApplicationContext(), "Le prix saisi est supérieur à " + (double) sessionManager.getPRICE_MAX_GASOIL(), Toast.LENGTH_SHORT).show();
                    }/* else if (Double.parseDouble(p_grade2_price) < prix_revient_gasoil) {
                        Toast.makeText(getApplicationContext(), "Le prix de vente du GASOIL saisi est inférieur au prix de revient.", Toast.LENGTH_SHORT).show();
                    }*/
                }

                if (grade1_row_add.getVisibility() == View.VISIBLE) {
                    if (p_grade1_price.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Le prix du SSP est vide.", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(p_grade1_price) < (double) sessionManager.getPRICE_MIN_SSP()) {
                        Toast.makeText(getApplicationContext(), "Le prix saisi est inférieur à " + (double) sessionManager.getPRICE_MIN_SSP(), Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(p_grade1_price) > (double) sessionManager.getPRICE_MAX_SSP()) {
                        Toast.makeText(getApplicationContext(), "Le prix saisi est supérieur à " + (double) sessionManager.getPRICE_MAX_SSP(), Toast.LENGTH_SHORT).show();
                    }/* else if (Double.parseDouble(p_grade1_price) < prix_revient_ssp) {
                        Toast.makeText(getApplicationContext(), "Le prix de vente du SSP saisi est inférieur au prix de revient.", Toast.LENGTH_SHORT).show();
                    }*/
                }

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                String date = dateFormat.format(calendar.getTime());

                if (p_date.isEmpty() || p_date.equals(date)) {
                    Toast.makeText(getApplicationContext(), "La date d'application est vide ou incorrecte.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApagActivity.this);
                    builder.setIcon(R.drawable.ic_notification_icon);
                    builder.setTitle("Etes-vous sûr de vouloir soumettre ce prix ?");
                    builder.setPositiveButton(R.string.validate_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface diag, int id) {
                            addPriceToDB(p_id_site, p_grade1_price, p_grade2_price, p_grade3_price, p_grade4_price, p_grade5_price, p_grade6_price, p_grade7_price, p_grade8_price, p_grade9_price, p_grade10_price, p_grade11_price, p_date, p_time);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface diag, int id) {
                            diag.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        HidePriceRows();
        displayGradeRowPerid_site(sessionManager.getUsername());

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshList();
                Util.updateParametersFromDB(getApplicationContext());
                Util.updateSiteInfo(getApplicationContext());
                code_sap.setText(sessionManager.getCode_sap());
                libelle.setText(sessionManager.getDisplay_name());
                id_city.setText(sessionManager.getId_city());
                navdisplay_name.setText(sessionManager.getDisplay_name());
                nav_code_sap.setText(sessionManager.getCode_sap());
                navCity.setText(sessionManager.getId_city());
                Toast.makeText(getApplicationContext(), "Mise à jour en cours...", Toast.LENGTH_SHORT).show();
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
                switch (item.getItemId()) {
                    case R.id.apag_add:
                        gotoApagActivity();
                        break;
                    case R.id.apag_list:
                        gotoApagListActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });

        date_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ApagActivity.this, ApagActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);*/
            }
        });

        if (!Util.isMyServiceRunning(getApplicationContext())) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            service_intent.putExtra("station_name", sessionManager.getDisplay_name());
            service_intent.putExtra("message", "APAG");
            service_intent.putExtra("drawable", R.drawable.btn_img_01_);
            service_intent.putExtra("module_name", "APAG");
            startService(service_intent);
        }
    }

    protected void refreshList() {
        blocUserEventClick();
        SetGPSLocation();
        getWholePrices();
        getCurrentPrices();
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String date = dateFormat.format(calendar.getTime());
        date_add_field.setText(date);
        priceRequest.getNextCycle(new PriceRequest.getNextCycleCallback() {
            @Override
            public void onSuccess(String next_cycle) {
                String _proposalAppTime_txt = proposalAppTime_txt.getText().toString();
                proposalAppTime_txt.setText(_proposalAppTime_txt.replace("{proposalAppTime_txt}", next_cycle));
                int next_cycle_time = hour_arraylist.indexOf(next_cycle);
                time_add_spinner.setOnItemSelectedListener(ApagActivity.this);
                time_add_spinner.setSelection(next_cycle_time);
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });
        date_add_field.setEnabled(false);
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
        nav_code_sap = headerView.findViewById(R.id.nav_code_sap);
        navCity = headerView.findViewById(R.id.navCity);
        code_sap = findViewById(R.id.code_sap);
        libelle = findViewById(R.id.libelle);
        id_city = findViewById(R.id.city);
        date_today = findViewById(R.id.date_today);
        gasoil_1 = findViewById(R.id.gasoil_1);
        ssp_1 = findViewById(R.id.ssp_1);
        date_tomorrow = findViewById(R.id.date_tomorrow);
        gasoil_2 = findViewById(R.id.gasoil_2);
        ssp_2 = findViewById(R.id.ssp_2);
        LinearLayout gasoil_line1 = findViewById(R.id.gasoil_line1);
        gasoil_line2 = findViewById(R.id.gasoil_line2);
        LinearLayout ssp_line1 = findViewById(R.id.ssp_line1);
        ssp_line2 = findViewById(R.id.ssp_line2);
        proposalAppTime_txt = findViewById(R.id.proposalAppTime_txt);
        date_add_field = findViewById(R.id.date_add_field);
        time_add_spinner = findViewById(R.id.time_add_spinner);
        date_popup = findViewById(R.id.date_popup);
        cancel_btn = findViewById(R.id.cancel_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
        actual_bloc = findViewById(R.id.actual_bloc);

        grade1_row_add = findViewById(R.id.grade1_row_add);
        grade2_row_add = findViewById(R.id.grade2_row_add);
        grade3_row_add = findViewById(R.id.grade3_row_add);
        grade4_row_add = findViewById(R.id.grade4_row_add);
        grade5_row_add = findViewById(R.id.grade5_row_add);
        grade6_row_add = findViewById(R.id.grade6_row_add);
        grade7_row_add = findViewById(R.id.grade7_row_add);
        grade8_row_add = findViewById(R.id.grade8_row_add);
        grade9_row_add = findViewById(R.id.grade9_row_add);
        grade10_row_add = findViewById(R.id.grade10_row_add);
        grade11_row_add = findViewById(R.id.grade11_row_add);

        grade1_price = findViewById(R.id.grade1_price);
        grade1_price.setFilters(new InputFilter[]{new InputFilterMax((double) sessionManager.getPRICE_MAX_SSP())});
        //grade1_price.setText(sessionManager.getPriceMaxSsp());

        grade2_price = findViewById(R.id.grade2_price);
        grade2_price.setFilters(new InputFilter[]{new InputFilterMax((double) sessionManager.getPRICE_MAX_GASOIL())});
        //grade2_price.setText(sessionManager.getPriceMaxGasoil());

        grade3_price = findViewById(R.id.grade3_price);
        grade4_price = findViewById(R.id.grade4_price);
        grade5_price = findViewById(R.id.grade5_price);
        grade6_price = findViewById(R.id.grade6_price);
        grade7_price = findViewById(R.id.grade7_price);
        grade8_price = findViewById(R.id.grade8_price);
        grade9_price = findViewById(R.id.grade9_price);
        grade10_price = findViewById(R.id.grade10_price);
        grade11_price = findViewById(R.id.grade11_price);

        loading_bloc = findViewById(R.id.loading_bloc);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
    }

    private void HidePriceRows() {
        grade1_row_add.setVisibility(View.GONE);
        grade2_row_add.setVisibility(View.GONE);
        grade3_row_add.setVisibility(View.GONE);
        grade4_row_add.setVisibility(View.GONE);
        grade5_row_add.setVisibility(View.GONE);
        grade6_row_add.setVisibility(View.GONE);
        grade7_row_add.setVisibility(View.GONE);
        grade8_row_add.setVisibility(View.GONE);
        grade9_row_add.setVisibility(View.GONE);
        grade10_row_add.setVisibility(View.GONE);
        grade11_row_add.setVisibility(View.GONE);
    }

    private void displayGradeRowPerid_site(String m_username) {
        blocUserEventClick();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        MainRequest mainRequest = new MainRequest(this, queue);
        mainRequest.getSiteByUserName(m_username, new MainRequest.SiteCallback() {
            @Override
            public void onSuccess(String id_site, String id_profile, String id_animateur, String id_animateur_site, String superviseur_name, String display_name, String nb_totale, String nb_active, String nb_standby, String code_sap, String username, String password, String libelle, String tel, String email, String id_city, String id_company, String address_ip, String imei, String GradeId_list, String date_upd, String statut, Boolean hasFusion, String prix_saisis, String prix_appliques, String prix_annules) {
                if (GradeId_list != "") {
                    if (GradeId_list.contains("-")) {
                        List<String> gradeList = new ArrayList<String>(Arrays.asList(GradeId_list.split("-")));
                        if (gradeList.size() > 0) {
                            for (int i = 0; i < gradeList.size(); i++) {
                                String val = gradeList.get(i);
                                if (val.equals("1")) {
                                    grade1_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("2")) {
                                    grade2_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("3")) {
                                    grade3_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("4")) {
                                    grade4_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("5")) {
                                    grade5_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("6")) {
                                    grade6_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("7")) {
                                    grade7_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("8")) {
                                    grade8_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("9")) {
                                    grade9_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("10")) {
                                    grade10_row_add.setVisibility(View.VISIBLE);
                                }
                                if (val.equals("11")) {
                                    grade11_row_add.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (thread.isAlive())
                thread.interrupt();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "thread.interrupt() " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (thread.isAlive())
                thread.interrupt();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "thread.interrupt() " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void SetGPSLocation() {
        latitude = sessionManager.getLatitude();
        longitude = sessionManager.getLongitude();
    }

    private void addPriceToDB(String p_id_site, String p_grade1_price, String p_grade2_price, String p_grade3_price, String p_grade4_price, String p_grade5_price, String p_grade6_price, String p_grade7_price, String p_grade8_price, String p_grade9_price, String p_grade10_price, String p_grade11_price, String p_date, String p_time) {
        priceRequest.addPrice(p_id_site, p_grade1_price, p_grade2_price, p_grade3_price, p_grade4_price, p_grade5_price, p_grade6_price, p_grade7_price, p_grade8_price, p_grade9_price, p_grade10_price, p_grade11_price, p_date, p_time, latitude, longitude, spref_id_support, spref_version, spref_imei, new PriceRequest.addPriceCallback() {
            @Override
            public void onSuccess(String message) {
                if (sessionManager.getSend_sms()) {
                    Toast.makeText(getApplicationContext(), "Vous seriez notifié pour votre demande par SMS dans quelques instants.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "La fonctionnalité 'SMS' est désactivée.", Toast.LENGTH_SHORT).show();
                }
                Util.displayPopupAndGoTo(ApagActivity.this, message, 3000, true, ApagListActivity.class);
            }
            @Override
            public void onError(String message) {
                Util.errorDialog(ApagActivity.this, message);
                unblocUserEventClick();
            }
        });
    }

    public void getWholePrices() {
        priceRequest.getWholealePricesByid_site(sessionManager.getId_site(), new PriceRequest.WholealePricesByid_siteCallback() {
            @Override
            public void onSuccess(String today_date_txt, String tomorow_date_txt, String gasoil_01, String ssp_01, String gasoil_02, String ssp_02, String new_price_date1, String new_price_date2) {
                date_today.setText(today_date_txt);
                gasoil_1.setText(gasoil_01);
                ssp_1.setText(ssp_01);
                date_tomorrow.setText(tomorow_date_txt);
                if (!new_price_date1.equals(new_price_date2)) {
                    gasoil_2.setText(gasoil_02);
                    ssp_2.setText(ssp_02);
                } else {
                    gasoil_line2.setVisibility(View.GONE);
                    ssp_line2.setVisibility(View.GONE);
                }
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });
    }

    public void getCurrentPrices() {
        priceRequest.getCurrentPriceByid_site(sessionManager.getId_site(), new PriceRequest.CurrentPriceByid_siteCallback() {
            @Override
            public void onSuccess(JSONArray gradelist_prices) {
                try {
                    actual_bloc.removeAllViews();
                    for (int i = 0; i < gradelist_prices.length(); i++) {
                        JSONObject melangeObject = gradelist_prices.getJSONObject(i);
                        View gradeline = null;
                        Typeface poppins_bold = ResourcesCompat.getFont(getApplicationContext(), R.font.poppins_bold);
                        gradeline = LayoutInflater.from(getApplicationContext()).inflate(R.layout.grade_line, actual_bloc, false);
                        TextView grade_name, grade_actual_price,grade_price_unit;
                        grade_name = gradeline.findViewById(R.id.grade_name);
                        grade_actual_price = gradeline.findViewById(R.id.grade_actual_price);
                        grade_price_unit = gradeline.findViewById(R.id.price_unit);
                        grade_name.setTypeface(poppins_bold);
                        grade_name.setText(melangeObject.getString("name"));
                        grade_actual_price.setText(melangeObject.getString("price"));
                        grade_price_unit.setTypeface(poppins_bold);
                        actual_bloc.addView(gradeline);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                unblocUserEventClick();
            }
        });
    }

    /************************************************************************************************/
    /****************************************** SHOW POPUPS *****************************************/
    /************************************************************************************************/
    public void show_Diag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApagActivity.this);
        builder.setIcon(R.drawable.ic_notification_icon);
        builder.setTitle(R.string.profil_incomplet);
        builder.setPositiveButton(R.string.update_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface diag, int id) {
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface diag, int id) {
                diag.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /************************************************************************************************/
    /************************************************************************************************/
    /************************************************************************************************/

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

    private void gotoApagActivity() {
        Intent intent = new Intent(this, ApagActivity.class);
        startActivity(intent);
    }

    private void gotoApagListActivity() {
        Intent intent = new Intent(this, ApagListActivity.class);
        startActivity(intent);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onDateSet(DatePicker view, int _year, int _month, int _day) {
        day = _day;
        month = _month;
        year = _year;
        date_add_field.setText(String.format("%02d", day) + "" + String.format("%02d", (month + 1)) + "" + year);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        selected_hour = hour_arraylist.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

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

    private void blocUserEventClick() {
        loading_bloc.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unblocUserEventClick() {
        loading_bloc.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
