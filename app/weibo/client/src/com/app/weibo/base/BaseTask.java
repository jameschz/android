package com.app.weibo.base;

public class BaseTask extends Thread {

	public static final int TASK_COMPLETE = 0;
	public static final int NETWORK_ERROR = 1;
	public static final int SHOW_LOADBAR = 2;
	public static final int HIDE_LOADBAR = 3;
	public static final int SHOW_TOAST = 4;
	public static final int LOAD_IMAGE = 5;
	
	public int taskId = -1;
	
	public BaseTask () {}
	
	public void setTaskId (int id) {
		this.taskId = id;
	}
	
	public int getTaskId () {
		return this.taskId;
	}
	
	public void onStart () {
//		Log.w("BaseTask", "onStart");
	}
	
	public void onComplete () {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onComplete (String httpResult) {
//		Log.w("BaseTask", "onComplete");
	}
	
	public void onError (String error) {
//		Log.w("BaseTask", "onError");
	}
	
	public void onStop () throws Exception {
//		Log.w("BaseTask", "onStop");
	}
	
}