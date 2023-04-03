package com.winxo.portailwinxo.Model;

public class Photo  {
    byte[] _image;

    public Photo() {

    }

    public Photo(byte[] _image) {
        this._image = _image;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] _image) {
        this._image = _image;
    }
}