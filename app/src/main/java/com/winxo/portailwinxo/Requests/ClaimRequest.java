package com.winxo.portailwinxo.Requests;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.Claim;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaimRequest {

    private final RequestQueue queue;
    private final Context context;

    public ClaimRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void addClaim(final String id_site, final String detail_val, final String id_support, final String version, final String imei, final addClaimCallback callback) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        String id_claim = api_rep.getString("id_claim");
                        String ref_claim = api_rep.getString("ref_claim");
                        boolean send_sms_param = api_rep.getBoolean("send_sms_param");
                        boolean sms_sent = api_rep.getBoolean("sms_sent");
                        boolean send_mail_param = api_rep.getBoolean("send_mail_param");
                        boolean mail_sent = api_rep.getBoolean("mail_sent");
                        callback.onSuccess(message, id_claim);
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
                map.put("param", "addClaim");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("detail_val", detail_val);
                map.put("build_version", BuildConfig.VERSION_NAME);
                map.put("id_support", id_support);
                map.put("version", version);
                return map;
            }
        };
        queue.add(request);
    }

    public interface addClaimCallback {
        void onSuccess(String message, String id_claim);
        void onError(String message);
    }



    public void getClaimList(final String id_site, final String limit, final ClaimCallback callback) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {JSONObject json = api_rep.getJSONObject("data");
                        JSONArray list = json.getJSONArray("list");
                        int nb_line = json.getInt("nb_line");
                        if (nb_line > 0) {
                            ArrayList<Claim> claims = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject claimObject = list.getJSONObject(i);
                                    String id_claim = claimObject.getString("id_claim");
                                    String ref = claimObject.getString("ref");
                                    String date_claim = claimObject.getString("date_claim");
                                    String time_claim = claimObject.getString("time_claim");
                                    String description = claimObject.getString("description");
                                    String audio = claimObject.getString("audio");
                                    String image1 = claimObject.getString("image1");
                                    String image2 = claimObject.getString("image2");
                                    String image3 = claimObject.getString("image3");
                                    String image4 = claimObject.getString("image4");
                                    String status = claimObject.getString("status");
                                    String SiteName = claimObject.getString("SiteName");
                                    Claim claim = new Claim(id_claim, SiteName, ref, date_claim, time_claim, description, audio, image1, image2, image3, image4, status);
                                    claims.add(claim);
                                }
                                callback.onSuccess(claims);
                        }
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
                map.put("param", "getClaimList");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("limit", String.valueOf(limit));
                return map;
            }
        };
        queue.add(request);
    }

    public interface ClaimCallback {
        void onSuccess(ArrayList<Claim> claims);
        void onError(String message);
    }



    public void getSupervisionClaimList(final String id_animateur, final String id_site, final int limit, final SupervisionClaimCallback callback) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {JSONObject json = api_rep.getJSONObject("data");
                        JSONArray list = json.getJSONArray("list");
                        int nb_line = json.getInt("nb_line");
                        if (nb_line > 0) {
                            ArrayList<Claim> claims = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject claimObject = list.getJSONObject(i);
                                String id_claim = claimObject.getString("id_claim");
                                String ref = claimObject.getString("ref");
                                String date_claim = claimObject.getString("date_claim");
                                String time_claim = claimObject.getString("time_claim");
                                String description = claimObject.getString("description");
                                String audio = claimObject.getString("audio");
                                String image1 = claimObject.getString("image1");
                                String image2 = claimObject.getString("image2");
                                String image3 = claimObject.getString("image3");
                                String image4 = claimObject.getString("image4");
                                String status = claimObject.getString("status");
                                String SiteName = claimObject.getString("SiteName");
                                Claim claim = new Claim(id_claim, SiteName, ref, date_claim, time_claim, description, audio, image1, image2, image3, image4, status);
                                claims.add(claim);
                            }
                            callback.onSuccess(claims);
                        }
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSupervisionClaimList onResponse Catch: " + e.getMessage());
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
                map.put("param", "getSupervisionClaimList");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("id_animateur", id_animateur);
                map.put("limit", String.valueOf(limit));
                return map;
            }
        };
        queue.add(request);
    }

    public interface SupervisionClaimCallback {
        void onSuccess(ArrayList<Claim> claims);
        void onError(String message);
    }



    public void getClaimById(final String id_claim, final getClaimByIdCallback callback) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        JSONObject claimObject = api_rep.getJSONObject("data");
                        String id_claim = claimObject.getString("id_claim");
                        String id_site = claimObject.getString("id_site");
                        String reference = claimObject.getString("reference");
                        String date_claim = claimObject.getString("date_claim");
                        String time_claim = claimObject.getString("time_claim");
                        String description = claimObject.getString("description");
                        String audio = claimObject.getString("audio");
                        String image1 = claimObject.getString("image1");
                        String image2 = claimObject.getString("image2");
                        String image3 = claimObject.getString("image3");
                        String image4 = claimObject.getString("image4");
                        String status = claimObject.getString("status");
                        String site_name = claimObject.getString("site_name");
                        callback.onSuccess(id_claim, id_site, reference, date_claim, time_claim, description, audio, image1, image2, image3, image4, status, site_name);
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
                map.put("param", "getClaimById");
                map.put("token", Constants.API_TOKEN);
                map.put("id_claim", id_claim);
                return map;
            }
        };
        queue.add(request);
    }

    public interface getClaimByIdCallback {
        void onSuccess(String id_claim, String id_site, String reference, String date_claim, String time_claim, String description, String audio, String image1, String image2, String image3, String image4, String status, String site_name);
        void onError(String message);
    }


    public void getLastClaimId(final getLastClaimIdCallback callback) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Constants.WEB_SERVICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject api_rep = new JSONObject(response);
                    int code = api_rep.getInt("code");
                    String message = api_rep.getString("message");
                    String consultation_date = api_rep.getString("consultation_date");
                    String consultation_time = api_rep.getString("consultation_time");
                    if (code == 200) {
                        int id_claim = api_rep.getInt("id_claim");
                        callback.onSuccess(id_claim);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getLastClaimId onResponse Catch: " + e.getMessage());
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
                map.put("param", "getLastClaimId");
                map.put("token", Constants.API_TOKEN);
                return map;
            }
        };
        queue.add(request);
    }

    public interface getLastClaimIdCallback {
        void onSuccess(int id_claim);
        void onError(String message);
    }
}
