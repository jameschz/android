package com.man.module;

import com.man.cfg.CFG;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.FloatMath;

/**
 * 人
 */
public abstract class People {

	/**
	 * 位置
	 */
	protected PointF location;

	/**
	 * 大小(半径)
	 */
	protected float radius = CFG.PEOPLE_RADIUS;

	/**
	 * 背景图像
	 */
	protected Bitmap image = null;

	/**
	 * 背景角度
	 */
	protected float angle = 0.0f;

	/**
	 * 将自己画在画布上
	 * 
	 * @param canvas
	 */
	public abstract void drawMe(Canvas canvas);

	/**
	 * 检测与另一个人是否碰撞
	 */
	public abstract boolean collide(People people);

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public PointF getLocation() {
		return location;
	}

	public void setLocation(PointF location) {
		this.location = location;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap bm) {
		this.image = bm;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float a) {
		this.angle = a;
	}

	/**
	 * 计算两点与垂直方向夹角
	 * @param p1
	 * @param p2
	 * @return
	 */
	public float getRotateAngle(PointF p1, PointF p2) {
		float tran_x = p1.x - p2.x;
		float tran_y = p1.y - p2.y;
		float angle = (float) (Math.asin(tran_x
				/ FloatMath.sqrt(tran_x * tran_x + tran_y * tran_y)) * 180 / Math.PI);
//		return angle;
		float degree = 0.0f;
		if (!Float.isNaN(angle)) {
			if (tran_x >= 0 && tran_y <= 0) {// 第一象限
				degree = angle;
			} else if (tran_x >= 0 && tran_y >= 0) {// 第二象限
				degree = 180 - angle;
			} else if (tran_x <= 0 && tran_y >= 0) {// 第三象限
				degree = 180 - angle;
			} else if (tran_x <= 0 && tran_y <= 0) {// 第四象限
				degree = 360 + angle;
			}
		}
		return degree;
	}
}
