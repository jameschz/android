package com.man;

import com.man.cfg.CFG;
import com.man.util.GameUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SceneMenu extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_menu);
		
		CFG.autoAdaption(this); // device adaption
		
		Button btnStart = (Button) this.findViewById(R.id.btn_start);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startGame();
			}
		});
		
		Button btnEnter = (Button) this.findViewById(R.id.btn_enter);
		btnEnter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				enterHall();
			}
		});
		
		Button btnBack = (Button) this.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				quitGame();
			}
		});
	}
	
	public void startGame() {
		GameUtil.forward(this, SceneGame.class);
	}
	
	public void enterHall() {
		GameUtil.forward(this, SceneHall.class);
	}
	
	public void quitGame() {
		this.finish();
	}
}