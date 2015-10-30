package com.example.foursquare.model;

public interface LocateListener {

    void locationSuccess(String latitude, String longitude);

    void locationError();

    void gpsNotEnabled();
}
