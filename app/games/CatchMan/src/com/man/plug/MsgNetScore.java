package com.man.plug;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 时间提示
 */
public class MsgNetScore {
	
	/**
	 * 角色
	 */
	private String user;
	
	/**
	 * 角色
	 */
	private String role;
	
	/**
	 * 时间
	 */
	private int time = 0;
	
	/**
	 * 赢时间
	 */
	private int winTime = 30;
	
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
		canvas.drawText("用户：" + user, 10, 20, paint);
		// 最高得分
		String timeStr = "时间：" + time + " (" + winTime + ")";
		int textWidth = (int) paint.measureText(timeStr);
		int leftPos = canvas.getWidth() - textWidth - 10;
		canvas.drawText(timeStr, leftPos, 20, paint);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public int getWinTime() {
		return winTime;
	}

	public void setWinTime(int winTime) {
		this.winTime = winTime;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
