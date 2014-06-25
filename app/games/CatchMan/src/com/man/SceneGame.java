package com.man;

import com.man.controller.GameController;
import com.man.util.GameUtil;
import com.man.view.GameView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 主activity类
 */
public class SceneGame extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case R.id.restartGame:
				controller.newGame();
				break;
			case R.id.resumeGame:
				if (!controller.isLive()){
					controller.startGame();
				}
				break;
			case R.id.quitGame:
				controller.endGame(false);
				GameUtil.forward(this, SceneMenu.class);
				break;
			default:
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (controller.isLive()){
					controller.pauseGame();
				}
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
	private GameController controller;

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
		controller = new GameController(this);
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
	protected void onStop() {
		controller.endGame(false);
		super.onStop();
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