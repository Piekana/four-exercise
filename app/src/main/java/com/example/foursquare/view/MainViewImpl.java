package com.example.foursquare.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foursquare.R;
import com.example.foursquare.model.Locate;
import com.example.foursquare.model.LocateImpl;
import com.example.foursquare.model.NetAccess;
import com.example.foursquare.model.NetAccessImpl;
import com.example.foursquare.presenter.SearchPresenter;
import com.example.foursquare.presenter.SearchPresenterImpl;

import java.util.ArrayList;

public class MainViewImpl extends Activity implements MainView {
    private final static String TAG = "JOE " + MainViewImpl.class.getSimpleName();
    private SearchPresenter mPresenter;
    private Locate mLocate;
    private NetAccess mNetwork;
    private ListView mListView;
    private TextView mStatusView;

    private CharSequence mVenueSearchWord = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.search_results_list);
        EditText venueSearch = (EditText) findViewById(R.id.venue_search);
        mStatusView = (TextView) findViewById(R.id.status_text);

        venueSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void afterTextChanged(Editable venueSearchWord) {
                mVenueSearchWord = venueSearchWord.toString();
                Log.d(TAG, "mVenueSearchWord: " + mVenueSearchWord);
                if (mVenueSearchWord.equals("")) {
                    mListView.setAdapter(null);
                    mStatusView.setText(R.string.default_search_text);
                }
                if (!mVenueSearchWord.equals(venueSearchWord.toString())) {
                    // We don't wont to trigger new search if search word has not changed
                    // This can easily happen if view is rotated and activity gets re-created
                    mPresenter.searchVenues(mVenueSearchWord.toString());
                }
            }
        });
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (mPresenter == null) {
            // These should be injected and not be here
            mNetwork = new NetAccessImpl();
            mLocate = new LocateImpl();
            mPresenter = new SearchPresenterImpl(this, mNetwork, mLocate);
        }
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mPresenter.destroy();
        mNetwork = null;
        mLocate = null;
        mPresenter = null;
    }

    public void updateSearchList(ArrayList<Venue> venues) {
        Log.d(TAG, "updateSearchList");
        if (venues.isEmpty()) {
            mStatusView.setText(R.string.error_search_failed);
            mListView.setAdapter(null);
            Toast.makeText(this, R.string.error_search_failed, Toast.LENGTH_SHORT).show();
        } else {
            mStatusView.setText(R.string.results_search_text);
            RowAdapter adapter = new RowAdapter(mListView.getContext(), venues);
            mListView.setAdapter(adapter);
        }
    }

    public void errorSearch() {
        mStatusView.setText(R.string.error_search_failed);
        mListView.setAdapter(null);
        Toast.makeText(this, R.string.error_search_failed, Toast.LENGTH_SHORT).show();
    }

    public void errorGpsDisabled() {
        mStatusView.setText(R.string.error_gps_not_enabled);
        mListView.setAdapter(null);
        Toast.makeText(this, R.string.error_gps_not_enabled, Toast.LENGTH_SHORT).show();
    }

    public void errorLocating() {
        mStatusView.setText(R.string.error_location_failed);
        mListView.setAdapter(null);
        Toast.makeText(this, R.string.error_location_failed, Toast.LENGTH_SHORT).show();
    }
}
