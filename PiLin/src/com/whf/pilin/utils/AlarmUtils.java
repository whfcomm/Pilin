package com.whf.pilin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtils {
	/**
	 * ����һ����������
	 * @param manager
	 * @param context
	 * @param reciverClsss
	 */
	public static void startAlarm(AlarmManager manager,Context context,Class reciverClsss,long triggerMillis,long intervalMillis){
		
		Intent intent = new Intent(context,reciverClsss);
		
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis, intervalMillis, pintent);
		
	}
	/**
	 * �ر�һ����������
	 * @param manager
	 * @param context
	 * @param reciverClsss
	 */
	public static void stopAlarm(AlarmManager manager,Context context,Class reciverClsss){
		
		Intent intent = new Intent(context,reciverClsss);
		
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		manager.cancel(pintent);
		
	}
	
	
}
