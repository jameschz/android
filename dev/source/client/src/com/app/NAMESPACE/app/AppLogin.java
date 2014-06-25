package com.app.NAMESPACE.app;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.app.NAMESPACE.R;
import com.app.NAMESPACE.base.BaseApp;
import com.app.NAMESPACE.base.BaseAuth;
import com.app.NAMESPACE.base.BaseMessage;
import com.app.NAMESPACE.base.BaseService;
import com.app.NAMESPACE.base.C;
import com.app.NAMESPACE.model.Customer;
import com.app.NAMESPACE.service.NoticeService;

public class AppLogin extends BaseApp {

	private EditText mEditName;
	private EditText mEditPass;
	private CheckBox mCheckBox;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check if login
		if (BaseAuth.isLogin()) {
			this.forward(AppIndex.class);
		}
		
		// set view after check login
		setContentView(R.layout.app_login);
		
		// remember password
		mEditName = (EditText) this.findViewById(R.id.app_login_edit_name);
		mEditPass = (EditText) this.findViewById(R.id.app_login_edit_pass);
		mCheckBox = (CheckBox) this.findViewById(R.id.app_login_check_remember);
		settings = getPreferences(Context.MODE_PRIVATE);
		if (settings.getBoolean("remember", false)) {
			mCheckBox.setChecked(true);
			mEditName.setText(settings.getString("username", ""));
			mEditPass.setText(settings.getString("password", ""));
		}
		mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = settings.edit();
				if (mCheckBox.isChecked()) {
					editor.putBoolean("remember", true);
					editor.putString("username", mEditName.getText().toString());
					editor.putString("password", mEditPass.getText().toString());
				} else {
					editor.putBoolean("remember", false);
					editor.putString("username", "");
					editor.putString("password", "");
				}
				editor.commit();
			}
		});
		
		// login submit
		OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.app_login_btn_submit : 
						doTaskLogin();
						break;
				}
			}
		};
		findViewById(R.id.app_login_btn_submit).setOnClickListener(mOnClickListener);
	}
	
	private void doTaskLogin() {
		if (mEditName.length() > 0 && mEditPass.length() > 0) {
			HashMap<String, String> urlParams = new HashMap<String, String>();
			urlParams.put("name", mEditName.getText().toString());
			urlParams.put("pass", mEditPass.getText().toString());
			try {
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		switch (taskId) {
			case C.task.login:
				Customer customer = null;
				// login logic
				try {
					customer = (Customer) message.getResult("Customer");
					// login success
					if (customer.getName() != null) {
						BaseAuth.setCustomer(customer);
						BaseAuth.setLogin(true);
					// login fail
					} else {
						BaseAuth.setCustomer(customer); // set sid
						BaseAuth.setLogin(false);
						toast(this.getString(R.string.msg_loginfail));
					}
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				// turn to index
				if (BaseAuth.isLogin()) {
					// start service
					BaseService.start(this, NoticeService.class);
					// turn to index
					forward(AppIndex.class);
				}
				break;
		}
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