package com.example.foursquaretest;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Looper;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.foursquare.application.ApplicationContextHelper;
import com.example.foursquare.model.Locate;
import com.example.foursquare.model.LocateImpl;
import com.example.foursquare.model.LocateListener;
import com.example.foursquare.view.MainViewImpl;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 * <p/>
 * <p/>
 * If you have somewhat of a legacy application, and you're not allowed to change the visibility of your methods, the best way to test private methods is to use reflection.
 * <p/>
 * Internally we're using helpers to get/set private and private static variables as well as invoke private and private static methods. The following patterns will let you do pretty much anything related to the private methods and fields. Of course you can't change private static final variables through reflection.
 * Method method = targetClass.getDeclaredMethod(methodName, argClasses);
 * method.setAccessible(true);
 * return method.invoke(targetObject, argObjects);
 * <p/>
 * And for fields:
 * Field field = targetClass.getDeclaredField(fieldName);
 * field.setAccessible(true);
 * field.set(object, value);
 */
//public class LocateTest extends ActivityInstrumentationTestCase2<LocateImpl> {
//
//public LocateTest() {
//        super((LocateImpl.class));
//        }
public class LocateTest extends ActivityInstrumentationTestCase2<MainViewImpl> implements LocateListener {

    public LocateTest() {
        super((MainViewImpl.class));
    }

    private Locate mLocate;
    private LocationManager mLocationManager;
    private double mTestLatitude = 0;
    private double mTestLongitude = 0;
    private boolean mGpsDisabledError = false;
    private boolean mLocationError = false;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = ApplicationContextHelper.getContext();
        mLocate = new LocateImpl();
        mLocate.setLocationResultListener(this);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            mLocationManager.addTestProvider(LocationManager.GPS_PROVIDER, true, true, true, true, true, true, true, 0, 5);
            mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            mLocationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper.prepare();
    }

    public void testLocation() {

        mLocate.startLocating();

        // Test location failed
        mLocationError = true;
        mLocate.getLastKnownLocation();
        // Interface set false if received
        getInstrumentation().waitForIdleSync();
        //assertEquals(false, mLocationError);

        // Test success case
        mTestLatitude = 11;
        mTestLongitude = 22;
        mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, createLocation(mTestLatitude, mTestLongitude));
        mLocate.getLastKnownLocation();

        // Test provider disabled
        mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, false);
        // We expect to receive GPS disabled error
        mGpsDisabledError = true;
        mLocate.getLastKnownLocation();
        // Interface set false if received
        getInstrumentation().waitForIdleSync();
        assertEquals(false, mGpsDisabledError);

        mLocate.stopLocating();
    }

    // Location fetching success comes here
    public void locationSuccess(String latitude, String longitude) {
        Log.d("JOE", "locationUpdated: " + longitude + " " + latitude);
        assertEquals("Latitude failed", Double.toString(mTestLatitude), latitude);
        assertEquals("Longitude failed", Double.toString(mTestLongitude), longitude);
    }

    // GPS not enabled
    public void gpsNotEnabled() {
        if (mGpsDisabledError) {
            Log.d("JOE", "gpsNotEnabled");
            assertEquals(true, mGpsDisabledError);
            mGpsDisabledError = false;
        }
    }

    // Locating failed
    public void locationError() {
        if (mLocationError) {
            Log.d("JOE", "locationError");
            assertEquals(true, mLocationError);
            mLocationError = false;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mLocate = null;
    }

    private Location createLocation(double latitude, double longitude) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        location.setAccuracy(1);
        location.setAltitude(10);
        location.setBearing(100);
        location.setSpeed(100);
        location.setElapsedRealtimeNanos(System.currentTimeMillis());
        return location;
    }
}