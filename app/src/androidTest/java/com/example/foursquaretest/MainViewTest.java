package com.example.foursquaretest;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.foursquare.R;
import com.example.foursquare.view.MainView;
import com.example.foursquare.view.MainViewImpl;
import com.example.foursquare.view.Venue;

import java.util.ArrayList;

public class MainViewTest extends ActivityInstrumentationTestCase2<MainViewImpl> {

    public MainViewTest() {
        super((MainViewImpl.class));
    }

    private MainView mMainView;
    private Activity mAct;
    private ListView mListView;
    private EditText mVenueSearch;
    private TextView mStatusView;
    private final static String testAddress = "TestAddress";
    private final static String testName = "TestName";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        mMainView = getActivity();
        mAct = getActivity();
        mStatusView = (TextView) mAct.findViewById(R.id.status_text);
        mVenueSearch = (EditText) mAct.findViewById(R.id.venue_search);
        mListView = (ListView) mAct.findViewById(R.id.search_results_list);
    }

    @SmallTest
    public void testPreconditions() {
        assertNotNull("mLaunchActivity is null", mAct);
        assertNotNull("statusView is null", mStatusView);
        assertNotNull("mVenueSearch is null", mVenueSearch);
        assertNotNull("mListView is null", mListView);
    }

    @SmallTest
    public void testDefaultTexts() {
        assertEquals("Status default failed", mAct.getString(R.string.default_search_text), mStatusView.getText().toString());
        assertEquals("Search hint failed", mAct.getString(R.string.search_hint), mVenueSearch.getHint().toString());
        assertEquals("Search results list adapter not set yet", null, mListView.getAdapter());
    }

    // mMainView requests must be run on the UI thread. We can use the runOnUiThread method
    // and pass it a Runnable that contains a call to interface or annotation @UiThreadTest.
    //  mAct.runOnUiThread(new Runnable() {
    //      public void run() {
    //          mMainViewImpl.errorGpsDisabled();
    //      }
    //  });
    //  getInstrumentation().waitForIdleSync();

    @UiThreadTest
    public void testGpsDisabledError() {
        mMainView.errorGpsDisabled();
        assertEquals("GPS disabled status error", mAct.getString(R.string.error_gps_not_enabled), mStatusView.getText().toString());
        assertEquals("Search results list adapter not null", null, mListView.getAdapter());
    }

    @UiThreadTest
    public void testLocatingError() {
        mMainView.errorLocating();
        assertEquals("Locating error", mAct.getString(R.string.error_location_failed), mStatusView.getText().toString());
        assertEquals("Search results list adapter not null", null, mListView.getAdapter());
    }

    @UiThreadTest
    public void testSearchError() {
        mMainView.errorSearch();
        assertEquals("Search error", mAct.getString(R.string.error_search_failed), mStatusView.getText().toString());
        assertEquals("Search results list adapter not null", null, mListView.getAdapter());
    }

    @UiThreadTest
    public void testEmptySearchSuccess() {
        ArrayList<Venue> venueList = new ArrayList<>();
        mMainView.updateSearchList(venueList);
        assertEquals("Search error", mAct.getString(R.string.error_search_failed), mStatusView.getText().toString());
        assertEquals("Search results list adapter not null", null, mListView.getAdapter());
    }

    @UiThreadTest
    public void testSearchSuccess() {
        ArrayList<Venue> venueList = new ArrayList<>();

        // First item
        venueList.add(createTestVenue(0));

        // Second item
        venueList.add(createTestVenue(1));

        mMainView.updateSearchList(venueList);

        assertEquals("Search error", mAct.getString(R.string.results_search_text), mStatusView.getText().toString());
        assertNotNull("Search results list is null", mListView.getAdapter());

        // Verify first item on list
        compareTestVenue(0);

        // Verify second item on list
        compareTestVenue(1);
    }

    @UiThreadTest
    public void testSearchSuccessNoDistance() {
        ArrayList<Venue> venueList = new ArrayList<>();

        Venue venue = createTestVenue(0);
        venue.mDistance = 0;
        venueList.add(venue);

        mMainView.updateSearchList(venueList);

        assertEquals("Search error", mAct.getString(R.string.results_search_text), mStatusView.getText().toString());
        assertNotNull("Search results list is null", mListView.getAdapter());

        Venue itemVenue = (Venue) mListView.getAdapter().getItem(0);
        assertEquals("Search results list adapter not null", testAddress + 0, itemVenue.mAddress);
        assertEquals("Search results list adapter not null", 0, itemVenue.mDistance);
        assertEquals("Search results list adapter not null", testName + 0, itemVenue.mName);
    }

//   TODO  public void testTextInput() {
//        // Send string input value
//        getInstrumentation().runOnMainSync(new Runnable() {
//            @Override
//            public void run() {
//                mVenueSearch.requestFocus();
//            }
//        });
//        getInstrumentation().waitForIdleSync();
//        getInstrumentation().sendStringSync("Hello Android!");
//        getInstrumentation().waitForIdleSync();
//        Help.sleep(10000);
//    }

    private Venue createTestVenue(int index) {
        Venue venue = new Venue();
        venue.mAddress = testAddress + index;

        venue.mDistance = 100 + index;
        venue.mName = testName + index;
        return venue;
    }

    private void compareTestVenue(int index) {
        Venue itemVenue = (Venue) mListView.getAdapter().getItem(index);
        assertEquals("Search results list adapter not null", testAddress + index, itemVenue.mAddress);
        assertEquals("Search results list adapter not null", 100 + index, itemVenue.mDistance);
        assertEquals("Search results list adapter not null", testName + index, itemVenue.mName);
    }
}