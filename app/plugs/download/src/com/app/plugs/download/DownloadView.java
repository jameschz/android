package com.app.plugs.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadView extends AlertDialog {

	private static final int MSG_START = 0;
	private static final int MSG_STOP = 1;
	private static final int MSG_END = 2;
	private static final int MSG_HALT = 3;
	
	private String URL = null; // 下载地址
	private int TIMEOUT = 10000; // 下载超时
	private String SD_PATH = null; // 保存路径
	private String SD_FILE = null; // 保存文件
	private String downloadFile = null;
	
	private ProgressBar barShow;
	private TextView txtShow;
	private static int nowSize = 0;
	private static int maxSize = 0;
	private DownloadListener downloadListener;
	private DownloadThread downloadThread;
	private Context context;
	
	/**
	 * 消息的处�?
	 */
	private Handler mHandler = new Handler() {
		// 处理消息handleMessage
		@Override
		public void handleMessage(Message msg) {
			if (barShow != null) {
				maxSize = msg.arg1;
				// 设置进度条最大
				barShow.setMax(maxSize);
				switch (msg.what) {
					case MSG_START:
						// 下载进行中，更新进度条
						nowSize += msg.arg2;
						barShow.setProgress(nowSize);
						txtShow.setText("已下载"+ ((nowSize * 100) / maxSize) + "%");
						break;
					case MSG_STOP:
						stopDownload();
						break;
					case MSG_END:
						// 提示下载完成
						endDownload();
						break;
					case MSG_HALT:
						haltDownload();
						break;
				}
			}
			super.handleMessage(msg);
		}

	};
	
	public DownloadView(Context context, String downloadUrl, String saveFileName) {
		super(context);
		this.context = context;
		
		URL = downloadUrl;
		SD_FILE = saveFileName;
		SD_PATH = Environment.getExternalStorageDirectory().getPath();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化UI
		DownloadLayout layout = new DownloadLayout(context);
		setContentView(layout);
		txtShow = layout.getTextView();
		barShow = layout.getProgressBar();
	}

	@Override
	protected void onStop() {
		super.onStop();
		stop();
	}
	
	/**
	 * 开始下载
	 */
	private void startDownload(){
		this.show();
		if (this.URL == null || SD_FILE == null || SD_PATH == null) {
			txtShow.setText("Failed to download.");
		} else {
			barShow.setVisibility(View.VISIBLE);
			downloadThread = new DownloadThread();
			downloadThread.start();
		}
	}

	/**
	 * 停止下载
	 */
	private void stopDownload() {
		downloadThread.interrupt();
		nowSize = 0;
		maxSize = 0;
		txtShow.setText("已停止");
//		this.dismiss();
	}
	
	/**
	 * 停止下载
	 */
	private void haltDownload() {
		downloadThread.interrupt();
		nowSize = 0;
		maxSize = 0;
		txtShow.setText("已终止");
		this.dismiss();
	}
	
	/**
	 * 下载完成后执行的任务
	 */
	private void endDownload() {
		nowSize = 0;
		maxSize = 0;
		txtShow.setText("已完成");
		downloadListener.onComplete(downloadFile);
		this.dismiss();
	}
	
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setDownloadListener(DownloadListener listener) {
		downloadListener = listener;
	}
	
	/**
	 * 回调子类
	 */
	public interface DownloadListener {
		public void onComplete(String downloadFile);
		public void onError(String errorMsg);
	}
	
	/**
	 * 中止下载
	 */
	public void start() {
		startDownload();
	}
	
	/**
	 * 中止下载
	 */
	public void stop() {
		Message msg = new Message();
		msg.what = MSG_STOP;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 中止下载
	 */
	public void halt() {
		Message msg = new Message();
		msg.what = MSG_HALT;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 安装程序
	 */
	public void install() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + SD_PATH + "/" + SD_FILE),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 下载子线程
	 */
	private class DownloadThread extends Thread {
		@Override
		public void run() {
			InputStream is = null;
			try {
				// 打开下载地址
				URL myUrl = new URL(URL);
				URLConnection connection = myUrl.openConnection();
				connection.setConnectTimeout(TIMEOUT);
				connection.connect();
				// 得到访问内容并保存在输入流中
				is = connection.getInputStream();
				// 得到文件的长度，这里有可能因得不到文件大小而抛出异常
				int size = connection.getContentLength();
				if (is != null) {
					// 存储目标文件路径
					downloadFile = SD_PATH +  "/" + SD_FILE;
					File file = new File(downloadFile);
					// 如果文件存在，则删除该文件
					if (file.exists()) {
						file.delete();
					}
					// RandomAccessFile类，可以从指定访问位置，为以后实现断点下载提供支持
					RandomAccessFile randomAccessFile = new RandomAccessFile(
							downloadFile, "rwd");
					byte[] buffer = new byte[4096];
					int length = -1;
					while ((length = is.read(buffer)) != -1) {
						randomAccessFile.write(buffer, 0, length);
						Message msg = new Message();
						msg.arg1 = size;// 将文件大小保存
						// 用what变量来标示当前的状态
						msg.what = MSG_START;
						// arg2标示本次循环完成的进度
						msg.arg2 = length;
						mHandler.sendMessage(msg);
					}
					// 结束以后，标记为结束状态
					Message msg = new Message();
					msg.what = MSG_END;
					mHandler.sendMessage(msg);
					// 释放资源
					is.close();
					randomAccessFile.close();
				}
			} catch (Exception e) {
				downloadListener.onError(e.toString());
			} finally {
				
			}
		}
	}
	
	/**
	 * 动态模板
	 */
	public static class DownloadLayout extends LinearLayout {
		private TextView tv;
		private ProgressBar pb;
		public TextView getTextView() {
			return tv;
		}
		public ProgressBar getProgressBar() {
			return pb;
		}
		public DownloadLayout(Context context) {
			super(context);
			// init layout
			Drawable bgImage = this.getDrawable("plugs_download_bg.png");
			this.setBackgroundDrawable(bgImage);
			LayoutParams params = new LayoutParams(200, 60);
			this.setLayoutParams(params);
			this.setOrientation(VERTICAL);
			this.setGravity(Gravity.LEFT);
			this.setPadding(10, 10, 10, 10);
			// add text view
			tv = new TextView(context);
			LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tvParams.bottomMargin = 5;
			tv.setLayoutParams(tvParams);
			tv.setText("开始下载");
			this.addView(tv);
			// add progress bar
			pb = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
			LayoutParams pbParams = new LayoutParams(180, 15);
			pb.setLayoutParams(pbParams);
			pb.setVisibility(INVISIBLE);
			this.addView(pb);
		}
		private Drawable getDrawable(String fileName) {
			Bitmap bitmap = null;
			try {
				String filePath = "com/app/plugs/download/res/" + fileName;
				InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
				if (null == bitmap.getNinePatchChunk()) {
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					return drawable;
				} else {
					Rect padding = new Rect();
					byte[] chunk = bitmap.getNinePatchChunk();
					NinePatchDrawable drawable = new NinePatchDrawable(bitmap, chunk, padding, null);
					return drawable;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}