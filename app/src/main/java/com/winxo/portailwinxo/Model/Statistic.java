package com.winxo.portailwinxo.Model;

public class Statistic {
    private String prix_saisis = "";
    private String prix_appliques = "";
    private String prix_annules = "";

    public Statistic(String prix_saisis, String prix_appliques, String prix_annules) {
        this.prix_saisis = prix_saisis;
        this.prix_appliques = prix_appliques;
        this.prix_annules = prix_annules;
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
