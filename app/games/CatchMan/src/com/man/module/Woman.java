package com.man.module;

import com.man.cfg.CFG;

import android.graphics.PointF;
import android.util.FloatMath;

/**
 * 女人
 */
public abstract class Woman extends People{

	/**
	 * 方向
	 */
	protected PointF direction;

	/**
	 * 看向一个人，一般用于在刚刚出来的时候，将自己的方向朝向男人
	 * @param people
	 */
	public void lookPeople(People people){
		if (this.direction == null){
			this.direction = new PointF();
		}
		float dtx = people.location.x - this.location.x;
		float dty = people.location.y - this.location.y;
		
		this.direction.x = (dtx / FloatMath.sqrt(dtx * dtx + dty * dty) * CFG.WOMAN_SPEED);// TODO 这里可以优化
		this.direction.y = (dty / FloatMath.sqrt(dtx * dtx + dty * dty) * CFG.WOMAN_SPEED);
		
		float angle = this.getRotateAngle(this.location, people.location);
		this.setAngle(angle);
	}

	/**
	 * 根据自身方向移动
	 */
	public void move() throws PeopleMoveException{
		//如果出了边界就抛异常
		float x = location.x + direction.x;
		float y = location.y + direction.y;
		if ((x < CFG.SCREEN_LTX || x > CFG.SCREEN_RBX) || (y < CFG.SCREEN_LTY || y > CFG.SCREEN_RBY)){
			throw new PeopleMoveException();
		}
		location.x = x;
		location.y = y;
	}

	/**
	 * 检测碰撞
	 */
	@Override
	public boolean collide(People people) {
		//检测两个圆是否相交
		float x = people.location.x - this.location.x;
		float y = people.location.y - this.location.y;
		float distance = FloatMath.sqrt(x * x + y * y);
		return distance < people.radius + this.radius;
	}
	
}
