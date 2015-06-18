package com.quandoo.mfarag.gameoflifesimulator;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MainActivityUnitTest extends ActivityUnitTestCase {

    MainActivity mainActivity;

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);

        // Getting a reference to the MainActivity of the target application
        mainActivity = (MainActivity) getActivity();
        mainActivity.onCreate(null);

    }

    @SmallTest
    public void testClearTheGridMethod() {

        mainActivity.clearTheGrid();
        assertEquals(mainActivity.getLivingList().size(), 0);
    }
}