package com.man.plug;

import com.man.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlertGame extends AlertDialog {

	Window window;
	Context context;
	BtnCallback callback;
	
	int score = 0;
	int hscore = 0;
	
	public AlertGame(Context context) {
		super(context);
		this.context = context;
		this.window = this.getWindow();
	}
	
	public void setScore (int score) {
		this.score = score;
	}
	
	public void setHighScore (int hscore) {
		this.hscore = hscore;
	}
	
	public void setBtnCallback (BtnCallback callback) {
		this.callback = callback;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_game);
		
		TextView textScoreVal = (TextView) window.findViewById(R.id.text_score_val);
		textScoreVal.setText("" + score);
		
		TextView textHScoreVal = (TextView) window.findViewById(R.id.text_hscore_val);
		textHScoreVal.setText("" + hscore);
		
		Button btnRestart = (Button) window.findViewById(R.id.btn_restart);
		btnRestart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onRestart();
				}
				dismiss(); // ¹Ø±Õ
			}
		});
		
		Button btnBack = (Button) window.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onBack();
				}
				dismiss(); // ¹Ø±Õ
			}
		});
		
		
	}
	
	public interface BtnCallback {
		public void onRestart();
		public void onBack();
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
}