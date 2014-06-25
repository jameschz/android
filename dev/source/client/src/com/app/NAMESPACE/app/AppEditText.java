package com.app.NAMESPACE.app;

import java.util.HashMap;

import com.app.NAMESPACE.R;
import com.app.NAMESPACE.auth.AuthApp;
import com.app.NAMESPACE.base.BaseMessage;
import com.app.NAMESPACE.base.C;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AppEditText extends AuthApp {
	
	private EditText mEditText;
	private Button mEditSubmit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_edit);
		
		// show keyboard
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		
		// init ui
		mEditText = (EditText) this.findViewById(R.id.app_edit_text);
		mEditSubmit = (Button) this.findViewById(R.id.app_edit_submit);
		
		// bind action logic
		Bundle params = this.getIntent().getExtras();
		final int action = params.getInt("action");
		
		switch (action) {
			case C.action.edittext.CONFIG:
				mEditText.setText(params.getString("value"));
				mEditSubmit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String input = mEditText.getText().toString();
						customer.setSign(input); // update local member
						HashMap<String, String> urlParams = new HashMap<String, String>();
						urlParams.put("key", "sign");
						urlParams.put("val", input);
						doTaskAsync(C.task.customerEdit, C.api.customerEdit, urlParams);
					}
				});
				break;
			case C.action.edittext.COMMENT:
				final String blogId = params.getString("blogId");
				mEditSubmit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String input = mEditText.getText().toString();
						HashMap<String, String> urlParams = new HashMap<String, String>();
						urlParams.put("blogId", blogId);
						urlParams.put("content", input);
						doTaskAsync(C.task.commentCreate, C.api.commentCreate, urlParams);
					}
				});
				break;
		}
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