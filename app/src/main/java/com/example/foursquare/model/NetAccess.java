package com.example.foursquare.model;

public interface NetAccess {

    void searchVenue(String venue, String latitude, String longitude);

    void setSearchResultListener(NetAccessResultListener listener);
}
