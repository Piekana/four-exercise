package com.example.foursquare.model;

public interface NetAccessResultListener {

    void netAccessSuccess(String json);

    void netAccessError();
}
