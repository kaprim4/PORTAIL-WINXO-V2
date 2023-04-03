package com.winxo.portailwinxo.Model;

public class Claim {
    private String id_claim = "";
    private String id_site = "";
    private String ref = "";
    private String date_claim = "";
    private String time_claim = "";
    private String description = "";
    private String audio = "";
    private String image1 = "";
    private String image2 = "";
    private String image3 = "";
    private String image4 = "";
    private String status = "";

    public Claim(String id_claim, String id_site, String ref, String date_claim, String time_claim, String description, String audio, String image1, String image2, String image3, String image4, String status) {
        this.id_claim = id_claim;
        this.id_site = id_site;
        this.ref = ref;
        this.date_claim = date_claim;
        this.time_claim = time_claim;
        this.description = description;
        this.audio = audio;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.status = status;
    }

    public String getId_claim() {
        return id_claim;
    }

    public void setId_claim(String id_claim) {
        this.id_claim = id_claim;
    }

    public String getId_site() {
        return id_site;
    }

    public void setId_site(String id_site) {
        this.id_site = id_site;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDate_claim() {
        return date_claim;
    }

    public void setDate_claim(String date_claim) {
        this.date_claim = date_claim;
    }

    public String getTime_claim() {
        return time_claim;
    }

    public void setTime_claim(String time_claim) {
        this.time_claim = time_claim;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
