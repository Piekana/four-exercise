package com.example.foursquare.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.foursquare.application.ApplicationContextHelper;

public class LocateImpl implements Locate, LocationListener {
    private final static String TAG = "JOE " + LocateImpl.class.getSimpleName();
    private final Context mContext;
    // New York lat: 40.7463956, long: -73.9852992
    // Oulu     lat: 65,0126, long: 25,4715
    private LocateListener mListener = null;
    private LocationManager mLocationManager;

    public LocateImpl() {
        this.mContext = ApplicationContextHelper.getContext();
    }

    public void startLocating() {
        Log.d(TAG, "startLocating");
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        try {
            // GPS does not start if this is not called
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        } catch (SecurityException e) {
            Log.d(TAG, "requestLocationUpdates failed");
            e.printStackTrace();
        }
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS not started when requestLocationUpdates");
        }
    }

    public void stopLocating() {
        Log.d(TAG, "stopLocating ");
        try {
            // GPS does not start if this is not called
            mLocationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation");
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "getLastKnownLocation, GPS not enabled");
            mListener.gpsNotEnabled();
            return;
        }

        Location location = null;

        try {
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (location != null) {
            Log.d(TAG, location.toString());
            mListener.locationSuccess(Double.toString(location.getLatitude()),
                    Double.toString(location.getLongitude()));
        } else {
            mListener.locationError();
        }
    }

    public void setLocationResultListener(LocateListener listener) {
        Log.d(TAG, "setLocationResultListener " + (listener == null ? "null":listener.toString()));
        mListener = listener;
    }


    @Override
    public void onLocationChanged(Location location) {
        // Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Auto-generated method stub
    }
}
