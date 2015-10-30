package com.example.foursquaretest;

import android.widget.EditText;

import com.example.foursquare.BuildConfig;
import com.example.foursquare.R;
import com.example.foursquare.view.MainViewImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ApplicationTest {
    private MainViewImpl mActivity;

    @Before
    public void setup() {
        mActivity = Robolectric.buildActivity(MainViewImpl.class).create().get();
    }

    @Test
    public void activityCreated() {
        assertNotNull(mActivity);
    }

    @Test
    public void defaultTextFound() {
        String test = mActivity.getResources().getString(R.string.default_search_text);
        assertThat(test, equalTo("Looking for something?"));
    }

    @Test
    public void testButtonClick() throws Exception {
        MainViewImpl activity = Robolectric.buildActivity(MainViewImpl.class).create().get();
        EditText editView = (EditText) activity.findViewById(R.id.venue_search);
        assertNotNull(editView);
        assertThat(editView.getHint().toString(), equalTo("Type venue name here"));
    }
}
