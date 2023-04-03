package com.winxo.portailwinxo.Requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.winxo.portailwinxo.BuildConfig;
import com.winxo.portailwinxo.Model.Order;
import com.winxo.portailwinxo.Model.OrderDetail;
import com.winxo.portailwinxo.Utilities.Constants;
import com.winxo.portailwinxo.Utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRequest {

    private final RequestQueue queue;
    private final Context context;

    public OrderRequest(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void addOrder(final String id_site, final String id_user, final String ssp_price, final String ga10_price, final String date_liv, final String latitude, final String longitude, final String id_support, final String version, final String imei, final addOrderCallback callback) {
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
                } catch (JSONException e) {
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
                map.put("param", "addOrder");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("id_user", id_user);
                map.put("ssp_price", ssp_price);
                map.put("ga10_price", ga10_price);
                map.put("date_liv", date_liv);
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

    public interface addOrderCallback {
        void onSuccess(String message);

        void onError(String message);
    }

    public void getOrderList(final String id_site, final String limit, final getOrderListCallback callback) {
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
                            ArrayList<Order> orders = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject orderObject = list.getJSONObject(i);
                                int id_order = orderObject.getInt("id_order");
                                String sell_doc_web = orderObject.getString("sell_doc_web");
                                String sell_doc_sap = orderObject.getString("sell_doc_sap");
                                String doc_date = orderObject.getString("doc_date");
                                int id_site = orderObject.getInt("id_site");
                                String SiteName = orderObject.getString("SiteName");
                                String net_value = orderObject.getString("net_value");
                                String desired_delivery_date = orderObject.getString("desired_delivery_date");
                                String schedule_delivery_date = orderObject.getString("schedule_delivery_date");
                                String shipped_delivery_date = orderObject.getString("shipped_delivery_date");
                                int added_by = orderObject.getInt("added_by");
                                int order_status = orderObject.getInt("order_status");
                                int exported = orderObject.getInt("exported");
                                String order_before_elapsed = orderObject.getString("order_before_elapsed");
                                String order_after_elapsed = orderObject.getString("order_after_elapsed");
                                String date_add = orderObject.getString("date_add");
                                String time_add = orderObject.getString("time_add");

                                JSONObject order_posts_json = orderObject.getJSONObject("order_posts");
                                JSONArray order_posts_list = order_posts_json.getJSONArray("list");
                                int order_posts_nb_line = order_posts_json.getInt("nb_line");
                                ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                                if (order_posts_nb_line > 0) {
                                    for (int j = 0; j < order_posts_list.length(); j++) {
                                        JSONObject orderDetailObject = order_posts_list.getJSONObject(j);
                                        int id_order_detail = orderDetailObject.getInt("id_order_detail");
                                        int id_product = orderDetailObject.getInt("id_product");
                                        String product_name = orderDetailObject.getString("product_name");
                                        int qty = orderDetailObject.getInt("qty");
                                        int planned_qty = orderDetailObject.getInt("planned_qty");
                                        int shipped_qty = orderDetailObject.getInt("shipped_qty");
                                        String order_detail_date_add = orderDetailObject.getString("date_add");
                                        OrderDetail orderDetail = new OrderDetail(id_order_detail, id_order, id_product, product_name, qty, planned_qty, shipped_qty, order_detail_date_add);
                                        orderDetails.add(orderDetail);
                                    }
                                }

                                Order order = new Order(id_order, sell_doc_web, sell_doc_sap, doc_date, id_site, SiteName, net_value, desired_delivery_date, schedule_delivery_date, shipped_delivery_date, added_by, order_status, exported, order_before_elapsed, order_after_elapsed, date_add, time_add, orderDetails);
                                orders.add(order);
                            }
                            callback.onSuccess(orders);
                        }
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getOrderList onResponse Catch: " + e.getMessage());
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
                map.put("param", "getOrderList");
                map.put("token", Constants.API_TOKEN);
                map.put("id_site", id_site);
                map.put("limit", String.valueOf(limit));
                return map;
            }
        };
        queue.add(request);
    }

    public interface getOrderListCallback {
        void onSuccess(ArrayList<Order> orders);

        void onError(String message);
    }

    public void getSupervisionOrderList(final String id_animateur, final String limit, final SupervisionOrderListCallback callback) {
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
                            ArrayList<Order> orders = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject orderObject = list.getJSONObject(i);
                                int id_order = orderObject.getInt("id_order");
                                String sell_doc_web = orderObject.getString("sell_doc_web");
                                String sell_doc_sap = orderObject.getString("sell_doc_sap");
                                String doc_date = orderObject.getString("doc_date");
                                int id_site = orderObject.getInt("id_site");
                                String SiteName = orderObject.getString("SiteName");
                                String net_value = orderObject.getString("net_value");
                                String desired_delivery_date = orderObject.getString("desired_delivery_date");
                                String schedule_delivery_date = orderObject.getString("schedule_delivery_date");
                                String shipped_delivery_date = orderObject.getString("shipped_delivery_date");
                                int added_by = orderObject.getInt("added_by");
                                int order_status = orderObject.getInt("order_status");
                                int exported = orderObject.getInt("exported");
                                String order_before_elapsed = orderObject.getString("order_before_elapsed");
                                String order_after_elapsed = orderObject.getString("order_after_elapsed");
                                String date_add = orderObject.getString("date_add");
                                String time_add = orderObject.getString("time_add");

                                JSONObject order_posts_json = orderObject.getJSONObject("order_posts");
                                JSONArray order_posts_list = order_posts_json.getJSONArray("list");
                                int order_posts_nb_line = order_posts_json.getInt("nb_line");
                                ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                                if (order_posts_nb_line > 0) {
                                    for (int j = 0; j < order_posts_list.length(); j++) {
                                        JSONObject orderDetailObject = order_posts_list.getJSONObject(j);
                                        int id_order_detail = orderDetailObject.getInt("id_order_detail");
                                        int id_product = orderDetailObject.getInt("id_product");
                                        String product_name = orderDetailObject.getString("product_name");
                                        int qty = orderDetailObject.getInt("qty");
                                        int planned_qty = orderDetailObject.getInt("planned_qty");
                                        int shipped_qty = orderDetailObject.getInt("shipped_qty");
                                        String order_detail_date_add = orderDetailObject.getString("date_add");
                                        OrderDetail orderDetail = new OrderDetail(id_order_detail, id_order, id_product, product_name, qty, planned_qty, shipped_qty, order_detail_date_add);
                                        orderDetails.add(orderDetail);
                                    }
                                }

                                Order order = new Order(id_order, sell_doc_web, sell_doc_sap, doc_date, id_site, SiteName, net_value, desired_delivery_date, schedule_delivery_date, shipped_delivery_date, added_by, order_status, exported, order_before_elapsed, order_after_elapsed, date_add, time_add, orderDetails);
                                orders.add(order);
                            }
                            callback.onSuccess(orders);
                        }
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getSupervisionOrderList onResponse Catch: " + e.getMessage());
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
                map.put("param", "getSupervisionOrderList");
                map.put("token", Constants.API_TOKEN);
                map.put("id_animateur", id_animateur);
                map.put("limit", String.valueOf(limit));
                return map;
            }
        };
        queue.add(request);
    }

    public interface SupervisionOrderListCallback {
        void onSuccess(ArrayList<Order> orders);

        void onError(String message);
    }

    public void getOrderById(final String id_order, final getClaimByIdCallback callback) {
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
                        JSONObject orderObject = api_rep.getJSONObject("data");
                        int id_order = orderObject.getInt("id_order");
                        String sell_doc_web = orderObject.getString("sell_doc_web");
                        String sell_doc_sap = orderObject.getString("sell_doc_sap");
                        String doc_date = orderObject.getString("doc_date");
                        int id_site = orderObject.getInt("id_site");
                        String SiteName = orderObject.getString("SiteName");
                        String net_value = orderObject.getString("net_value");
                        String desired_delivery_date = orderObject.getString("desired_delivery_date");
                        String schedule_delivery_date = orderObject.getString("schedule_delivery_date");
                        String shipped_delivery_date = orderObject.getString("shipped_delivery_date");
                        int added_by = orderObject.getInt("added_by");
                        int order_status = orderObject.getInt("order_status");
                        int exported = orderObject.getInt("exported");
                        String order_before_elapsed = orderObject.getString("order_before_elapsed");
                        String order_after_elapsed = orderObject.getString("order_after_elapsed");
                        String date_add = orderObject.getString("date_add");
                        String time_add = orderObject.getString("time_add");

                        JSONObject order_posts_json = orderObject.getJSONObject("order_posts");
                        JSONArray order_posts_list = order_posts_json.getJSONArray("list");
                        int order_posts_nb_line = order_posts_json.getInt("nb_line");
                        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                        if (order_posts_nb_line > 0) {
                            for (int j = 0; j < order_posts_list.length(); j++) {
                                JSONObject orderDetailObject = order_posts_list.getJSONObject(j);
                                int id_order_detail = orderDetailObject.getInt("id_order_detail");
                                int id_product = orderDetailObject.getInt("id_product");
                                String product_name = orderDetailObject.getString("product_name");
                                int qty = orderDetailObject.getInt("qty");
                                int planned_qty = orderDetailObject.getInt("planned_qty");
                                int shipped_qty = orderDetailObject.getInt("shipped_qty");
                                String order_detail_date_add = orderDetailObject.getString("date_add");
                                OrderDetail orderDetail = new OrderDetail(id_order_detail, id_order, id_product, product_name, qty, planned_qty, shipped_qty, order_detail_date_add);
                                orderDetails.add(orderDetail);
                            }
                        }
                        callback.onSuccess(id_order, sell_doc_web, sell_doc_sap, doc_date, id_site, SiteName, net_value, desired_delivery_date, schedule_delivery_date, shipped_delivery_date, added_by, order_status, exported, order_before_elapsed, order_after_elapsed, date_add, time_add, orderDetails);
                    } else {
                        callback.onError(message);
                    }
                } catch (JSONException e) {
                    callback.onError("getOrderById onResponse Catch: " + e.getMessage());
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
                map.put("param", "getOrderById");
                map.put("token", Constants.API_TOKEN);
                map.put("id_order", id_order);
                return map;
            }
        };
        queue.add(request);
    }

    public interface getClaimByIdCallback {
        void onSuccess(int id_order, String sell_doc_web, String sell_doc_sap, String doc_date, int id_site, String SiteName, String net_value, String desired_delivery_date, String schedule_delivery_date, String shipped_delivery_date, int added_by, int order_status, int exported, String order_before_elapsed, String order_after_elapsed, String date_add, String time_add, ArrayList<OrderDetail> orderDetails);

        void onError(String message);
    }


    public void cancelOrder(final String id_order, final cancelOrderCallback callback) {
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
                } catch (JSONException e) {
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
                map.put("param", "cancelOrder");
                map.put("token", Constants.API_TOKEN);
                map.put("id_order", id_order);
                return map;
            }
        };
        queue.add(request);
    }

    public interface cancelOrderCallback {
        void onSuccess(String message);
        void onError(String message);
    }


    public void ApplyOrder(final String id_order, final ApplyOrderCallback callback) {
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
                } catch (JSONException e) {
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
                map.put("param", "applyOrder");
                map.put("token", Constants.API_TOKEN);
                map.put("id_order", id_order);
                return map;
            }
        };
        queue.add(request);
    }

    public interface ApplyOrderCallback {
        void onSuccess(String message);
        void onError(String message);
    }
}
