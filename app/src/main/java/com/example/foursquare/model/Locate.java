package com.example.foursquare.model;

public interface Locate {

    void startLocating();

    void getLastKnownLocation();

    void setLocationResultListener(LocateListener listener);

    void stopLocating();

}
