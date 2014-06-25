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

public class AlertNetGame extends AlertDialog {

	Window window;
	Context context;
	BtnCallback callback;
	
	String winner = "";
	int wscore = 0;
	
	public AlertNetGame(Context context) {
		super(context);
		this.context = context;
		this.window = this.getWindow();
	}
	
	public void setWinner (String winner) {
		this.winner = winner;
	}
	
	public void setWinScore (int wscore) {
		this.wscore = wscore;
	}
	
	public void setBtnCallback (BtnCallback callback) {
		this.callback = callback;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_netgame);
		
		TextView textWinnerVal = (TextView) window.findViewById(R.id.text_winner_val);
		textWinnerVal.setText("" + winner);
		
		TextView textWScoreVal = (TextView) window.findViewById(R.id.text_wscore_val);
		textWScoreVal.setText("" + wscore);
		
		Button btnRestart = (Button) window.findViewById(R.id.btn_quitgame);
		btnRestart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onQuit();
				}
				dismiss(); // ¹Ø±Õ
			}
		});
	}
	
	public interface BtnCallback {
		public void onQuit();
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