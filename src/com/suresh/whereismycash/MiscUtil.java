package com.suresh.whereismycash;

import android.os.Build;

public class MiscUtil {

	public static boolean phoneSupportsSwipe() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}
	
}
