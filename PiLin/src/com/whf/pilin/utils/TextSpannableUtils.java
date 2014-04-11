package com.whf.pilin.utils;

import java.lang.ref.SoftReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.bitmap.cache.ImageResizer;

public class TextSpannableUtils {

	public static Pattern buildPattern(){
		
		StringBuilder patterString = new StringBuilder(PiLinApplication.mEmotions.size() * 3);
		
		patterString.append("(");
		
		for(int i = 0; i < PiLinApplication.mEmotions.size(); i++){
			
			String s = PiLinApplication.mEmotions.get(i);
			
			patterString.append(Pattern.quote(s));
			
			patterString.append("|");
			
		}
		
		patterString.replace(patterString.length() - 1, patterString.length(), ")");
		
		return Pattern.compile(patterString.toString());
	}
	
	private static Bitmap getBitmap(String key,int res,Context context){
		SoftReference<Bitmap> soft = PiLinApplication.getInstance().mSendbarCache.get(key);
		Bitmap bitmap = null;
		if(soft == null){
			bitmap = ImageResizer.decodeSampledBitmapFromResource(context.getResources(), res, 60, 60);
			soft = new SoftReference<Bitmap>(bitmap);
			PiLinApplication.getInstance().mSendbarCache.put(key,soft);
		}else{
			bitmap = soft.get();
			if(bitmap == null){
				bitmap = ImageResizer.decodeSampledBitmapFromResource(context.getResources(), res, 60, 60);
				soft = new SoftReference<Bitmap>(bitmap);
				PiLinApplication.getInstance().mSendbarCache.put(key,soft);
			}
		}
		return bitmap;
	}
	
	/**
	 * 将特定文字替换为表情
	 * @param text
	 * @return
	 */
	public static CharSequence replace(CharSequence text,Context context){
		
		try {
			SpannableStringBuilder builder = new SpannableStringBuilder(text);
			
			Pattern pattern = buildPattern();
			
			Matcher matcher = pattern.matcher(text);
			
			while(matcher.find()){
				
				if(PiLinApplication.mEmotions_Id.containsKey(matcher.group())){
					
					int id = PiLinApplication.mEmotions_Id.get(matcher.group());
					
					Bitmap bitmap = getBitmap(matcher.group(),id,context);
					
					if(bitmap != null){
						
						ImageSpan span = new ImageSpan(context, bitmap);
						
						builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						
					}
					
				}
				
				
			}
			return builder;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return text;
		}
		
	}
	
}
