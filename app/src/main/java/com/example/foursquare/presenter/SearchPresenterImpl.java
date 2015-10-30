package com.example.foursquare.presenter;

import android.util.Log;

import com.example.foursquare.model.Locate;
import com.example.foursquare.model.LocateListener;
import com.example.foursquare.model.NetAccess;
import com.example.foursquare.model.NetAccessResultListener;
import com.example.foursquare.view.MainView;
import com.example.foursquare.view.Venue;

import java.util.ArrayList;

public class SearchPresenterImpl implements SearchPresenter, LocateListener, NetAccessResultListener {
    private final static String TAG = "JOE " + SearchPresenterImpl.class.getSimpleName();
    private MainView mMainView;
    private NetAccess mNetwork;
    private Locate mLocate;
    private String mVenueSearch = null;

    public SearchPresenterImpl(MainView mainView, NetAccess netAccess, Locate locate) {
        Log.d(TAG, "SearchPresenterImpl");
        mMainView = mainView;
        mNetwork = netAccess;
        mLocate = locate;
    }

    public void resume() {
        Log.d(TAG, "resume");
        mLocate.startLocating();
        mLocate.setLocationResultListener(this);
        mNetwork.setSearchResultListener(this);
    }

    public void pause() {
        Log.d(TAG, "pause");
        mLocate.stopLocating();
        mLocate.setLocationResultListener(null);
        mNetwork.setSearchResultListener(null);
    }

    public void destroy() {
        Log.d(TAG, "destroy");
        mLocate.stopLocating();
        mLocate.setLocationResultListener(null);
        mNetwork.setSearchResultListener(null);
        mLocate = null;
        mNetwork = null;
        mMainView = null;
    }

    public void searchVenues(String venue) {
        Log.d(TAG, "searchVenues");
        if (mVenueSearch == null) {
            mVenueSearch = venue;
            mLocate.getLastKnownLocation();
        } else {
            mVenueSearch = venue;
        }
    }

    // Location fetching success comes here
    public void locationSuccess(String latitude, String longitude) {
        Log.d(TAG, "locationUpdated: " + longitude + " " + latitude);
        mNetwork.searchVenue(mVenueSearch, latitude, longitude);
        mVenueSearch = null;
    }

    // Locating failed
    public void locationError() {
        mVenueSearch = null;
        mMainView.errorLocating();
    }

    // GPS not enabled
    public void gpsNotEnabled() {
        Log.d(TAG, "gpsNotEnabled");
        mVenueSearch = null;
        mMainView.errorGpsDisabled();
    }

    // Search results come here
    public void netAccessSuccess(String results) {
        Log.d(TAG, "netAccessSuccess");
        ArrayList<Venue> venues = ParseVenue.parseFoursquare(results);
        if (venues.isEmpty()) {
            mMainView.errorSearch();
        } else {
            mMainView.updateSearchList(venues);
        }
    }

    // Search errors come here
    public void netAccessError() {
        mMainView.errorSearch();
    }
}
