package com.whf.pilin.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.whf.pilin.bitmap.cache.ImageCache;
import com.whf.pilin.utils.TextSpannableUtils;

public class EmotionEditText extends EditText {
	public EmotionEditText(Context context){
		super(context);
	}
	
	public EmotionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public EmotionEditText(Context context, AttributeSet attrs,int defStyle){
		
		super(context, attrs, defStyle);
		
	}
	
	@Override
	public void setText(CharSequence text, BufferType type) {
		
		if(!TextUtils.isEmpty(text)){
			
			super.setText(TextSpannableUtils.replace(text,getContext()), type);
			
		}else{
			
			super.setText(text, type);
		}
		
		
	}
	
	
	
}
