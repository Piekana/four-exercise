package com.example.foursquare.presenter;

import android.util.Log;

import com.example.foursquare.view.Venue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class ParseVenue {
    private final static String TAG = "JOE " + ParseVenue.class.getSimpleName();
    public static ArrayList<Venue> parseFoursquare(final String response) {
        ArrayList<Venue> venueList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Venue venue = new Venue();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            venue.mName = jsonArray.getJSONObject(i).getString("name");
                            Log.d(TAG, "mName: " + venue.mName);
                            if (jsonArray.getJSONObject(i).has("location")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    venue.mAddress = jsonArray.getJSONObject(i).getJSONObject("location").getString("address");
                                    Log.d(TAG, "mAddress: " + venue.mAddress);
                                }
                                if (jsonArray.getJSONObject(i).getJSONObject("location").has("distance")) {
                                    venue.mDistance = jsonArray.getJSONObject(i).getJSONObject("location").getInt("distance");
                                    Log.d(TAG, "mDistance: " + venue.mDistance);
                                }
                                venueList.add(venue);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return venueList;
    }
}
