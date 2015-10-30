package com.example.foursquare.model;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetAccessImpl implements NetAccess {
    private final static String TAG = "JOE " + NetAccessImpl.class.getSimpleName();
    private String mLongitude;
    private String mLatitude;
    private String mSearchName;
    private NetAccessResultListener mResultListener = null;

    public NetAccessImpl() {
    }

    private String getVenues() {
        StringBuilder builder = new StringBuilder("");
        String CLIENT_ID = "";
        String CLIENT_SECRET = "";

        URL url;
        InputStream inputStream;
        HttpURLConnection urlConnection;

        try {
            url = new URL("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET +
                    "&v=20151024&ll=" + mLatitude + "," + mLongitude + "&query=" + mSearchName);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        urlConnection.disconnect();
        // trim the whitespaces
        return builder.toString().trim();
    }

    public void searchVenue(String venue, String latitude, String longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
        mSearchName = venue;
        new networkSearch().execute();
    }

    public void setSearchResultListener(NetAccessResultListener listener) {
        Log.d(TAG, "setSearchResultListener " + (listener == null ? "null":listener.toString()));
        mResultListener = listener;
    }

    private class networkSearch extends AsyncTask<View, Void, String> {
        String mSearchResult;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            Log.d(TAG, "doInBackground: start");
            mSearchResult = getVenues();
            Log.d(TAG, "doInBackground: done");
            return "";
        }

        @Override
        protected void onPreExecute() {
            // progress bar here?
        }

        @Override
        protected void onPostExecute(String result) {
            if (mSearchResult == null) {
                Log.d(TAG, "onPostExecute failed");
                mResultListener.netAccessError();
            } else {
                Log.d(TAG, "onPostExecute OK");
                mResultListener.netAccessSuccess(mSearchResult);
            }
        }
    }
}
