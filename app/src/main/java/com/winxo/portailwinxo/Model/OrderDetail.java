package com.winxo.portailwinxo.Model;

public class OrderDetail {
    private int id_order_detail;
    private int id_order;
    private int id_product;
    private String product_name;
    private int qty;
    private int planned_qty;
    private int shipped_qty;
    private String date_add;

    public OrderDetail() {
    }

    public OrderDetail(int id_order_detail) {
        this.id_order_detail = id_order_detail;
    }

    public OrderDetail(int id_order_detail, int id_order, int id_product, String product_name, int qty, int planned_qty, int shipped_qty, String date_add) {
        this.id_order_detail = id_order_detail;
        this.id_order = id_order;
        this.id_product = id_product;
        this.product_name = product_name;
        this.qty = qty;
        this.planned_qty = planned_qty;
        this.shipped_qty = shipped_qty;
        this.date_add = date_add;
    }

    public int getId_order_detail() {
        return id_order_detail;
    }

    public void setId_order_detail(int id_order_detail) {
        this.id_order_detail = id_order_detail;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPlanned_qty() {
        return planned_qty;
    }

    public void setPlanned_qty(int planned_qty) {
        this.planned_qty = planned_qty;
    }

    public int getShipped_qty() {
        return shipped_qty;
    }

    public void setShipped_qty(int shipped_qty) {
        this.shipped_qty = shipped_qty;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }
}
