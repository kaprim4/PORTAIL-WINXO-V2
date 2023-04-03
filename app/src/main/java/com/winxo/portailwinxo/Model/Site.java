package com.winxo.portailwinxo.Model;

public class Site {
    private String id_site = "";
    private String id_profile = "";
    private String id_animateur = "";
    private String id_animateur_site = "";
    private String superviseur_name = "";
    private String display_name = "";
    private String code_sap = "";
    private String username = "";
    private String password = "";
    private String password_brut = "";
    private String libelle = "";
    private String tel = "";
    private String email = "";
    private String id_city = "";
    private String id_company = "";
    private String address_ip = "";
    private String imei = "";
    private String GradeId_list = "";
    private String date_upd = "";
    private String status = "";
    private Boolean hasFusion = false;
    private String nb_totale = "";
    private String nb_active = "";
    private String nb_standby = "";
    private String prix_saisis;
    private String prix_appliques;
    private String prix_annules;

    public Site() {
    }

    public Site(String id_site) {
        this.id_site = id_site;
    }

    public Site(String id_site, String id_profile, String id_animateur, String id_animateur_site, String superviseur_name, String display_name, String code_sap, String username, String password, String password_brut, String libelle, String tel, String email, String id_city, String id_company, String address_ip, String imei, String gradeId_list, String date_upd, String status, Boolean hasFusion, String nb_totale, String nb_active, String nb_standby, String prix_saisis, String prix_appliques, String prix_annules) {
        this.id_site = id_site;
        this.id_profile = id_profile;
        this.id_animateur = id_animateur;
        this.id_animateur_site = id_animateur_site;
        this.superviseur_name = superviseur_name;
        this.display_name = display_name;
        this.code_sap = code_sap;
        this.username = username;
        this.password = password;
        this.password_brut = password_brut;
        this.libelle = libelle;
        this.tel = tel;
        this.email = email;
        this.id_city = id_city;
        this.id_company = id_company;
        this.address_ip = address_ip;
        this.imei = imei;
        GradeId_list = gradeId_list;
        this.date_upd = date_upd;
        this.status = status;
        this.hasFusion = hasFusion;
        this.nb_totale = nb_totale;
        this.nb_active = nb_active;
        this.nb_standby = nb_standby;
        this.prix_saisis = prix_saisis;
        this.prix_appliques = prix_appliques;
        this.prix_annules = prix_annules;
    }

    public String getId_site() {
        return id_site;
    }

    public void setId_site(String id_site) {
        this.id_site = id_site;
    }

    public String getId_profile() {
        return id_profile;
    }

    public void setId_profile(String id_profile) {
        this.id_profile = id_profile;
    }

    public String getId_animateur() {
        return id_animateur;
    }

    public void setId_animateur(String id_animateur) {
        this.id_animateur = id_animateur;
    }

    public String getId_animateur_site() {
        return id_animateur_site;
    }

    public void setId_animateur_site(String id_animateur_site) {
        this.id_animateur_site = id_animateur_site;
    }

    public String getSuperviseur_name() {
        return superviseur_name;
    }

    public void setSuperviseur_name(String superviseur_name) {
        this.superviseur_name = superviseur_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getCode_sap() {
        return code_sap;
    }

    public void setCode_sap(String code_sap) {
        this.code_sap = code_sap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_brut() {
        return password_brut;
    }

    public void setPassword_brut(String password_brut) {
        this.password_brut = password_brut;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId_city() {
        return id_city;
    }

    public void setId_city(String id_city) {
        this.id_city = id_city;
    }

    public String getId_company() {
        return id_company;
    }

    public void setId_company(String id_company) {
        this.id_company = id_company;
    }

    public String getAddress_ip() {
        return address_ip;
    }

    public void setAddress_ip(String address_ip) {
        this.address_ip = address_ip;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getGradeId_list() {
        return GradeId_list;
    }

    public void setGradeId_list(String gradeId_list) {
        GradeId_list = gradeId_list;
    }

    public String getDate_upd() {
        return date_upd;
    }

    public void setDate_upd(String date_upd) {
        this.date_upd = date_upd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getHasFusion() {
        return hasFusion;
    }

    public void setHasFusion(Boolean hasFusion) {
        this.hasFusion = hasFusion;
    }

    public String getNb_totale() {
        return nb_totale;
    }

    public void setNb_totale(String nb_totale) {
        this.nb_totale = nb_totale;
    }

    public String getNb_active() {
        return nb_active;
    }

    public void setNb_active(String nb_active) {
        this.nb_active = nb_active;
    }

    public String getNb_standby() {
        return nb_standby;
    }

    public void setNb_standby(String nb_standby) {
        this.nb_standby = nb_standby;
    }

    public String getPrix_saisis() {
        return prix_saisis;
    }

    public void setPrix_saisis(String prix_saisis) {
        this.prix_saisis = prix_saisis;
    }

    public String getPrix_appliques() {
        return prix_appliques;
    }

    public void setPrix_appliques(String prix_appliques) {
        this.prix_appliques = prix_appliques;
    }

    public String getPrix_annules() {
        return prix_annules;
    }

    public void setPrix_annules(String prix_annules) {
        this.prix_annules = prix_annules;
    }
}