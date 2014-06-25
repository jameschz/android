package com.app.weibo.demo;

import android.webkit.WebView;

import com.app.weibo.R;
import com.app.weibo.base.BaseWebApp;
import com.app.weibo.base.C;

public class DemoWeb extends BaseWebApp {
	
	private WebView mWebView;
	
	@Override
	public void onStart() {
		super.onStart();
		
		// start loading webview
		setContentView(R.layout.demo_web);
		mWebView = (WebView) findViewById(R.id.web_form);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(C.web.index);
		
		this.setWebView(mWebView);
		this.startWebView();
	}
}