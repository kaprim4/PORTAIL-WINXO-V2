package com.winxo.portailwinxo.Model;

public class ClaimImage {
    byte[] _image;
    int id_claim;
    String date_capture, heure_capture;

    public ClaimImage(byte[] _image) {
        this._image = _image;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] _image) {
        this._image = _image;
    }

    public int getId_claim() {
        return id_claim;
    }

    public void setId_claim(int id_claim) {
        this.id_claim = id_claim;
    }

    public String getDate_capture() {
        return date_capture;
    }

    public void setDate_capture(String date_capture) {
        this.date_capture = date_capture;
    }

    public String getHeure_capture() {
        return heure_capture;
    }

    public void setHeure_capture(String heure_capture) {
        this.heure_capture = heure_capture;
    }
}
