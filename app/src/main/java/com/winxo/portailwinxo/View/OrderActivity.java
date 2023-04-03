package com.winxo.portailwinxo.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.vicmikhailau.maskededittext.MaskedEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.VolleySingleton;
import com.winxo.portailwinxo.R;
import com.winxo.portailwinxo.Requests.OrderRequest;
import com.winxo.portailwinxo.Service.PortailWinxoService;
import com.winxo.portailwinxo.Utilities.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private CircleImageView avatar;
    private SessionManager sessionManager;
    private TextView navdisplay_name, nav_code_sap, navCity, code_sap, libelle, id_city, btn_cgv;
    private LinearLayout grade2_row_add, grade1_row_add;
    private EditText grade2_price, grade1_price, sum_qty, date_shipping_field;
    private MaskedEditText date_add_field;
    private ImageView date_popup;
    public int day, month, year;
    private Button cancel_btn, confirm_btn, refresh_btn;
    private BottomNavigationView b_nav_view;
    private FloatingActionButton home_btn;
    private RequestQueue queue;
    private OrderRequest orderRequest;
    public String latitude = "", longitude = "", spref_id_support, spref_version, spref_imei;
    private LinearLayout loading_bloc;
    private boolean cgv_accepted = false;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        sessionManager = new SessionManager(this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        orderRequest = new OrderRequest(this, queue);

        initialiseVars();
        blocUserEventClick();
        refresh_list();
        init_fields();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (sessionManager.getIs_logged()) {
            code_sap.setText(sessionManager.getCode_sap());
            libelle.setText(sessionManager.getDisplay_name());
            id_city.setText(sessionManager.getId_city());
            navdisplay_name.setText(sessionManager.getDisplay_name());
            nav_code_sap.setText(sessionManager.getCode_sap());
            navCity.setText(sessionManager.getId_city());
        }

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(), true);
        spref_id_support = preferences.getString("id_support");
        spref_version = preferences.getString("version");
        spref_imei = preferences.getString("imei");

        date_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        OrderActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);

                // Setting Min Date to today date
                Calendar min_date_c = Calendar.getInstance();
                min_date_c.add(Calendar.DAY_OF_MONTH, 1);
                dpd.setMinDate(min_date_c);

                // Setting Max Date to next 2 years
                Calendar max_date_c = Calendar.getInstance();
                max_date_c.add(Calendar.YEAR, 2);
                dpd.setMaxDate(max_date_c);

                //Disable all SUNDAYS between Min and Max Dates
                for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                    int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY) {
                        Calendar[] disabledDays =  new Calendar[1];
                        disabledDays[0] = loopdate;
                        dpd.setDisabledDays(disabledDays);
                    }
                }
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh_list();
                init_fields();
                code_sap.setText(sessionManager.getCode_sap());
                libelle.setText(sessionManager.getDisplay_name());
                id_city.setText(sessionManager.getId_city());
                navdisplay_name.setText(sessionManager.getDisplay_name());
                nav_code_sap.setText(sessionManager.getCode_sap());
                navCity.setText(sessionManager.getId_city());
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
                    case R.id.order_add:
                        gotoOrderThisMonthActivity();
                        break;
                    case R.id.order_list:
                        gotoOrderListActivity();
                        break;
                }
                item.setEnabled(true);
                item.setChecked(true);
                return false;
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init_fields();
            }
        });

        grade1_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int g2 = 0;
                    int val_input = Integer.parseInt(s.toString());
                    if (!grade2_price.getText().toString().trim().isEmpty())
                        g2 = Integer.parseInt(grade2_price.getText().toString());
                    int sum = g2 + val_input;
                    if (sum > sessionManager.getORDER_CAR_MAX()) {
                        sum = sessionManager.getORDER_CAR_MAX();
                        int new_val = val_input - 1;
                        grade1_price.setText(String.valueOf(new_val));
                    }
                    sum_qty.setText(String.valueOf(sum));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        grade2_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int g2 = 0;
                    int val_input = Integer.parseInt(s.toString());
                    if (!grade1_price.getText().toString().trim().isEmpty())
                        g2 = Integer.parseInt(grade1_price.getText().toString());
                    int sum = g2 + val_input;
                    if (sum > sessionManager.getORDER_CAR_MAX()) {
                        sum = sessionManager.getORDER_CAR_MAX();
                        int new_val = val_input - 1;
                        grade2_price.setText(String.valueOf(new_val));
                    }
                    sum_qty.setText(String.valueOf(sum));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String p_id_site = sessionManager.getId_site();
                final String p_grade1_price = grade1_price.getText().toString().trim();
                final String p_grade2_price = grade2_price.getText().toString().trim();
                final String qty = sum_qty.getText().toString().trim();
                final String p_date = Objects.requireNonNull(date_add_field.getText()).toString().trim();

                if (qty.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Veuillez saisir une quantité d'ordre valide.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setIcon(R.drawable.ic_notification_icon);
                    builder.setTitle("Etes-vous sûr de vouloir soumettre cette commande ?");
                    builder.setPositiveButton(R.string.validate_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface diag, int id) {


                            addOrderToDB(p_id_site, p_grade1_price, p_grade2_price, p_date);
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

        /*confirm_btn.setEnabled(false);
        confirm_btn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhiteGrey));
        confirm_btn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGrey));*/

        btn_cgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCGVActivity();
            }
        });

        if (!Util.isMyServiceRunning(getApplicationContext())) {
            Intent service_intent = new Intent(this, PortailWinxoService.class);
            service_intent.putExtra("station_name", sessionManager.getDisplay_name());
            service_intent.putExtra("message", "Commandes");
            service_intent.putExtra("drawable", R.drawable.btn_img_02_);
            service_intent.putExtra("module_name", "ORDER");
            startService(service_intent);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void init_fields() {
        grade1_price.setText("0");
        grade2_price.setText("0");
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinutes = c.get(Calendar.MINUTE);
        int mSeconds = c.get(Calendar.SECOND);
        String today_date = String.format("%02d", mDay) + "/" + String.format("%02d", (mMonth + 1)) + "/" + mYear + " " + mHour + ":" + mMinutes + ":" + mSeconds;
        String calendar_date = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "/" + calendar.get(Calendar.YEAR) + " 12:00:00";
        calendar.add(Calendar.DAY_OF_MONTH, sessionManager.getORDER_DATE_SHIP_DAY_UPON());
        @SuppressLint("SimpleDateFormat") long hours = Util.getDateDiff(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"), calendar_date, today_date);
        if (hours > 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        date_shipping_field.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "/" + calendar.get(Calendar.YEAR));

        // Setting Min Date to tomorrow date
        Calendar min_date_c = Calendar.getInstance();
        min_date_c.add(Calendar.DAY_OF_MONTH, 1);
        date_add_field.setText(String.format("%02d", min_date_c.get(Calendar.DAY_OF_MONTH)) + "" + String.format("%02d", (min_date_c.get(Calendar.MONTH) + 1)) + "" + min_date_c.get(Calendar.YEAR));

        date_shipping_field.setEnabled(false);
        date_add_field.setEnabled(false);
        sum_qty.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void refresh_list() {
        Toast.makeText(getApplicationContext(), "Mise à jour en cours...", Toast.LENGTH_SHORT).show();
        Util.updateParametersFromDB(getApplicationContext());
        Util.updateSiteInfo(getApplicationContext());
        SetGPSLocation();
        unblocUserEventClick();
    }

    private void addOrderToDB(String p_id_site, String p_grade1_price, String p_grade2_price, String p_date) {
        orderRequest.addOrder(p_id_site, sessionManager.getId_user(), p_grade1_price, p_grade2_price, p_date, latitude, longitude, spref_id_support, spref_version, spref_imei, new OrderRequest.addOrderCallback() {
            @Override
            public void onSuccess(String message) {
                Util.correctDialog(OrderActivity.this, message);
                init_fields();
                if (sessionManager.getSend_sms()) {
                    Toast.makeText(getApplicationContext(), "Vous seriez notifié pour votre demande par SMS dans quelques instants.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Fonctionnalité 'SMS' est désactivée.", Toast.LENGTH_SHORT).show();
                }
                unblocUserEventClick();
            }

            @Override
            public void onError(String message) {
                Util.errorDialog(OrderActivity.this, message);
                unblocUserEventClick();
            }
        });
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
        grade2_row_add = findViewById(R.id.grade2_row_add);
        grade1_row_add = findViewById(R.id.grade1_row_add);
        grade2_price = findViewById(R.id.grade2_price);
        grade1_price = findViewById(R.id.grade1_price);
        date_add_field = findViewById(R.id.date_add_field);
        date_popup = findViewById(R.id.date_popup);
        sum_qty = findViewById(R.id.sum_qty);
        date_shipping_field = findViewById(R.id.date_shipping_field);
        cancel_btn = findViewById(R.id.cancel_btn);
        confirm_btn = findViewById(R.id.confirm_btn);
        b_nav_view = findViewById(R.id.b_nav_view);
        home_btn = findViewById(R.id.home_btn);
        loading_bloc = findViewById(R.id.loading_bloc);
        btn_cgv = findViewById(R.id.btn_cgv);
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

    private void gotoOrderThisMonthActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }

    private void gotoOrderListActivity() {
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
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

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date_add_field.setText(String.format("%02d", dayOfMonth) + "" + String.format("%02d", (monthOfYear + 1)) + "" + year);
    }

    @SuppressLint("SetTextI18n")
    private void gotoCGVActivity() {
        final ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.cgv, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        TextView order_add_count_per_day_txt = dialogView.findViewById(R.id.order_add_count_per_day_txt);
        TextView order_add_time_txt = dialogView.findViewById(R.id.order_add_time_txt);
        TextView order_add_holiday_txt = dialogView.findViewById(R.id.order_add_holiday_txt);
        TextView order_add_shipping_date_txt = dialogView.findViewById(R.id.order_add_shipping_date_txt);
        TextView order_cancel_delay_txt = dialogView.findViewById(R.id.order_cancel_delay_txt);
        Button close_btn = dialogView.findViewById(R.id.close_btn);

        if (sessionManager.getORDER_ADD_COUNT_PER_DAY() == 1)
            order_add_count_per_day_txt.setText(Html.fromHtml("- Vous avez droit à " + sessionManager.getORDER_ADD_COUNT_PER_DAY() + " commande livrée par jour avec une quantité Maximum à commander de: " + sessionManager.getORDER_CAR_MAX() + " M<sup>3</sup>."));
        else if (sessionManager.getORDER_ADD_COUNT_PER_DAY() > 1)
            order_add_count_per_day_txt.setText(Html.fromHtml("- Vous avez droit à " + sessionManager.getORDER_ADD_COUNT_PER_DAY() + " commandes livrées par jour."));
        order_add_time_txt.setText(Html.fromHtml("- Toute commande saisie après le " + sessionManager.getORDER_SUSPEND_DAY_FROM() + " ne sera prise en considération qu'après le " + sessionManager.getORDER_SUSPEND_DAY_TO() + "."));
        order_add_holiday_txt.setText(Html.fromHtml("- Toute commande saisie un jour férié, elle ne sera prise en considération qu'après le 1er jour ouvré à 12H00."));
        order_add_shipping_date_txt.setText("- Toute commande saisie aura une date de livraison contractuelle (de minimum 48H ouvrable).");
        order_cancel_delay_txt.setText(Html.fromHtml("- Vous avez un délai de " + sessionManager.getORDER_CANCEL_DELAY() + " minutes pour annuler et abondonner une commande."));
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void SetGPSLocation() {
        latitude = sessionManager.getLatitude();
        longitude = sessionManager.getLongitude();
    }
}