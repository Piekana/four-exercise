package com.example.foursquaretest;

import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.foursquare.R;
import com.example.foursquare.model.Locate;
import com.example.foursquare.model.LocateListener;
import com.example.foursquare.model.NetAccess;
import com.example.foursquare.model.NetAccessResultListener;
import com.example.foursquare.presenter.SearchPresenterImpl;
import com.example.foursquare.view.MainView;
import com.example.foursquare.view.MainViewImpl;
import com.example.foursquare.view.Venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchPresenterTest extends ActivityInstrumentationTestCase2<MainViewImpl> {
    public SearchPresenterTest() {
        super((MainViewImpl.class));
    }

    private SearchPresenterImpl mPresenter;
    private boolean mGpsDisabledError = false;
    private boolean mLocationError = false;
    private boolean mSearchError = false;
    private boolean mSearch = false;
    private boolean mResume = false;
    private boolean mStop = false;
    private final String mExpectedLatitude = "33";
    private final String mExpectedLongitude = "34";
    private final String mExpectedVenue = "koe";

    private class mockMainView implements MainView {
        public void updateSearchList(ArrayList<Venue> venues) {
            Log.d("Mock koe", "updateSearchList");
            assertEquals("Search list update failed", true, !venues.isEmpty());
            assertEquals("Search list distance failed", 100, venues.get(0).mDistance);
            assertEquals("Search list name failed", "Mercedes Vito Koeajo", venues.get(0).mName);
            assertEquals("Search list address failed", "Rita-aukiontie", venues.get(0).mAddress);
            mSearch = false;
        }

        public void errorSearch() {
            Log.d("Mock koe", "errorSearch");
            assertEquals("errorSearch not expected", true, mSearchError);
            mSearchError = false;
        }

        public void errorGpsDisabled() {
            Log.d("Mock koe", "errorGpsDisabled");
            assertEquals("errorGpsDisabled not expected", true, mGpsDisabledError);
            mGpsDisabledError = false;
        }

        public void errorLocating() {
            Log.d("Mock koe", "errorLocating");
            assertEquals("errorLocating not expected", true, mLocationError);
            mLocationError = false;
        }
    }

    private class mockNetAccess implements NetAccess {

        private NetAccessResultListener mResultListener = null;

        public void searchVenue(String venue, String latitude, String longitude) {
            Log.d("Mock koe", "searchVenue" + venue + latitude + longitude);
            assertEquals("searchVenue not expected", true, mSearch);
            assertEquals("Expected venue failed", venue, mExpectedVenue);
            assertEquals("Expected latitude failed", latitude, mExpectedLatitude);
            assertEquals("Expected longitude failed", longitude, mExpectedLongitude);

            // Read response from file
            mResultListener.netAccessSuccess(read_file());
        }

        public void setSearchResultListener(NetAccessResultListener listener) {
            Log.d("Mock koe", "setSearchResultListener");
            mResultListener = listener;
        }
    }

    private class mockLocate implements Locate {

        private LocateListener mLocateListener = null;

        public void startLocating() {
            Log.d("Mock koe", "startLocating");
            assertEquals("startLocating not expected", true, mResume);
            mResume = false;
        }

        public void getLastKnownLocation() {
            Log.d("Mock koe", "getLastKnownLocation");
            mLocateListener.locationSuccess(mExpectedLatitude, mExpectedLongitude);
        }

        public void setLocationResultListener(LocateListener listener) {
            Log.d("Mock koe", "setLocationResultListener");
            mLocateListener = listener;
        }

        public void stopLocating() {
            Log.d("Mock koe", "stopLocating");
            assertEquals("stopLocating not expected", true, mStop);
            mStop = false;
        }
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Log.d("JOE", "setUp");
        mPresenter = new SearchPresenterImpl(new mockMainView(), new mockNetAccess(), new mockLocate());

        mGpsDisabledError = false;
        mLocationError = false;
        mSearchError = false;
        mSearch = false;
        mResume = false;
        mStop = false;
        getInstrumentation().waitForIdleSync();
    }

    public void testLocateErrors() {
        Log.d("JOE", "testLocateErrors");

        // Location fetching failed
        mLocationError = true;
        mPresenter.locationError();
        assertEquals("Location error not received", false, mLocationError);

        // GPS disabled
        mGpsDisabledError = true;
        mPresenter.gpsNotEnabled();
        assertEquals("GPS disabled error not received", false, mGpsDisabledError);
    }

    public void testNetAccessErrors() {
        Log.d("JOE", "testNetAccessErrors");

        // Search error
        mSearchError = true;
        mPresenter.netAccessError();
        assertEquals("Search error not received", false, mSearchError);
    }

    public void testSearch() {
        Log.d("JOE", "testSearch");

        // Search venues matching koe, triggers getLastKnownLocation and then setLocationResultListener
        mResume = true;
        mPresenter.resume();
        assertEquals("Location tracking not started", false, mResume);

        mSearch = true;
        mPresenter.searchVenues(mExpectedVenue);
        assertEquals("No search results received", false, mSearch);

        mStop = true;
        mPresenter.pause();
        assertEquals("Location tracking was not stopped", false, mStop);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        Log.d("JOE", "tearDown");
    }

    private String read_file() {
        Context testContext = getActivity().getApplicationContext();
        Resources testRes = testContext.getResources();
        InputStream ts = testRes.openRawResource(R.raw.fourmock200);
        assertNotNull(testRes);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(ts));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Testing file koe", text.toString());
        return text.toString().trim();
    }
}