package com.winxo.portailwinxo.Model;

import java.util.ArrayList;

public class Order {
    private int id_order;
    private String sell_doc_web;
    private String sell_doc_sap;
    private String doc_date;
    private int id_site;
    private String SiteName;
    private String net_value;
    private String desired_delivery_date;
    private String schedule_delivery_date;
    private String shipped_delivery_date;
    private int added_by;
    private int order_status;
    private int exported;
    private String order_before_elapsed;
    private String order_after_elapsed;
    private String date_add;
    private String time_add;
    private ArrayList<OrderDetail> order_posts;

    public Order() {
    }

    public Order(int id_order) {
        this.id_order = id_order;
    }

    public Order(int id_order, String sell_doc_web, String sell_doc_sap, String doc_date, int id_site, String SiteName, String net_value, String desired_delivery_date, String schedule_delivery_date, String shipped_delivery_date, int added_by, int order_status, int exported, String order_before_elapsed, String order_after_elapsed, String date_add, String time_add, ArrayList<OrderDetail> order_posts) {
        this.id_order = id_order;
        this.sell_doc_web = sell_doc_web;
        this.sell_doc_sap = sell_doc_sap;
        this.doc_date = doc_date;
        this.id_site = id_site;
        this.SiteName = SiteName;
        this.net_value = net_value;
        this.desired_delivery_date = desired_delivery_date;
        this.schedule_delivery_date = schedule_delivery_date;
        this.shipped_delivery_date = shipped_delivery_date;
        this.added_by = added_by;
        this.order_status = order_status;
        this.exported = exported;
        this.order_before_elapsed = order_before_elapsed;
        this.order_after_elapsed = order_after_elapsed;
        this.date_add = date_add;
        this.time_add = time_add;
        this.order_posts = order_posts;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public String getSell_doc_web() {
        return sell_doc_web;
    }

    public void setSell_doc_web(String sell_doc_web) {
        this.sell_doc_web = sell_doc_web;
    }

    public String getSell_doc_sap() {
        return sell_doc_sap;
    }

    public void setSell_doc_sap(String sell_doc_sap) {
        this.sell_doc_sap = sell_doc_sap;
    }

    public String getDoc_date() {
        return doc_date;
    }

    public void setDoc_date(String doc_date) {
        this.doc_date = doc_date;
    }

    public int getId_site() {
        return id_site;
    }

    public void setId_site(int id_site) {
        this.id_site = id_site;
    }

    public String getSiteName() {
        return SiteName;
    }

    public void setSiteName(String siteName) {
        SiteName = siteName;
    }

    public String getNet_value() {
        return net_value;
    }

    public void setNet_value(String net_value) {
        this.net_value = net_value;
    }

    public String getDesired_delivery_date() {
        return desired_delivery_date;
    }

    public void setDesired_delivery_date(String desired_delivery_date) {
        this.desired_delivery_date = desired_delivery_date;
    }

    public String getSchedule_delivery_date() {
        return schedule_delivery_date;
    }

    public void setSchedule_delivery_date(String schedule_delivery_date) {
        this.schedule_delivery_date = schedule_delivery_date;
    }

    public String getShipped_delivery_date() {
        return shipped_delivery_date;
    }

    public void setShipped_delivery_date(String shipped_delivery_date) {
        this.shipped_delivery_date = shipped_delivery_date;
    }

    public int getAdded_by() {
        return added_by;
    }

    public void setAdded_by(int added_by) {
        this.added_by = added_by;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getExported() {
        return exported;
    }

    public void setExported(int exported) {
        this.exported = exported;
    }

    public String getOrder_before_elapsed() {
        return order_before_elapsed;
    }

    public void setOrder_before_elapsed(String order_before_elapsed) {
        this.order_before_elapsed = order_before_elapsed;
    }

    public String getOrder_after_elapsed() {
        return order_after_elapsed;
    }

    public void setOrder_after_elapsed(String order_after_elapsed) {
        this.order_after_elapsed = order_after_elapsed;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getTime_add() {
        return time_add;
    }

    public void setTime_add(String time_add) {
        this.time_add = time_add;
    }

    public ArrayList<OrderDetail> getOrder_posts() {
        return order_posts;
    }

    public void setOrder_posts(ArrayList<OrderDetail> order_posts) {
        this.order_posts = order_posts;
    }
}
