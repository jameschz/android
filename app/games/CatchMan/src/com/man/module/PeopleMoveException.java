package com.man.module;

/**
 * 人物移动异常
 */
public class PeopleMoveException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "超出边界，不能再移动了";
	}
	
	

}
