package com.man.plug;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 时间提示
 */
public class MsgScore {
	
	/**
	 * 时间
	 */
	private int time;
	
	/**
	 * 得分
	 */
	private int score;
	
	/**
	 * 画自己
	 * 
	 * @param canvas
	 */
	public void drawMe(Canvas canvas){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 247, 211, 66);
		paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		paint.setTextSize(16);
		// 当前得分
		canvas.drawText("得分：" + time, 10, 20, paint);
		// 最高得分
		String highScoreStr = "最高分：" + score;
		int textWidth = (int) paint.measureText(highScoreStr);
		int leftPos = canvas.getWidth() - textWidth - 10;
		canvas.drawText(highScoreStr, leftPos, 20, paint);
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
