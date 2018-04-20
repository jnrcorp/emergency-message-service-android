package com.jnrcorp.ems.util;

import android.R.color;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jnrcorp.ems.R;

@SuppressLint("ClickableViewAccessibility")
public class HighlightOnTouchListener implements OnTouchListener {

	private int highlightSemaphore = 0;

	public HighlightOnTouchListener() {
		super();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_HOVER_ENTER || event.getAction() == MotionEvent.ACTION_DOWN) {
			highlightRow(v);
		} else if(event.getAction() == MotionEvent.ACTION_HOVER_EXIT || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
			unhighlightRow(v);
		}
		// must return false otherwise OnClickListner and OnLongClickListener will not be called
		return false;
	}

	public void highlightRow(View v) {
		highlightSemaphore += 1;
		v.setBackgroundResource(R.color.list_background_pressed);
	}

	public void unhighlightRow(View v) {
		if(highlightSemaphore > 0) {
			highlightSemaphore -= 1;
		}
		if(highlightSemaphore == 0) {
			v.setBackgroundResource(color.transparent);
		}
	}

}
