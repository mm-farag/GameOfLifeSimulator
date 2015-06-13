package com.quandoo.mfarag.gameoflifesimulator.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @author mfarag
 * 
 */
public class LogUtil {

	private static final String TAG = "gameoflifesimulation";

	public static boolean enableDebug = true;
	public static boolean showToast = true;

	public static void debug(String message) {
		if (enableDebug)
			Log.d(TAG, message + "");
	}

	public static void error(String message) {
		Log.e(TAG, message + "");
	}

	public static void showToast(Context context, String message) {
		if (showToast)
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
