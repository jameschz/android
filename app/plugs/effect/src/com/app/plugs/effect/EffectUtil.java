package com.app.plugs.effect;

import com.app.plugs.effect.view.EffectImageView;
import com.app.plugs.effect.view.EffectTextView;
import com.app.plugs.effect.view.EffectView;

import android.content.Context;

public class EffectUtil {

	public static EffectView fillTexts(Context context, int ani, int size, String[] texts) {
		EffectTextView etv = new EffectTextView(context);
		etv.setAnimation(ani);
		etv.setTextSize(size);
		etv.setTexts(texts);
		return etv;
	}
	
	public static EffectView fillImages(Context context, int ani, int[] resIds) {
		EffectImageView eiv = new EffectImageView(context);
		eiv.setAnimation(ani);
		eiv.setResources(resIds);
		return eiv;	
	}
	
	
}