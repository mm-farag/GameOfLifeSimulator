package com.quandoo.mfarag.gameoflifesimulator;

import android.test.ActivityInstrumentationTestCase2;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MainActivityInstrumentationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mainActivity;

    public MainActivityInstrumentationTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Getting a reference to the MainActivity of the target application
        mainActivity = getActivity();

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void test1() {

        mainActivity.clearTheGrid();
        assertEquals(mainActivity.getLivingList().size(), 0);
    }
}