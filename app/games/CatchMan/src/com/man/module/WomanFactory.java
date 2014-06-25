package com.man.module;

import com.man.cfg.CFG;

import android.graphics.PointF;

/**
 * 女人工厂
 */
public class WomanFactory {

	/**
	 * 在随机位置产生女人
	 */
	public static Woman getWoman() {
		
		//生产一个丑女人
		Woman woman = new UglyWoman();
		
		//设置女人属性
		float rnd1 = (float) Math.random();
		float rnd2 = (float) Math.random();
		float rnd3 = (float) Math.random();
		PointF point = new PointF();
		if (rnd1 > 0.5){
			//左右两边
			point.x = CFG.SCREEN_WIDTH * rnd3;
			point.y = (rnd2 > 0.5) ? 0 : CFG.SCREEN_HEIGHT;
		} else {
			//上下两边
			point.x = (rnd2 > 0.5) ? 0 : CFG.SCREEN_WIDTH;
			point.y = CFG.SCREEN_HEIGHT * rnd3;
		}
		//获得实际坐标
		point.x = CFG.getRealX(point.x);
		point.y = CFG.getRealY(point.y);
		woman.setLocation(point);
		
		//返回女人
		return woman;
	}
	
	/**
	 * 在指定位置女人
	 */
	public static Woman getWoman(float x, float y) {
		
		//生产一个丑女人
		Woman woman = new UglyWoman();
		
		//设置女人属性
		PointF point = new PointF(x, y);
		woman.setLocation(point);
		
		//返回女人
		return woman;
	}
}
