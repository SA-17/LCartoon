package com.lpi.lcartoon.CL;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import androidx.annotation.NonNull;

public abstract class OnEditTextRightDrawableTouchListener implements OnTouchListener {
	
	private final EditText mEditText;
	
	public OnEditTextRightDrawableTouchListener(@NonNull final EditText editText) {
		mEditText = editText;
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			final int DRAWABLE_RIGHT_POSITION = 2;
			final Drawable drawable = mEditText.getCompoundDrawables()[DRAWABLE_RIGHT_POSITION];
			if (drawable != null) {
				final float touchEventX = motionEvent.getX();
				final int touchAreaRight = mEditText.getRight();
				final int touchAreaLeft = touchAreaRight - drawable.getBounds().width();
				if (touchEventX >= touchAreaLeft && touchEventX <= touchAreaRight) {
					view.performClick();
					OnDrawableClick();
				}
				return true;
			}
		}
		return false;
	}
	
	public abstract void OnDrawableClick();
}