package com.winxo.portailwinxo.Model;

public class Station {
    private String id = "";
    private String latitude = "";
    private String longitude = "";
    private String address = "";
    private String address_ar = "";
    private String openingtime = "";
    private String closingtime = "";
    private String codestation = "";
    private String datebeginservice = "";
    private String namefr = "";
    private String namear = "";
    private String id_ville = "";
    private String id_service = "";
    private String id_produit = "";
    private String type_gestion = "";
    private String tpe = "";
    private String distance = "";
    private String ville = "";

    public Station(String codestation, String namefr, String address, String distance, String ville) {
        this.address = address;
        this.codestation = codestation;
        this.namefr = namefr;
        this.distance = distance;
        this.ville = ville;
    }

    public Station(String id, String latitude, String longitude, String address, String address_ar, String openingtime, String closingtime, String codestation, String datebeginservice, String namefr, String namear, String id_ville, String id_service, String id_produit, String type_gestion, String tpe, String distance, String ville) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.address_ar = address_ar;
        this.openingtime = openingtime;
        this.closingtime = closingtime;
        this.codestation = codestation;
        this.datebeginservice = datebeginservice;
        this.namefr = namefr;
        this.namear = namear;
        this.id_ville = id_ville;
        this.id_service = id_service;
        this.id_produit = id_produit;
        this.type_gestion = type_gestion;
        this.tpe = tpe;
        this.distance = distance;
        this.ville = ville;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_ar() {
        return address_ar;
    }

    public void setAddress_ar(String address_ar) {
        this.address_ar = address_ar;
    }

    public String getOpeningtime() {
        return openingtime;
    }

    public void setOpeningtime(String openingtime) {
        this.openingtime = openingtime;
    }

    public String getClosingtime() {
        return closingtime;
    }

    public void setClosingtime(String closingtime) {
        this.closingtime = closingtime;
    }

    public String getCodestation() {
        return codestation;
    }

    public void setCodestation(String codestation) {
        this.codestation = codestation;
    }

    public String getDatebeginservice() {
        return datebeginservice;
    }

    public void setDatebeginservice(String datebeginservice) {
        this.datebeginservice = datebeginservice;
    }

    public String getNamefr() {
        return namefr;
    }

    public void setNamefr(String namefr) {
        this.namefr = namefr;
    }

    public String getNamear() {
        return namear;
    }

    public void setNamear(String namear) {
        this.namear = namear;
    }

    public String getId_ville() {
        return id_ville;
    }

    public void setId_ville(String id_ville) {
        this.id_ville = id_ville;
    }

    public String getId_service() {
        return id_service;
    }

    public void setId_service(String id_service) {
        this.id_service = id_service;
    }

    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getType_gestion() {
        return type_gestion;
    }

    public void setType_gestion(String type_gestion) {
        this.type_gestion = type_gestion;
    }

    public String getTpe() {
        return tpe;
    }

    public void setTpe(String tpe) {
        this.tpe = tpe;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
