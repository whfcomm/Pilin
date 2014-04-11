package com.whf.pilin.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {
	/**
	 * ������ͨĬ�ϵ�������ʾ
	 * @param context
	 * @param textContext
	 * @param time
	 */
	public static void createNormalToast(Context context,String textContext,int time){
		
		Toast.makeText(context, textContext, time).show();
		
	}
	/**
	 * ������ͨλ�����������
	 * @param context
	 * @param textContext
	 * @param time
	 */
	public static void createCenterNormalToast(Context context,String textContext,int time){
		
		Toast toast = Toast.makeText(context, textContext, time);
		
		toast.setGravity(Gravity.CENTER, 0, 0);
		
		toast.show();
		
	}
	
}
