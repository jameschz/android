package com.app.NAMESPACE.demo;

import android.webkit.WebView;

import com.app.NAMESPACE.R;
import com.app.NAMESPACE.base.BaseWebApp;
import com.app.NAMESPACE.base.C;

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