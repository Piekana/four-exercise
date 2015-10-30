package com.example.foursquaretest.util;

import android.util.Log;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

class CustomMatcher {

    public static Matcher matches(final Object expected, final int temp){

        return new BaseMatcher() {

            final Object theExpected = expected;
            final int theTemp = temp;

            public boolean matches(Object o) {
                Log.d("JOE", "temp:" + Integer.toString(theTemp));
                return theExpected.equals(o);
            }

            public void describeTo(Description description) {
                description.appendText(theExpected.toString());
            }
        };
    }
}
