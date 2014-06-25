package com.app.plugs.effect;

import com.app.plugs.effect.view.EffectView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.app.Activity;

public class EffectDemo extends Activity implements OnItemSelectedListener {
	
	private LinearLayout container;
	private EffectView mView;
	private String[] mTexts = {"今日新闻1", "今日新闻2", "今日新闻3"};
	private int[] mImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3};
	
	private Spinner sp1;
	private ArrayAdapter<String> aa1;
	private String[] menu1 = {"内容:文字", "内容:图片"};

	private Spinner sp2;
	private ArrayAdapter<String> aa2;
	private String[] menu2 = {"动时:300ms", "动时:1000ms", "动时:3000ms"};
	
	private Spinner sp3;
	private ArrayAdapter<String> aa3;
	private String[] menu3 = {"停时:3000ms", "停时:5000ms", "停时:10000ms"};
	
	private Spinner sp4;
	private ArrayAdapter<String> aa4;
	private String[] menu4 = {"动效:向上", "动效:向下", "动效:向左", "动效:向右", "动效:旋转", "动效:大小", "动效:渐变"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		container = (LinearLayout) this.findViewById(R.id.container);
		
		sp1 = (Spinner) findViewById(R.id.spinner1);
		aa1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menu1);
		aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(aa1);
		sp1.setOnItemSelectedListener(this);
		
		sp2 = (Spinner) findViewById(R.id.spinner2);
		aa2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menu2);
		aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp2.setAdapter(aa2);
		sp2.setOnItemSelectedListener(this);
		
		sp3 = (Spinner) findViewById(R.id.spinner3);
		aa3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menu3);
		aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp3.setAdapter(aa3);
		sp3.setOnItemSelectedListener(this);
		
		sp4 = (Spinner) findViewById(R.id.spinner4);
		aa4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, menu4);
		aa4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp4.setAdapter(aa4);
		sp4.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getAdapter() == aa1) {
			container.removeAllViews();
			switch (position) {
				case 0:
					mView = EffectUtil.fillTexts(this, EffectView.ANI_UP, 30, mTexts);
					container.addView(mView);
					mView.start();
					break;
				default:
					mView = EffectUtil.fillImages(this, EffectView.ANI_UP, mImages);
					container.addView(mView);
					mView.start();
					break;
			}
		}
		if (parent.getAdapter() == aa2) {
			switch (position) {
				case 0:
					mView.setDuration(300);
					break;
				case 1:
					mView.setDuration(1000);
					break;
				default:
					mView.setDuration(3000);
					break;
			}
		}
		if (parent.getAdapter() == aa3) {
			switch (position) {
				case 0:
					mView.setInterval(3000);
					break;
				case 1:
					mView.setInterval(5000);
					break;
				default:
					mView.setInterval(10000);
					break;
			}
		}
		if (parent.getAdapter() == aa4) {
			switch (position) {
				case 0:
					mView.setAnimation(EffectView.ANI_UP);
					break;
				case 1:
					mView.setAnimation(EffectView.ANI_DOWN);
					break;
				case 2:
					mView.setAnimation(EffectView.ANI_LEFT);
					break;
				case 3:
					mView.setAnimation(EffectView.ANI_RIGHT);
					break;
				case 4:
					mView.setAnimation(EffectView.ANI_ROTATE);
					break;
				case 5:
					mView.setAnimation(EffectView.ANI_SCALE);
					break;
				default:
					mView.setAnimation(EffectView.ANI_FADE);
					break;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
