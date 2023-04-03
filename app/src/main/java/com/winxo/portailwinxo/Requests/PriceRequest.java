package com.winxo.portailwinxo.Requests;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.Price;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PriceRequest {

    private final RequestQueue queue;
    private static final String TAG = "ParametersRequest";
    private final Activity activity;
    private final Context context;

    public PriceRequest(Activity activity, Context context, RequestQueue queue) {
        this.activity = activity;
        this.context = context;
        this.queue = queue;
    }

    public void getWholealePricesByid_site(final String id_site, final WholealePricesByid_siteCallback callback) {
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
                        String today_date_txt = json.getString("today_date_txt");
                        String tomorow_date_txt = json.getString("tomorow_date_txt");
                        String gasoil_1 = json.getString("gasoil_1");
                        String ssp_1 = json.getString("ssp_1");
                        String gasoil_2 = json.getString("gasoil_2");
                        String ssp_2 = json.getString("ssp_2");
                        String new_price_date1 = json.getString("new_price_date1");
                        String new_price_date2 = json.getString("new_price_date2");
                        callback.onSuccess(today_date_txt, tomorow_date_txt, gasoil_1, ssp_1, gasoil_2, ssp_2, new_price_date1, new_price_date2);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSiteByUserName onResponse Catch: " + e.getMessage());
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
                map.put("param", "getWholealePricesBySite");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                return map;
            }
        };
        queue.add(request);
    }

    public interface WholealePricesByid_siteCallback {
        void onSuccess(String today_date_txt, String tomorow_date_txt, String gasoil_1, String ssp_1, String gasoil_2, String ssp_2, String new_price_date1, String new_price_date2);

        void onError(String message);
    }


    public void getCurrentPriceByid_site(final String id_site, final CurrentPriceByid_siteCallback callback) {
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
                        JSONArray list = json.getJSONArray("list");
                        String nb_line = json.getString("nb_line");
                        callback.onSuccess(list);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSiteByUserName onResponse Catch: " + e.getMessage());
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
                map.put("param", "getCurrentPriceBySite");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                return map;
            }
        };
        queue.add(request);
    }

    public interface CurrentPriceByid_siteCallback {
        void onSuccess(JSONArray gradelist_prices);

        void onError(String message);
    }


    public void getNextCycle(final getNextCycleCallback callback) {
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
                        String data = api_rep.getString("data");
                        callback.onSuccess(data);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSiteByUserName onResponse Catch: " + e.getMessage());
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
                map.put("param", "getNextCycle");
                map.put("token", Constants.API_TOKEN);
                return map;
            }
        };
        queue.add(request);
    }

    public interface getNextCycleCallback {
        void onSuccess(String next_cycle);

        void onError(String message);
    }

    public void addPrice(final String id_site, final String grade1, final String grade2, final String grade3, final String grade4, final String grade5, final String grade6, final String grade7, final String grade8, final String grade9, final String grade10, final String grade11, final String proposalAppDate, final String proposalAppTime, final String latitude, final String longitude, final String id_support, final String version, final String imei, final addPriceCallback callback) {
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
                        callback.onSuccess(message);
                    } else {
                        callback.onError(message);
                    }
                } catch (Exception e) {
                    callback.onError("addPrice onResponse Catch: " + e.getMessage());
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
                map.put("param", "addPrice");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("grade1", grade1);
                map.put("grade2", grade2);
                map.put("grade3", grade3);
                map.put("grade4", grade4);
                map.put("grade5", grade5);
                map.put("grade6", grade6);
                map.put("grade7", grade7);
                map.put("grade8", grade8);
                map.put("grade9", grade9);
                map.put("grade10", grade10);
                map.put("grade11", grade11);
                map.put("proposalAppDate", proposalAppDate);
                map.put("proposalAppTime", proposalAppTime);
                map.put("build_version", BuildConfig.VERSION_NAME);
                map.put("ip_address", Util.getLocalIpAddress());
                map.put("lat", latitude);
                map.put("lng", longitude);
                map.put("id_support", id_support);
                map.put("version", version);
                map.put("imei", imei);
                return map;
            }
        };
        queue.add(request);
    }

    public interface addPriceCallback {
        void onSuccess(String message);

        void onError(String message);
    }


    public void getPriceStatistics(final String id_site, final PriceStatisticsCallback callback) {
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
                        String prix_saisis = json.getString("prix_saisis");
                        String prix_appliques = json.getString("prix_appliques");
                        String prix_annules = json.getString("prix_annules");
                        callback.onSuccess(prix_saisis, prix_appliques, prix_annules);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSiteByUserName onResponse Catch: " + e.getMessage());
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
                map.put("param", "getCurrentPriceBySite");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                return map;
            }
        };
        queue.add(request);
    }

    public interface PriceStatisticsCallback {
        void onSuccess(String prix_saisis, String prix_appliques, String prix_annules);

        void onError(String message);
    }


    public void getPricesList(final String id_site, final String limit, final PriceCallback callback) {
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
                        JSONArray list = json.getJSONArray("list");
                        int nb_line = json.getInt("nb_line");
                        if (nb_line > 0) {
                            ArrayList<Price> prices = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject priceObject = list.getJSONObject(i);
                                String id_proposal = priceObject.getString("id_proposal");
                                String id_site = priceObject.getString("id_site");
                                String SiteName = priceObject.getString("SiteName");
                                String superviseur_name = priceObject.getString("superviseur_name");
                                String Grade1 = priceObject.getString("Grade1");
                                String Grade2 = priceObject.getString("Grade2");
                                String Grade3 = priceObject.getString("Grade3");
                                String Grade4 = priceObject.getString("Grade4");
                                String Grade5 = priceObject.getString("Grade5");
                                String Grade6 = priceObject.getString("Grade6");
                                String Grade7 = priceObject.getString("Grade7");
                                String Grade8 = priceObject.getString("Grade8");
                                String Grade9 = priceObject.getString("Grade9");
                                String Grade10 = priceObject.getString("Grade10");
                                String Grade11 = priceObject.getString("Grade11");
                                String treated = priceObject.getString("treated");
                                String application_date = priceObject.getString("application_date");
                                String application_time = priceObject.getString("application_time");
                                String date_add = priceObject.getString("date_add");
                                String add_date = priceObject.getString("add_date");
                                String add_time = priceObject.getString("add_time");
                                String id_event_ho = priceObject.getString("id_event_ho");
                                Price price = new Price(id_proposal, id_site, SiteName, superviseur_name, Grade1, Grade2, Grade3, Grade4, Grade5, Grade6, Grade7, Grade8, Grade9, Grade10, Grade11, treated, application_date, application_time, date_add, add_date, add_time, id_event_ho);
                                prices.add(price);
                            }
                            callback.onSuccess(prices);
                        }
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getPricesList onResponse Catch: " + e.getMessage());
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
                map.put("param", "getPrices");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("limit", limit);
                return map;
            }
        };
        queue.add(request);
    }

    public interface PriceCallback {
        void onSuccess(ArrayList<Price> prices);

        void onError(String message);
    }

    public void getSupervisionPricesList(final String id_animateur_site, final String limit, final SupervisionPriceCallback callback) {
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
                        JSONArray list = json.getJSONArray("list");
                        int nb_line = json.getInt("nb_line");
                        if (nb_line > 0) {
                            ArrayList<Price> prices = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject priceObject = list.getJSONObject(i);
                                String id_proposal = priceObject.getString("id_proposal");
                                String id_site = priceObject.getString("id_site");
                                String SiteName = priceObject.getString("SiteName");
                                String superviseur_name = priceObject.getString("superviseur_name");
                                String Grade1 = priceObject.getString("Grade1");
                                String Grade2 = priceObject.getString("Grade2");
                                String Grade3 = priceObject.getString("Grade3");
                                String Grade4 = priceObject.getString("Grade4");
                                String Grade5 = priceObject.getString("Grade5");
                                String Grade6 = priceObject.getString("Grade6");
                                String Grade7 = priceObject.getString("Grade7");
                                String Grade8 = priceObject.getString("Grade8");
                                String Grade9 = priceObject.getString("Grade9");
                                String Grade10 = priceObject.getString("Grade10");
                                String Grade11 = priceObject.getString("Grade11");
                                String treated = priceObject.getString("treated");
                                String application_date = priceObject.getString("application_date");
                                String application_time = priceObject.getString("application_time");
                                String date_add = priceObject.getString("date_add");
                                String add_date = priceObject.getString("add_date");
                                String add_time = priceObject.getString("add_time");
                                String id_event_ho = priceObject.getString("id_event_ho");
                                Price price = new Price(id_proposal, id_site, SiteName, superviseur_name, Grade1, Grade2, Grade3, Grade4, Grade5, Grade6, Grade7, Grade8, Grade9, Grade10, Grade11, treated, application_date, application_time, date_add, add_date, add_time, id_event_ho);
                                prices.add(price);
                            }
                            callback.onSuccess(prices);
                        } else {
                            callback.onError("Aucune donnée disponible");
                        }
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
                map.put("param", "getSupervisionPrices");
                map.put("token", Constants.API_TOKEN);
                map.put("id_animateur_site", id_animateur_site);
                map.put("limit", limit);
                return map;
            }
        };
        queue.add(request);
    }

    public interface SupervisionPriceCallback {
        void onSuccess(ArrayList<Price> prices);

        void onError(String message);
    }

}
