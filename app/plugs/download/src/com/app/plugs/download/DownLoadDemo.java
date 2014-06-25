package com.app.plugs.download;

import com.app.demos.download.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DownLoadDemo extends Activity {
	
	private Button btnD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 下载配置
		String downloadUrl = "http://test.gameplus.sdo.com/gtsdk/GT_SDK_Android_v1.0.0.9.zip";
//		String downloadUrl = "http://116.211.28.9/d/unknown.apk";
		String saveFileName = "GamePlus.apk";
		final DownloadView dv = new DownloadView(this, downloadUrl, saveFileName);
		dv.setDownloadListener(new DownloadView.DownloadListener(){
			@Override
			public void onComplete(String downloadFile) {
				Log.e("DownloadDemo", "onComplete:" + downloadFile);
//				dv.install();
			}
			@Override
			public void onError(String errorMsg) {
				Log.e("DownloadDemo", "onError:" + errorMsg);
				dv.halt();
			}
		});
		// 点击下载
		btnD = (Button) findViewById(R.id.btnDownLoad);
		btnD.setText("点击下载");
		btnD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dv.start();
			}
		});
	}
}