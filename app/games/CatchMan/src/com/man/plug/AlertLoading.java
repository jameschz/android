package com.man.plug;

import java.util.Timer;
import java.util.TimerTask;

import com.app.plugs.effect.EffectUtil;
import com.app.plugs.effect.view.EffectTextView;
import com.app.plugs.effect.view.EffectView;
import com.man.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.LinearLayout;

public class AlertLoading extends AlertDialog {

	Window window;
	Context context;
	
	private static Listener listener;
	private AlertLoadingHandler handler;
	
	private LinearLayout counter;
	
	public static interface Listener {
		public void onComplete();
	}
	
	public AlertLoading(Context context, Listener listener) {
		super(context);
		this.context = context;
		this.window = this.getWindow();
		this.handler = new AlertLoadingHandler();
		AlertLoading.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_loading);
		
		counter = (LinearLayout) this.findViewById(R.id.alert_loading_counter);
		
		String[] mTexts = {"3", "2", "1", "GO"};
		EffectView mEffectView = EffectUtil.buildTextView(context, EffectTextView.ANI_UP, 100, mTexts);
		counter.addView(mEffectView);
		mEffectView.setInterval(2000);
		mEffectView.start();
		
		Timer timer = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				Message m = new Message();
				m.what = AlertLoadingTask.TASK_COMPLETE;
				handler.sendMessage(m);
				dismiss(); // ¹Ø±Õ
			}
		};
		timer.schedule(tt, 8000);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				break;
			default:
				break;
		}
		return false;
	}
	
	private static class AlertLoadingTask {
		final public static int TASK_COMPLETE = 1;
	}
	
	private static class AlertLoadingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case AlertLoadingTask.TASK_COMPLETE:
					AlertLoading.listener.onComplete();
					break;
			}
		}
	}
}