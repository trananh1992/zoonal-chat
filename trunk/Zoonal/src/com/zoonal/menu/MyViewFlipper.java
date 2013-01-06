package com.zoonal.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.zoonal.R;

public class MyViewFlipper extends ViewFlipper {

	static final String logTag = "ViewFlipper";
	static final int MIN_DISTANCE = 30;
	private float downX, downY, upX, upY;
	Animation slideLeftIn;
	Animation slideLeftOut;
	Animation slideRightIn;
	Animation slideRightOut;
	Context context;
	ViewFlipper viewFlipper;

	public MyViewFlipper(Context context) {
		super(context);
		viewFlipper = this;
		this.context = context;
		System.out.println("I am in MyFlipper() counstructor...");
	}

	public MyViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		viewFlipper = this;
		System.out.println("I am in MyFlipper() counstructor...");
		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.anim.push_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.anim.push_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.anim.push_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.anim.push_right_out);
	}

	public void onRightToLeftSwipe() {
		viewFlipper.setInAnimation(slideLeftIn);
		viewFlipper.setOutAnimation(slideLeftOut);
		viewFlipper.showNext();
	}

	public void onLeftToRightSwipe() {
		viewFlipper.setInAnimation(slideRightIn);
		viewFlipper.setOutAnimation(slideRightOut);
		viewFlipper.showPrevious();
	}

	public void onTopToBottomSwipe() {
		Log.i(logTag, "onTopToBottomSwipe!");
	}

	public void onBottomToTopSwipe() {
		Log.i(logTag, "onBottomToTopSwipe!");
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// Here if true then Flipping done
		// And if false then click event done.
		return true;
	}
}