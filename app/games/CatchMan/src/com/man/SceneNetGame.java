package com.man;

import com.man.controller.NetGameController;
import com.man.view.GameView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;

/**
 * 主activity类
 */
public class SceneNetGame extends Activity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				break;
			case KeyEvent.KEYCODE_BACK:
				break;
			default:
				break;
		}
		return false;
	}
	
	/**
	 * 控制器
	 */
	private NetGameController controller;

	/**
	 * 游戏面板
	 */
	private GameView gameView;

	private PowerManager.WakeLock mWakeLock;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity(); // 系统初始化
		initGame(); // 游戏初始化
		setContentView(gameView);
	}

	/**
	 * 系统初始化
	 */
	private void initActivity() {
		// 屏幕常亮所需的代码
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
	}

	/**
	 * 游戏初始化
	 */
	private void initGame() {
		// 创建控制器及游戏面板对象
		Bundle args = this.getIntent().getExtras();
		controller = new NetGameController(this, args);
		gameView = new GameView(this);
		controller.setGameView(gameView);
		// 设置控制器的初始资源
		Resources res = getResources();
		Bitmap bmw1 = BitmapFactory.decodeResource(res, R.drawable.w1);
		Bitmap bmw2 = BitmapFactory.decodeResource(res, R.drawable.w2);
		// 保持图片的原始尺寸
		bmw1 = Bitmap.createScaledBitmap(bmw1, 48, 48, true);
		bmw2 = Bitmap.createScaledBitmap(bmw2, 48, 48, true);
		controller.setWomanImage(bmw1);
		controller.setManImage(bmw2);
		// 设置控制器的游戏逻辑
		gameView.setController(controller);
		// 设置控制器的控制逻辑
		gameView.setOnTouchListener(controller);
		// 开始新游戏
		controller.newGame();
	}
	
	@Override
	protected void onPause() {
		mWakeLock.release();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mWakeLock.acquire();
		super.onResume();
	}
	
	public Context getContext () {
		return this;
	}
}