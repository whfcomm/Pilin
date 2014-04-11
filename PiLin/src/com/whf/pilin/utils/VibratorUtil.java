package com.whf.pilin.utils;

import android.os.Vibrator;

public class VibratorUtil {
	
	public static void vibrate(final Vibrator vibrator,long mills){
		
		vibrator.vibrate(mills); 
		
	}
	
	
	public static void vibrate(final Vibrator vibrator,long [] pattern,boolean isRepeat){
		
		vibrator.vibrate(pattern, isRepeat ? 1 : -1);  
		
	}
}
