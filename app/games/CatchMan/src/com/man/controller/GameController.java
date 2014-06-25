package com.man.controller;

import android.app.Activity;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * ¼üÅÌ¿ØÖÆÆ÷
 */
public class GameController extends BaseController implements OnTouchListener {

	public GameController(Activity activity) {
		super(activity);
	}
	
	/**
	 * ÓÎÏ·Ö÷Âß¼­
	 */
	@Override
	public void run() {
		runGame();
	}
	
	/**
	 * ÓÃ»§²Ù¿ØÂß¼­
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (this.isLive()) {
			float x = event.getX();
			float y = event.getY();
			PointF direction = new PointF(x, y);
			man.forwardTo(direction);
//			man.move();
		}
		return false;
	}
	
}
