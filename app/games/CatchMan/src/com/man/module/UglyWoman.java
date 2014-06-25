package com.man.module;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 丑女人
 * 玩家要躲避丑女人
 */
public class UglyWoman extends Woman {
	
	@Override
	public void drawMe(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		// 画丑女
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
