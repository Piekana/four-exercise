package com.example.foursquare.view;

import java.util.ArrayList;

public interface MainView {
    void updateSearchList(ArrayList<Venue> venues);

    void errorSearch();

    void errorGpsDisabled();

    void errorLocating();
}
