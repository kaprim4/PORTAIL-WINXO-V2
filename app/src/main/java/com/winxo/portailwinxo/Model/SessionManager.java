package com.winxo.portailwinxo.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class SessionManager {
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final static String PREFS_NAME = "apag_app_prefs";
    private final static int PRIVATE_MODE = 0;
    private final static String is_logged = "is_logged";
    private final static String id_user = "id_user";
    private final static String id_site = "id_site";
    private final static String code_sap = "code_sap";
    private final static String id_city = "id_city";
    private final static String hasFusion = "hasFusion";
    private final static String username = "username";
    private final static String email = "email";
    private final static String GradeId_list = "GradeId_list";
    private final static String profile_pic = "image_url";
    private final static String id_profile = "id_profile";
    private final static String id_animateur_site = "id_animateur_site";
    private final static String superviseur_name = "superviseur_name";
    private final static String display_name = "display_name";
    private final static String nb_totale = "nb_totale";
    private final static String nb_active = "nb_active";
    private final static String nb_standby = "nb_standby";
    private final static String prix_saisis = "prix_saisis";
    private final static String prix_appliques = "prix_appliques";
    private final static String prix_annules = "prix_annules";
    private final static String PRICE_MIN_SSP = "PRICE_MIN_SSP";
    private final static String PRICE_MAX_SSP = "PRICE_MAX_SSP";
    private final static String PRICE_MIN_GASOIL = "PRICE_MIN_GASOIL";
    private final static String PRICE_MAX_GASOIL = "PRICE_MAX_GASOIL";
    private final static String PRICE_MIN_2T = "PRICE_MIN_2T";
    private final static String PRICE_MAX_2T = "PRICE_MAX_2T";
    private final static String PRICE_STEP = "PRICE_STEP";
    private final static String INPUT_INTO_DAY = "INPUT_INTO_DAY";
    private final static String DAY_LIMIT_FOR_LISTING = "DAY_LIMIT_FOR_LISTING";
    private final static String INTERVAL_BY_SECS = "INTERVAL_BY_SECS";
    private final static String ADD_COUNT_PER_DAY = "ADD_COUNT_PER_DAY";
    private final static String ADD_INTERVAL_PER_DAY = "ADD_INTERVAL_PER_DAY";
    private final static String LOGIN_ATTEMPT = "LOGIN_ATTEMPT";
    private final static String send_mail = "send_mail";
    private final static String send_sms = "send_sms";
    private final static String pw_checksum = "pw_checksum";
    private final static String latitude = "latitude";
    private final static String longitude = "longitude";

    private final static String AVIS_MAINTENANCE = "AVIS_MAINTENANCE";
    private final static String MAINTENANCE = "MAINTENANCE";
    private final static String ENABLE_APAG = "ENABLE_APAG";
    private final static String ENABLE_ORDERS = "ENABLE_ORDERS";
    private final static String ENABLE_CLAIM = "ENABLE_CLAIM";

    private final static String ORDER_ADD_COUNT_PER_DAY = "ORDER_ADD_COUNT_PER_DAY";
    private final static String ORDER_SUSPEND_DAY_FROM = "ORDER_SUSPEND_DAY_FROM";
    private final static String ORDER_SUSPEND_DAY_TO = "ORDER_SUSPEND_DAY_TO";
    private final static String ORDER_ADD_TIME_FROM = "ORDER_ADD_TIME_FROM";
    private final static String ORDER_ADD_TIME_TO = "ORDER_ADD_TIME_TO";
    private final static String ORDER_CANCEL_DELAY = "ORDER_CANCEL_DELAY";
    private final static String ORDER_DATE_SHIP_DAY_UPON = "ORDER_DATE_SHIP_DAY_UPON";
    private final static String ORDER_CAR_MIN = "ORDER_CAR_MIN";
    private final static String ORDER_CAR_MAX = "ORDER_CAR_MAX";
    private final static String ORDER_CAR_UNIT = "ORDER_CAR_UNIT";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean getIs_logged() {
        return prefs.getBoolean(is_logged, false);
    }

    public String getId_user() {
        return prefs.getString(id_user, null);
    }

    public String getId_site() {
        return prefs.getString(id_site, null);
    }

    public String getCode_sap() {
        return prefs.getString(code_sap, null);
    }

    public String getId_city() {
        return prefs.getString(id_city, null);
    }

    public boolean getHasFusion() {
        return prefs.getBoolean(hasFusion, false);
    }

    public String getUsername() {
        return prefs.getString(username, null);
    }

    public String getEmail() {
        return prefs.getString(email, null);
    }

    public String getGradeId_list() {
        return prefs.getString(GradeId_list, null);
    }

    public String getProfile_pic() {
        return prefs.getString(profile_pic, null);
    }

    public String getDisplay_name() {
        return prefs.getString(display_name, null);
    }

    public String getId_profile() {
        return prefs.getString(id_profile, null);
    }

    public String getId_animateur_site() {
        return prefs.getString(id_animateur_site, null);
    }

    public String getSuperviseur_name() {
        return prefs.getString(superviseur_name, null);
    }

    public String getNb_totale() {
        return prefs.getString(nb_totale, null);
    }

    public String getNb_active() {
        return prefs.getString(nb_active, null);
    }

    public String getNb_standby() {
        return prefs.getString(nb_standby, null);
    }

    public String getPrix_saisis() {
        return prefs.getString(prix_saisis, null);
    }

    public String getPrix_appliques() {
        return prefs.getString(prix_appliques, null);
    }

    public String getPrix_annules() {
        return prefs.getString(prix_annules, null);
    }

    public float getPRICE_MIN_SSP() {
        return prefs.getFloat(PRICE_MIN_SSP, 0);
    }

    public float getPRICE_MAX_SSP() {
        return prefs.getFloat(PRICE_MAX_SSP, 0);
    }

    public float getPRICE_MIN_GASOIL() {
        return prefs.getFloat(PRICE_MIN_GASOIL, 0);
    }

    public float getPRICE_MAX_GASOIL() {
        return prefs.getFloat(PRICE_MAX_GASOIL, 0);
    }

    public float getPRICE_MIN_2T() {
        return prefs.getFloat(PRICE_MIN_2T, 0);
    }

    public float getPRICE_MAX_2T() {
        return prefs.getFloat(PRICE_MAX_2T, 0);
    }

    public float getPRICE_STEP() {
        return prefs.getFloat(PRICE_STEP, 0);
    }

    public float getINPUT_INTO_DAY() {
        return prefs.getFloat(INPUT_INTO_DAY, 0);
    }

    public int getDAY_LIMIT_FOR_LISTING() {
        return prefs.getInt(DAY_LIMIT_FOR_LISTING, 3);
    }

    public String getINTERVAL_BY_SECS() {
        return prefs.getString(INTERVAL_BY_SECS, null);
    }

    public String getADD_COUNT_PER_DAY() {
        return prefs.getString(ADD_COUNT_PER_DAY, null);
    }

    public String getADD_INTERVAL_PER_DAY() {
        return prefs.getString(ADD_INTERVAL_PER_DAY, null);
    }

    public String getLOGIN_ATTEMPT() {
        return prefs.getString(LOGIN_ATTEMPT, null);
    }

    public boolean getSend_mail() {
        return prefs.getBoolean(send_mail, false);
    }

    public boolean getSend_sms() {
        return prefs.getBoolean(send_sms, false);
    }

    public String getPw_checksum() {
        return prefs.getString(pw_checksum, null);
    }

    public String getLatitude() {
        return prefs.getString(latitude, "0");
    }

    public String getLongitude() {
        return prefs.getString(longitude, "0");
    }

    public boolean getAVIS_MAINTENANCE() {
        return prefs.getBoolean(AVIS_MAINTENANCE, false);
    }

    public boolean getMAINTENANCE() {
        return prefs.getBoolean(MAINTENANCE, false);
    }

    public boolean getENABLE_APAG() {
        return prefs.getBoolean(ENABLE_APAG, false);
    }

    public boolean getENABLE_ORDERS() {
        return prefs.getBoolean(ENABLE_ORDERS, false);
    }

    public boolean getENABLE_CLAIM() {
        return prefs.getBoolean(ENABLE_CLAIM, false);
    }

    public int getORDER_ADD_COUNT_PER_DAY() {
        return prefs.getInt(ORDER_ADD_COUNT_PER_DAY, 0);
    }

    public String getORDER_SUSPEND_DAY_FROM() {
        return prefs.getString(ORDER_SUSPEND_DAY_FROM, "");
    }

    public String getORDER_SUSPEND_DAY_TO() {
        return prefs.getString(ORDER_SUSPEND_DAY_TO, "");
    }

    public String getORDER_ADD_TIME_FROM() {
        return prefs.getString(ORDER_ADD_TIME_FROM, "");
    }

    public String getORDER_ADD_TIME_TO() {
        return prefs.getString(ORDER_ADD_TIME_TO, "");
    }

    public String getORDER_CANCEL_DELAY() {
        return prefs.getString(ORDER_CANCEL_DELAY, "");
    }

    public int getORDER_DATE_SHIP_DAY_UPON() {
        return prefs.getInt(ORDER_DATE_SHIP_DAY_UPON, 0);
    }

    public int getORDER_CAR_MIN() {
        return prefs.getInt(ORDER_CAR_MIN, 0);
    }

    public int getORDER_CAR_MAX() {
        return prefs.getInt(ORDER_CAR_MAX, 0);
    }

    public String getORDER_CAR_UNIT() {
        return prefs.getString(ORDER_CAR_UNIT, "");
    }

    public void insertUser(@NotNull Site site) {
        editor.putBoolean(is_logged, true);
        editor.putString(id_site, site.getId_site());
        editor.putString(code_sap, site.getCode_sap());
        editor.putString(username, site.getUsername());
        editor.putString(id_city, site.getId_city());
        editor.putBoolean(hasFusion, site.getHasFusion());
        editor.putString(email, site.getEmail());
        editor.putString(GradeId_list, site.getGradeId_list());
        editor.putString(profile_pic, "");
        editor.putString(id_profile, site.getId_profile());
        editor.putString(id_animateur_site, site.getId_animateur_site());
        editor.putString(superviseur_name, site.getSuperviseur_name());
        editor.putString(display_name, site.getDisplay_name());
        editor.putString(nb_totale, site.getNb_totale());
        editor.putString(nb_active, site.getNb_active());
        editor.putString(nb_standby, site.getNb_standby());
        editor.commit();
    }

    public void updateCounter(@NonNull Statistic statistic) {
        editor.putString(prix_saisis, statistic.getPrix_saisis());
        editor.putString(prix_appliques, statistic.getPrix_appliques());
        editor.putString(prix_annules, statistic.getPrix_annules());
        editor.commit();
    }

    public void updateParams(@NonNull Parameters parameter) {
        editor.putFloat(PRICE_MIN_SSP, parameter.getPRICE_MIN_SSP());
        editor.putFloat(PRICE_MAX_SSP, parameter.getPRICE_MAX_SSP());
        editor.putFloat(PRICE_MIN_GASOIL, parameter.getPRICE_MIN_GASOIL());
        editor.putFloat(PRICE_MAX_GASOIL, parameter.getPRICE_MAX_GASOIL());
        editor.putFloat(PRICE_MIN_2T, parameter.getPRICE_MIN_2T());
        editor.putFloat(PRICE_MAX_2T, parameter.getPRICE_MAX_2T());
        editor.putFloat(PRICE_STEP, parameter.getPRICE_STEP());
        editor.putInt(INPUT_INTO_DAY, parameter.getINPUT_INTO_DAY());
        editor.putInt(DAY_LIMIT_FOR_LISTING, parameter.getDAY_LIMIT_FOR_LISTING());
        editor.putInt(INTERVAL_BY_SECS, parameter.getINTERVAL_BY_SECS());
        editor.putInt(ADD_COUNT_PER_DAY, parameter.getADD_COUNT_PER_DAY());
        editor.putInt(ADD_INTERVAL_PER_DAY, parameter.getADD_INTERVAL_PER_DAY());
        editor.putInt(LOGIN_ATTEMPT, parameter.getLOGIN_ATTEMPT());
        editor.putBoolean(send_mail, parameter.isSEND_MAIL());
        editor.putBoolean(send_sms, parameter.isSEND_SMS());
        editor.putString(pw_checksum, parameter.getPW_CHECKSUM());
        editor.putBoolean(AVIS_MAINTENANCE, parameter.isAVIS_MAINTENANCE());
        editor.putBoolean(MAINTENANCE, parameter.isMAINTENANCE());
        editor.putBoolean(ENABLE_APAG, parameter.isENABLE_APAG());
        editor.putBoolean(ENABLE_ORDERS, parameter.isENABLE_ORDERS());
        editor.putBoolean(ENABLE_CLAIM, parameter.isENABLE_CLAIM());
        editor.putInt(ORDER_ADD_COUNT_PER_DAY, parameter.getORDER_ADD_COUNT_PER_DAY());
        editor.putString(ORDER_SUSPEND_DAY_FROM, parameter.getORDER_SUSPEND_DAY_FROM());
        editor.putString(ORDER_SUSPEND_DAY_TO, parameter.getORDER_SUSPEND_DAY_TO());
        editor.putString(ORDER_ADD_TIME_FROM, parameter.getORDER_ADD_TIME_FROM());
        editor.putString(ORDER_ADD_TIME_TO, parameter.getORDER_ADD_TIME_TO());
        editor.putString(ORDER_CANCEL_DELAY, parameter.getORDER_CANCEL_DELAY());
        editor.putInt(ORDER_DATE_SHIP_DAY_UPON, parameter.getORDER_DATE_SHIP_DAY_UPON());
        editor.putInt(ORDER_CAR_MIN, parameter.getORDER_CAR_MIN());
        editor.putInt(ORDER_CAR_MAX, parameter.getORDER_CAR_MAX());
        editor.putString(ORDER_CAR_UNIT, parameter.getORDER_CAR_UNIT());
        editor.commit();
    }

    public void updateSiteInformations(String _code_sap, String _username, String _display_name, String _id_city, Boolean _hasFusion) {
        editor.putString(code_sap, _code_sap);
        editor.putString(username, _username);
        editor.putString(display_name, _display_name);
        editor.putString(id_city, _id_city);
        editor.putBoolean(hasFusion, _hasFusion);
        editor.commit();
    }

    public void updateSiteHasFusion(Boolean _hasFusion) {
        editor.putBoolean(hasFusion, _hasFusion);
        editor.commit();
    }

    public void updateIdUser(String _id_user) {
        editor.putString(id_user, _id_user);
        editor.commit();
    }

    public void updateUser(String var, String value) {
        editor.putString(var, value);
        editor.commit();
    }

    public void updateGPS(String _latitude, String _longitude) {
        editor.putString(latitude, _latitude);
        editor.putString(longitude, _longitude);
        editor.commit();
    }

    public void logOut() {
        editor.clear().commit();
    }
}