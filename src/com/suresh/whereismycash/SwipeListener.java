package com.suresh.whereismycash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ListView;

@SuppressLint("NewApi")
public class SwipeListener implements OnTouchListener {
	
	private String TAG = "SwipeListener";
	private float startPosition = 0;
	private ListView lv;
	private float swipeSlop = -1;
	private boolean swiping = false;
	
	private final int SWIPE_DURATION = 300;
	private final double ACTION_THRESHOLD = 0.25;
	
	public SwipeListener(ListView lv, Context context) {
		this.lv = lv;
		swipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float deltaX = event.getX() + v.getTranslationX() - startPosition;
		float deltaXAbs = Math.abs(deltaX);
		float fractionCovered = deltaXAbs / v.getWidth(); 
		float alpha = 1 - fractionCovered;
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "ACTION_DOWN");
			startPosition = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (deltaXAbs > swipeSlop) { //Yes. The user is swiping for sure!
				swiping = true;
				lv.requestDisallowInterceptTouchEvent(true);
			}
			
			Log.v(TAG, "ACTION_MOVE" + " DeltaX:" + deltaX + " Alpha:" + alpha);
			
			if (swiping) {
				v.setTranslationX(deltaX);
				v.setAlpha(alpha);	
			}

			break;
		case MotionEvent.ACTION_UP:
			Log.v(TAG, "ACTION_UP");
			
			if (!swiping) {
				v.performClick();
			} else {
				float endX; //End position of the row. Left or right of screen.
				float endAlpha;
				boolean remove;
				long animDuration;
				
				if (fractionCovered > ACTION_THRESHOLD) { //Delete the row
					animDuration = (long) ((1 - fractionCovered) * SWIPE_DURATION);
					endX = deltaX > 0 ? v.getWidth() : -v.getWidth(); //Swipe left or right
					endAlpha = 0; //Transparent
					remove = true;
				} else { //Snap back into place
					animDuration = (long) (fractionCovered * SWIPE_DURATION);
					endX = 0;
					endAlpha = 1;
					remove = false;
				}
				v.animate().setDuration(animDuration).alpha(endAlpha).translationX(endX);
				swiping = false;
			}

			break;
		}
		
		return true;
	}

}
