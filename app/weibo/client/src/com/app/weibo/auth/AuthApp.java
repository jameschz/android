package com.app.weibo.auth;

import com.app.weibo.R;
import com.app.weibo.app.AppBlogs;
import com.app.weibo.app.AppConfig;
import com.app.weibo.app.AppIndex;
import com.app.weibo.app.AppLogin;
import com.app.weibo.base.BaseApp;
import com.app.weibo.base.BaseAuth;
import com.app.weibo.demo.DemoMap;
import com.app.weibo.demo.DemoWeb;
import com.app.weibo.model.Customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class AuthApp extends BaseApp {
	
	private final int MENU_APP_WRITE = 0;
	private final int MENU_APP_LOGOUT = 1;
	private final int MENU_APP_ABOUT = 2;
	private final int MENU_DEMO_WEB = 3;
	private final int MENU_DEMO_MAP = 4;
	
	protected static Customer customer = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!BaseAuth.isLogin()) {
			this.forward(AppLogin.class);
			this.onStop();
		} else {
			customer = BaseAuth.getCustomer();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		this.bindMainTop();
		this.bindMainTab();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_APP_WRITE, 0, R.string.menu_app_write).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENU_APP_LOGOUT, 0, R.string.menu_app_logout).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(0, MENU_APP_ABOUT, 0, R.string.menu_app_about).setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(0, MENU_DEMO_WEB, 0, R.string.menu_demo_web).setIcon(android.R.drawable.ic_menu_view);
		menu.add(0, MENU_DEMO_MAP, 0, R.string.menu_demo_map).setIcon(android.R.drawable.ic_menu_view);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_APP_WRITE: {
				doEditBlog();
				break;
			}
			case MENU_APP_LOGOUT: {
				doLogout(); // do logout first
				forward(AppLogin.class);
				break;
			}
			case MENU_APP_ABOUT:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.menu_app_about);
				String appName = this.getString(R.string.app_name);
				String appVersion = this.getString(R.string.app_version);
				builder.setMessage(appName + " " + appVersion);
				builder.setIcon(R.drawable.face);
				builder.setPositiveButton(R.string.btn_cancel, null);
				builder.show();
				break;
			case MENU_DEMO_WEB:
				forward(DemoWeb.class);
				break;
			case MENU_DEMO_MAP:
				forward(DemoMap.class);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void bindMainTop () {
		Button bTopQuit = (Button) findViewById(R.id.main_top_quit);
		if (bTopQuit != null) {
			OnClickListener mOnClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.main_top_quit:
							doFinish();
							break;
					}
				}
			};
			bTopQuit.setOnClickListener(mOnClickListener);
		}
	}
	
	private void bindMainTab () {
		ImageButton bTabHome = (ImageButton) findViewById(R.id.main_tab_1);
		ImageButton bTabBlog = (ImageButton) findViewById(R.id.main_tab_2);
		ImageButton bTabConf = (ImageButton) findViewById(R.id.main_tab_3);
		ImageButton bTabWrite = (ImageButton) findViewById(R.id.main_tab_4);
		if (bTabHome != null && bTabBlog != null && bTabConf != null) {
			OnClickListener mOnClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.main_tab_1:
							forward(AppIndex.class);
							break;
						case R.id.main_tab_2:
							forward(AppBlogs.class);
							break;
						case R.id.main_tab_3:
							forward(AppConfig.class);
							break;
						case R.id.main_tab_4:
							doEditBlog();
							break;
					}
				}
			};
			bTabHome.setOnClickListener(mOnClickListener);
			bTabBlog.setOnClickListener(mOnClickListener);
			bTabConf.setOnClickListener(mOnClickListener);
			bTabWrite.setOnClickListener(mOnClickListener);
		}
	}
}