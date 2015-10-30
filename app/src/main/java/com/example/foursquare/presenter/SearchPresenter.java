package com.example.foursquare.presenter;

public interface SearchPresenter {

    void resume();

    void searchVenues(String venue);

    void pause();

    void destroy();
}
