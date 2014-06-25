package com.app.NAMESPACE.demo;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.NAMESPACE.R;
import com.app.NAMESPACE.base.BaseWebApp;
import com.app.NAMESPACE.base.C;

public class DemoMap extends BaseWebApp {
	
	private WebView mWebViewMap;
	
	@Override
	public void onStart() {
		super.onStart();
		
		// start loading webview
		setContentView(R.layout.demo_map);
		final String centerURL = "javascript:centerAt(31.237141,121.501622);";
		mWebViewMap = (WebView) findViewById(R.id.web_map);
		mWebViewMap.getSettings().setJavaScriptEnabled(true);
		mWebViewMap.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url){
				mWebViewMap.loadUrl(centerURL);
			}
		});
		mWebViewMap.loadUrl(C.web.gomap);
		
		this.setWebView(mWebViewMap);
		this.startWebView();
	}	
}