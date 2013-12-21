package com.suresh.whereismycash;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.ListView;

@SuppressLint("NewApi")
public class SwipeListener implements OnTouchListener {
	
	private String TAG = "SwipeListener";
	private float startPosition;
	private ListView lv;
	private float swipeSlop;
	private boolean swiping = false;
	private DeleteRowListener listener;
	
	private final int SWIPE_DURATION = 300;
	private final double ACTION_THRESHOLD = 0.25;
	
	public SwipeListener(ListView lv, Context context, DeleteRowListener listener) {
		this.lv = lv;
		this.listener = listener;
		swipeSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		float deltaX = event.getX() + v.getTranslationX() - startPosition;
		float deltaXAbs = Math.abs(deltaX);
		float fractionCovered = deltaXAbs / v.getWidth(); 
		float alpha = 1 - fractionCovered;
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPosition = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if (deltaXAbs > swipeSlop) { //Yes. The user is swiping for sure!
				swiping = true;
				lv.requestDisallowInterceptTouchEvent(true);
			}
			
			if (swiping) {
				v.setTranslationX(deltaX);
				v.setAlpha(alpha);	
			}

			break;
		case MotionEvent.ACTION_UP:
			if (!swiping) {
				v.performClick();
			} else {
				float endX; //End position of the row. Left or right of screen.
				float endAlpha;
				final boolean remove;
				long animDuration;
				
				if (fractionCovered > ACTION_THRESHOLD) { //Delete the row
					animDuration = (long) ((1 - fractionCovered) * SWIPE_DURATION);
					endX = deltaX > 0 ? v.getWidth() : -v.getWidth(); //Swipe left or right
					endAlpha = 0;
					remove = true;
					lv.setEnabled(false);
				} else { //Snap back into place
					animDuration = (long) (fractionCovered * SWIPE_DURATION);
					endX = 0;
					endAlpha = 1;
					remove = false;
				}
				
				v.animate().setDuration(animDuration).alpha(endAlpha).translationX(endX).setListener(new AnimatorListener() {					
					@Override public void onAnimationStart(Animator animation) {}
					@Override public void onAnimationRepeat(Animator animation) {}
					@Override public void onAnimationCancel(Animator animation) {}
					@Override public void onAnimationEnd(Animator animation) {
						if (remove) listener.deleteRow(v);
						v.setTranslationX(0);
						v.setAlpha(1);
						lv.setEnabled(true);
					}
				});
				
				swiping = false;
			}

			break;
		}
		
		return true;
	}

	public interface DeleteRowListener {
		public void deleteRow(View v);
	}

}
