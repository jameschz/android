package com.man.module;

import com.man.cfg.CFG;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.FloatMath;

/**
 * 男人玩家
 */
public class Man extends People {

	/**
	 * 方向
	 */
	protected PointF direction;
	
	/**
	 * 目的地
	 */
	protected PointF target;
	
	/**
	 * 看向一个人
	 */
	public void forwardTo (PointF target){
		// 初始化
		if (this.target == null){
			this.target = new PointF();
		}
		if (this.direction == null){
			this.direction = new PointF();
		}
		
		// 保存目标位置
		this.target = target;
		
		// 计算方向数值
		float dtx = this.target.x - this.location.x;
		float dty = this.target.y - this.location.y;
		this.direction.x = (dtx / FloatMath.sqrt(dtx * dtx + dty * dty) * CFG.MAN_SPEED);
		this.direction.y = (dty / FloatMath.sqrt(dtx * dtx + dty * dty) * CFG.MAN_SPEED);
		
		float angle = this.getRotateAngle(this.location, this.target);
		this.setAngle(angle);
	}

	/**
	 * 向一个方向移动
	 */
	public void move(){
		// 初始化
		if (this.target == null){
			this.target = new PointF();
		}
		if (this.direction == null){
			this.direction = new PointF();
		}
		// 开始移动
		float x = location.x + direction.x;
		float y = location.y + direction.y;
		// 移动到目的地
		float dtx = x - this.target.x;
		float dty = y - this.target.y;
		if (FloatMath.sqrt(dtx * dtx + dty * dty) < 10) {
			x = location.x;
			y = location.y;
		}
		// 到边界了就不移动，也不抛异常
		if (x > CFG.SCREEN_LTX && x < CFG.SCREEN_RBX){
			location.x = x;
		}
		if (y > CFG.SCREEN_LTY && y < CFG.SCREEN_RBY){
			location.y = y;
		}
	}
	
	@Override
	public boolean collide(People people) {
		return false;
	}

	@Override
	public void drawMe(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		// 画男人
		if (image != null) {
			// 原始数值
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			float imageAngle = this.getAngle();
			// 设置旋转
			Matrix m = new Matrix();
			m.reset();
			float pivotX = imageWidth / 2;
			float pivotY = imageHeight / 2;
			float transX = location.x - pivotX;
			float transY = location.y - pivotY;
			m.setRotate(imageAngle, pivotX, pivotY);
			m.postTranslate(transX, transY);
			canvas.drawBitmap(image, m, null);
		} else {
			canvas.drawCircle(location.x, location.y, radius, paint);
		}
	}

}
