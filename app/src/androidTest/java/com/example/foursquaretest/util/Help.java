package com.example.foursquaretest.util;

class Help {
    public static long DEFAULT_DELAY = 1000;
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
        }
    }

}
