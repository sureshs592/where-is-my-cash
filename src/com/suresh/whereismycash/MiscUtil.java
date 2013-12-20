package com.suresh.whereismycash;

import android.os.Build;

public class MiscUtil {

	public static boolean sdkAboveHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
	
}
