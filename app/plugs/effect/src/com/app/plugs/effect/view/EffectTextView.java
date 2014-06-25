package com.app.plugs.effect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class EffectTextView extends EffectView {
	
	// default text size
	private float ts = 24.0f;
	
	public EffectTextView(Context context) {
		super(context);
	}
	
	public EffectTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setTextSize(int size) {
		ts = size;
	}
	
	public void setTexts(String[] texts) {
		Context ctx = this.getContext();
		for (String text : texts) {
			// add text view
			TextView tv = new TextView(ctx);
			LayoutParams tvParams = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
			tvParams.gravity = Gravity.CENTER;
			tv.setLayoutParams(tvParams);
			tv.setTextSize(ts);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(text);
			this.addView(tv);
		}
	}
}