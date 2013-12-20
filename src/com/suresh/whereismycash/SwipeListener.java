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
	
	public SwipeListener(ListView lv, Context context) {
		this.lv = lv;
		swipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.v(TAG, "ACTION_DOWN");
			startPosition = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			float deltaX = event.getX() + v.getTranslationX() - startPosition;
			float deltaXAbs = Math.abs(deltaX);
			float alpha = 1 - (deltaXAbs / v.getWidth());
			
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
			v.setTranslationX(0);
			v.setAlpha(1);
			swiping = false;
			break;
		}
		
		return true;
	}

}
