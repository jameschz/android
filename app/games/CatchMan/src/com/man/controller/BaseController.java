package com.man.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.os.Handler;

import com.man.SceneMenu;
import com.man.cfg.CFG;
import com.man.module.Man;
import com.man.module.PeopleMoveException;
import com.man.module.UglyWoman;
import com.man.module.Woman;
import com.man.module.WomanFactory;
import com.man.plug.AlertGame;
import com.man.plug.MsgNetScore;
import com.man.plug.MsgScore;
import com.man.util.GameUtil;
import com.man.view.GameView;

/**
 * 游戏控制器
 */
public class BaseController implements Runnable {
	
	/**
	 * 男人
	 */
	protected Man man;

	/**
	 * 女人们
	 */
	protected List<Woman> women;

	/**
	 * 男人图片
	 */
	private Bitmap manImage = null;
	
	/**
	 * 女人图片
	 */
	private Bitmap womanImage = null;
	
	/**
	 * 游戏画布
	 */
	protected GameView gameView;

	/**
	 * Handler
	 */
	protected Handler handler;

	/**
	 * Context
	 */
	protected Context context;
	
	/**
	 * Handler
	 */
	protected Activity activity;
	
	/**
	 * 单机游戏提示器
	 */
	protected MsgScore msgScore;

	/**
	 * 网络游戏提示器
	 */
	protected MsgNetScore msgNetScore;
	
	/**
	 * 游戏时间（秒）
	 */
	protected float gameTime = 0;
	
	/**
	 * 游戏是否运行
	 */
	private volatile boolean isLive = false;
	
	/**
	 * 构造一个控制器
	 */
	public BaseController(Activity activity) {
		this.handler = new Handler();
		this.activity = activity;
		this.context = activity;
	}

	/**
	 * 是否正在运行
	 * @return
	 */
	public void setLive(boolean value) {
		isLive = value;
	}
	
	/**
	 * 是否正在运行
	 * @return
	 */
	public boolean isLive() {
		return isLive;
	}
	
	public void setManImage(Bitmap bm) {
		this.manImage = bm;
	}
	
	public void setWomanImage(Bitmap bm) {
		this.womanImage = bm;
	}
	
	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	/**
	 * 重绘场景
	 */
	public void drawAll(Canvas canvas) {
		// 优化锯齿
		PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		canvas.setDrawFilter(pfd);
		// 画出男人
		man.drawMe(canvas);
		// 画出女人们
		if (women.size() > 0) {
			for (Woman woman : women) {
				woman.drawMe(canvas);
			}
		}
		// 画出单机游戏提示器
		if (msgScore != null) {
			msgScore.drawMe(canvas);
		}
		// 画出网络游戏提示器
		else if(msgNetScore != null) {
			msgNetScore.drawMe(canvas);
		}
	}
	
	/**
	 * 游戏主逻辑
	 */
	@Override
	public void run() {
		// 在子类里实现
	}

	/**
	 * 单机游戏主逻辑
	 */
	protected void runGame() {
		// 定时执行
		handler.postDelayed(this, CFG.SCREEN_DELAY);

		// 游戏计时
		gameTime += CFG.SCREEN_STIME / 1000.0;
		msgScore.setTime((int) gameTime);
		
		// 游戏执行
		if (isLive()) {
			// 移动男人
			man.move();
			// 移除出界女人
			removeWomen();
			// 产生若干个女人
			createWomen();
			// 重绘画面
			// GameView.redraw -> GameView.onDraw -> Controller.drawAll
			gameView.redraw();
			// 检测碰撞
			checkCollide();
		}
	}
	
	/**
	 * 对所有的人物检测碰撞
	 */
	protected void checkCollide() {
		// 如果丑女人碰撞了男人则游戏结束
		if (women.size() > 0) {
			for (Woman woman : women) {
				if (woman.collide(man)) {
					if (woman instanceof UglyWoman) {
						endGame(true);
					}
				}
			}
		}
	}
	
	protected Man newMan() {
		// 产生男人
		man = new Man();
		man.setImage(manImage); // 设置图片
		float centerX = CFG.getRealX(CFG.SCREEN_WIDTH / 2);
		float centerY = CFG.getRealY(CFG.SCREEN_HEIGHT / 2);
		man.setLocation(new PointF(centerX, centerY));
		return man;
	}
	
	protected Man newMan(float x, float y) {
		// 产生男人
		man = new Man();
		man.setImage(manImage); // 设置图片
		man.setLocation(new PointF(x, y));
		return man;
	}
	
	protected Woman newWoman() {
		Woman woman = WomanFactory.getWoman();
		woman.setImage(womanImage); // 设置图片
		woman.lookPeople(man); // 让女人看向男人
		return woman;
	}
	
	protected Woman newWoman(float x, float y) {
		Woman woman = WomanFactory.getWoman(x, y);
		woman.setImage(womanImage); // 设置图片
		woman.lookPeople(man); // 让女人看向男人
		return woman;
	}
	
	protected void addWoman(Woman woman) {
		women.add(woman);
	}
	
	protected void createWomen() {
		// AI：产生若干个女人
		if (women.size() < CFG.GAME_LEVEL[((int) (gameTime / 10)) % CFG.GAME_LEVEL.length]) {
			int tmpCount = CFG.GAME_LEVEL[((int) (gameTime / 10)) % CFG.GAME_LEVEL.length] - women.size();
			for (int i = 0; i < tmpCount; i++) {
				women.add(newWoman());
			}
		}
	}
	
	protected void removeWomen() {
		// 需要移除的
		List<Woman> removeWomen = null;
		
		// 移动女人们
		if (women.size() > 0) {
			for (Woman woman : women) {
				try {
					woman.move();
				} catch (PeopleMoveException e) {
					// 如果这个女人出界了
					if (removeWomen == null) {
						removeWomen = new ArrayList<Woman>();
					}
					removeWomen.add(woman);
				}
			}
		}
		
		// 移除出界的
		if (removeWomen != null) {
			women.removeAll(removeWomen);
		}
	}
	
	/**
	 * 新游戏
	 */
	public void newGame() {
		// 产生一个男人
		man = newMan();
		// 产生一个女人
		women = new ArrayList<Woman>();
		// 计时
		gameTime = 0;
		// 创建一个时间提示器
		msgScore = new MsgScore();
		msgScore.setTime((int) gameTime);
		msgScore.setScore(GameUtil.getHighScore(activity));
		// 开始
		startGame();
	}
	
	/**
	 * 开始游戏
	 */
	public void startGame() {
		handler.removeCallbacks(this);
		handler.post(this);
		setLive(true);
	}

	/**
	 * 暂停游戏
	 */
	public void pauseGame() {
		handler.removeCallbacks(this);
		setLive(false);
	}
	
	/**
	 * 结束游戏
	 * @param showDialog
	 */
	public void endGame(boolean showDialog) {
		// 设置标志
		setLive(false);
		// 停止监听
		handler.removeCallbacks(this);
		// 记录分数
		int thisScore = (int) gameTime;
		int highScore = GameUtil.getHighScore(activity);
		if (thisScore > highScore) {
			highScore = thisScore;
			GameUtil.setHighScore(activity, thisScore);
		}
		// 显示结果
		if (showDialog) {
			AlertGame alertGame = new AlertGame(context);
			alertGame.setScore(thisScore);
			alertGame.setHighScore(highScore);
			alertGame.setBtnCallback(new AlertGame.BtnCallback(){
				@Override
				public void onRestart() {
					newGame();
				}
				@Override
				public void onBack() {
					GameUtil.forward(activity, SceneMenu.class);
				}
			});
			alertGame.show();
		}
	}
}