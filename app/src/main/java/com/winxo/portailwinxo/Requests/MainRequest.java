package com.winxo.portailwinxo.Requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.SecurePreferences;
import com.winxo.portailwinxo.Model.SessionManager;
import com.winxo.portailwinxo.Model.Station;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainRequest {

    private final RequestQueue queue;
    private final Context context;
    private SecurePreferences preferences;
    private SessionManager sessionManager;

    public MainRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
        this.preferences = new SecurePreferences(context, true);
        this.sessionManager = new SessionManager(context);
    }

    public void getParameterList(final ParametersCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");

                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        JSONObject data = api_rep.getJSONObject("data");
                        float PRICE_MIN_SSP = Float.parseFloat(data.getString("PRICE_MIN_SSP"));
                        float PRICE_MAX_SSP = Float.parseFloat(data.getString("PRICE_MAX_SSP"));
                        float PRICE_MIN_GASOIL = Float.parseFloat(data.getString("PRICE_MIN_GASOIL"));
                        float PRICE_MAX_GASOIL = Float.parseFloat(data.getString("PRICE_MAX_GASOIL"));
                        float PRICE_MIN_2T = Float.parseFloat(data.getString("PRICE_MIN_2T"));
                        float PRICE_MAX_2T = Float.parseFloat(data.getString("PRICE_MAX_2T"));
                        float PRICE_STEP = Float.parseFloat(data.getString("PRICE_STEP"));
                        int INPUT_INTO_DAY = data.getInt("INPUT_INTO_DAY");
                        int DAY_LIMIT_FOR_LISTING = data.getInt("DAY_LIMIT_FOR_LISTING");
                        int INTERVAL_BY_SECS = data.getInt("INTERVAL_BY_SECS");
                        int ADD_COUNT_PER_DAY = data.getInt("ADD_COUNT_PER_DAY");
                        int ADD_INTERVAL_PER_DAY = data.getInt("ADD_INTERVAL_PER_DAY");
                        int LOGIN_ATTEMPT = data.getInt("LOGIN_ATTEMPT");
                        boolean SEND_MAIL = data.getBoolean("SEND_MAIL");
                        boolean SEND_SMS = data.getBoolean("SEND_SMS");
                        String PW_CHECKSUM = data.getString("PW_CHECKSUM");
                        boolean AVIS_MAINTENANCE = data.getBoolean("AVIS_MAINTENANCE");
                        boolean MAINTENANCE = data.getBoolean("MAINTENANCE");
                        boolean ENABLE_APAG = data.getBoolean("ENABLE_APAG");
                        boolean ENABLE_ORDERS = data.getBoolean("ENABLE_ORDERS");
                        boolean ENABLE_CLAIM = data.getBoolean("ENABLE_CLAIM");
                        int ORDER_ADD_COUNT_PER_DAY = data.getInt("ORDER_ADD_COUNT_PER_DAY");
                        String ORDER_SUSPEND_DAY_FROM = data.getString("ORDER_SUSPEND_DAY_FROM");
                        String ORDER_SUSPEND_DAY_TO = data.getString("ORDER_SUSPEND_DAY_TO");
                        String ORDER_ADD_TIME_FROM = data.getString("ORDER_ADD_TIME_FROM");
                        String ORDER_ADD_TIME_TO = data.getString("ORDER_ADD_TIME_TO");
                        String ORDER_CANCEL_DELAY = data.getString("ORDER_CANCEL_DELAY");
                        int ORDER_DATE_SHIP_DAY_UPON = data.getInt("ORDER_DATE_SHIP_DAY_UPON");
                        int ORDER_CAR_MIN = data.getInt("ORDER_CAR_MIN");
                        int ORDER_CAR_MAX = data.getInt("ORDER_CAR_MAX");
                        String ORDER_CAR_UNIT = data.getString("ORDER_CAR_UNIT");

                        callback.onSuccess(PRICE_MIN_SSP, PRICE_MAX_SSP, PRICE_MIN_GASOIL, PRICE_MAX_GASOIL, PRICE_MIN_2T, PRICE_MAX_2T, PRICE_STEP, INPUT_INTO_DAY, DAY_LIMIT_FOR_LISTING, INTERVAL_BY_SECS, ADD_COUNT_PER_DAY, ADD_INTERVAL_PER_DAY, LOGIN_ATTEMPT, SEND_MAIL, SEND_SMS, PW_CHECKSUM, AVIS_MAINTENANCE, MAINTENANCE, ENABLE_APAG, ENABLE_ORDERS, ENABLE_CLAIM, ORDER_ADD_COUNT_PER_DAY, ORDER_SUSPEND_DAY_FROM, ORDER_SUSPEND_DAY_TO, ORDER_ADD_TIME_FROM, ORDER_ADD_TIME_TO, ORDER_CANCEL_DELAY, ORDER_DATE_SHIP_DAY_UPON, ORDER_CAR_MIN, ORDER_CAR_MAX, ORDER_CAR_UNIT);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("Une erreur s'est produite: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", "getParameters");
                params.put("token", Constants.API_TOKEN);
                return params;
            }
        };
        queue.add(request);
    }

    public interface ParametersCallback {
        void onSuccess(float PRICE_MIN_SSP, float PRICE_MAX_SSP, float PRICE_MIN_GASOIL, float PRICE_MAX_GASOIL, float PRICE_MIN_2T, float PRICE_MAX_2T, float PRICE_STEP, int INPUT_INTO_DAY, int DAY_LIMIT_FOR_LISTING, int INTERVAL_BY_SECS, int ADD_COUNT_PER_DAY, int ADD_INTERVAL_PER_DAY, int LOGIN_ATTEMPT, boolean SEND_MAIL, boolean SEND_SMS, String PW_CHECKSUM, boolean AVIS_MAINTENANCE, boolean MAINTENANCE, boolean ENABLE_APAG, boolean ENABLE_ORDERS, boolean ENABLE_CLAIM, int ORDER_ADD_COUNT_PER_DAY, String ORDER_SUSPEND_DAY_FROM, String ORDER_SUSPEND_DAY_TO, String ORDER_ADD_TIME_FROM, String ORDER_ADD_TIME_TO, String ORDER_CANCEL_DELAY, int ORDER_DATE_SHIP_DAY_UPON, int ORDER_CAR_MIN, int ORDER_CAR_MAX, String ORDER_CAR_UNIT);
        void onError(String message);
    }


    public void login(final String username, final String password, final String fingerprint, final LoginCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("login", "response: " + response);
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        JSONObject data = api_rep.getJSONObject("data");
                        String username = data.getString("username");
                        String id_user = data.getString("id_user");
                        String password = data.getString("password");
                        String build_version = data.getString("build_version");
                        String hashed_pws_db = data.getString("hashed_pws_db");
                        String hashed_pws = data.getString("hashed_pws");
                        callback.onSuccess(id_user, username, password, build_version, hashed_pws_db, hashed_pws);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("param", "login");
                map.put("token", Constants.API_TOKEN);
                map.put("username", username);
                map.put("password", password);
                map.put("fingerprint", fingerprint);
                map.put("build_version", BuildConfig.VERSION_NAME);
                map.put("latitude_user", sessionManager.getLatitude());
                map.put("longitude_user", sessionManager.getLongitude());
                map.put("version_name", preferences.getString("version"));
                map.put("MacWlanAddress", preferences.getString("MacWlanAddress"));
                map.put("MacEthAddress", preferences.getString("MacEthAddress"));
                map.put("ipAddressV4", preferences.getString("ipAddressV4"));
                map.put("ipAddressV6", preferences.getString("ipAddressV6"));
                map.put("imei", preferences.getString("imei"));
                map.put("id_support", preferences.getString("id_support"));
                return map;
            }
        };
        queue.add(request);
    }

    public interface LoginCallback {
        void onSuccess(String id_user, String username, String password, String build_version, String hashed_pws_db, String hashed_pws);
        void onError(String message);
    }


    public void getSiteByUserName(final String username, final SiteCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        JSONObject json = api_rep.getJSONObject("data");
                        String _id_site = json.getString("id_site");
                        String _id_profile = json.getString("id_profile");
                        String _id_animateur = json.getString("id_animateur");
                        String _id_animateur_site = json.getString("id_animateur_site");
                        String _superviseur_name = json.getString("superviseur_name");
                        String _display_name = json.getString("display_name");
                        String _nb_totale = json.getString("nb_totale");
                        String _nb_active = json.getString("nb_active");
                        String _nb_standby = json.getString("nb_standby");
                        String _code_sap = json.getString("code_sap");
                        String _username = json.getString("username");
                        String _password = json.getString("password");
                        String _libelle = json.getString("libelle");
                        String _tel = json.getString("tel");
                        String _email = json.getString("email");
                        String _id_city = json.getString("id_city");
                        String _id_company = json.getString("id_company");
                        String _address_ip = json.getString("address_ip");
                        String _imei = json.getString("imei");
                        String _GradeId_list = json.getString("GradeId_list");
                        String _date_upd = json.getString("date_upd");
                        String _status = json.getString("status");
                        Boolean _hasFusion = json.getBoolean("hasFusion");
                        String _prix_saisis = json.getString("prix_saisis");
                        String _prix_appliques = json.getString("prix_appliques");
                        String _prix_annules = json.getString("prix_annules");
                        callback.onSuccess(_id_site, _id_profile, _id_animateur, _id_animateur_site, _superviseur_name, _display_name, _nb_totale, _nb_active, _nb_standby, _code_sap, _username, _password, _libelle, _tel, _email, _id_city, _id_company, _address_ip, _imei, _GradeId_list, _date_upd, _status, _hasFusion, _prix_saisis, _prix_appliques, _prix_annules);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("param", "getSiteByUserName");
                map.put("token", Constants.API_TOKEN);
                map.put("username", username);
                return map;
            }
        };
        queue.add(request);
    }

    public interface SiteCallback {
        void onSuccess(String _id_site, String _id_profile, String _id_animateur, String _id_animateur_site, String _superviseur_name, String _display_name, String _nb_totale, String _nb_active, String _nb_standby, String _code_sap, String _username, String _password, String _libelle, String _tel, String _email, String _id_city, String _id_company, String _address_ip, String _imei, String _GradeId_list, String _date_upd, String _status, Boolean _hasFusion, String _prix_saisis, String _prix_appliques, String _prix_annules);
        void onError(String message);
    }



    public void ResetPassword(final String EmailAddress, final String tel, final ResetPasswordCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    JSONObject data = api_rep.getJSONObject("data");
                    String message = data.getString("message");
                    String hashed_pws = data.getString("hashed_pws");
                    String new_password = data.getString("new_password");
                    boolean send_sms_param = api_rep.getBoolean("send_sms_param");
                    boolean sms_sent = api_rep.getBoolean("sms_sent");
                    boolean send_mail_param = api_rep.getBoolean("send_mail_param");
                    boolean mail_sent = api_rep.getBoolean("mail_sent");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        callback.onSuccess(message);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("Une erreur s'est produite: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param", "ResetPassword");
                params.put("token", Constants.API_TOKEN);
                params.put("EmailAddress", EmailAddress);
                params.put("tel", tel);
                return params;
            }
        };
        queue.add(request);
    }

    public interface ResetPasswordCallback {
        void onSuccess(String message);
        void onError(String message);
    }


    public void getStationList(final String latitude, final String longitude, final StationCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("param", "getParameters");
        params.put("token", Constants.API_TOKEN);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        JSONObject json = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.WEB_SERVICE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject _station) {
                ArrayList<Station> stations = new ArrayList<>();
                if (_station != null) {
                    JSONArray list = null;
                    try {
                        list = _station.getJSONArray("list");
                        if (list.length() > 0) {
                            try {
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject stationObject = list.getJSONObject(i);
                                    String codestation = stationObject.getString("codestation");
                                    String namefr = stationObject.getString("namefr");
                                    String address = stationObject.getString("address");
                                    String distance = stationObject.getString("distance");
                                    String ville = stationObject.getString("ville");
                                    /*String id = stationObject.getString("id");
                                    String latitude = stationObject.getString("latitude");
                                    String longitude = stationObject.getString("longitude");
                                    String address_ar = stationObject.getString("address_ar");
                                    String openingtime = stationObject.getString("openingtime");
                                    String closingtime = stationObject.getString("closingtime");
                                    String datebeginservice = stationObject.getString("datebeginservice");
                                    String namear = stationObject.getString("namear");
                                    String id_ville = stationObject.getString("id_ville");
                                    String id_service = stationObject.getString("id_service");
                                    String id_produit = stationObject.getString("id_produit");
                                    String type_gestion = stationObject.getString("type_gestion");
                                    String tpe = stationObject.getString("tpe");*/
                                    Station station = new Station(codestation, namefr, address, distance, ville);
                                    stations.add(station);
                                }
                                callback.onSuccess(stations);
                            } catch (JSONException e) {
                                callback.onError("Une erreur s'est produite" + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            callback.onError("Aucune donnée disponible");
                        }
                    } catch (JSONException e) {
                        callback.onError("Aucune donnée disponible");
                    }
                } else {
                    callback.onError("Aucune donnée disponible");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        });
        queue.add(request);
    }

    public interface StationCallback {
        void onSuccess(ArrayList<Station> stations);
        void onError(String message);
    }


    public void updateUserPassword(final String username, final String new_password, final UpdateUserPasswordCallback callback) {
        String url = Constants.WEB_SERVICE_URL + "UpdateUserPassword.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    JSONObject data = api_rep.getJSONObject("data");
                    String message = data.getString("message");
                    String hashed_pws = data.getString("hashed_pws");
                    String new_password = data.getString("new_password");
                    boolean send_sms_param = api_rep.getBoolean("send_sms_param");
                    boolean sms_sent = api_rep.getBoolean("sms_sent");
                    boolean send_mail_param = api_rep.getBoolean("send_mail_param");
                    boolean mail_sent = api_rep.getBoolean("mail_sent");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        callback.onSuccess(hashed_pws, new_password, message);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("Une erreur s'est produite: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(Util.getErrorMessage(context, error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("new_password", new_password);
                return map;
            }
        };
        queue.add(request);
    }

    public interface UpdateUserPasswordCallback {
        void onSuccess(String hashed_pws, String new_password, String message);
        void onError(String message);
    }
}