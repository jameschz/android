package com.app.weibo.app;

import java.util.HashMap;

import com.app.weibo.R;
import com.app.weibo.auth.AuthApp;
import com.app.weibo.base.BaseMessage;
import com.app.weibo.base.C;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AppEditBlog extends AuthApp {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_write);
		
		// show keyboard
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		
		// bind action logic
		findViewById(R.id.app_write_submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText mWriteText = (EditText) findViewById(R.id.app_write_text);
				HashMap<String, String> urlParams = new HashMap<String, String>();
				urlParams.put("content", mWriteText.getText().toString());
				doTaskAsync(C.task.blogCreate, C.api.blogCreate, urlParams);
			}
		});
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		doFinish();
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// other methods
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}