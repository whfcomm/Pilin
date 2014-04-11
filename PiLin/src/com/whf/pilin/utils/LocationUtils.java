package com.whf.pilin.utils;

import java.util.List;

import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.whf.pilin.entity.LocalGeoPoint;

public class LocationUtils {
	
	/**
	 * 获取�??合的Provider
	 * @return
	 */
	public static Criteria getBastProvider(){
		
		Criteria criteria = new Criteria();
		
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		
		criteria.setCostAllowed(true);

		criteria.setBearingRequired(false);
		
		criteria.setSpeedRequired(true);
		
		return criteria;
		
	}
	/**
	 * �?��定位
	 * @param manager 位置管理�?
	 * @param listener 监听�?
	 * @param updatemills 更新间隔(毫秒)
	 * @param updateMiles 更新距离(�?
	 * @return
	 */
	public static Location startLocation(LocationManager manager,LocationListener listener,SharedPreferences sharedPreferences){
		
		int provider = sharedPreferences.getInt("LocationType", 0);
		String bastProvider = LocationManager.GPS_PROVIDER;
		switch (provider) {
		case 0:
			Criteria criteria = getBastProvider();
			bastProvider = manager.getBestProvider(criteria, false);
			break;
		case 1:
			bastProvider = LocationManager.GPS_PROVIDER;
			break;
			
		case 2:
			bastProvider = LocationManager.NETWORK_PROVIDER;
			break;
			
		case 3:
			bastProvider = LocationManager.PASSIVE_PROVIDER;
			break;	
		}
		manager.requestLocationUpdates(bastProvider, sharedPreferences.getInt("HZ_Mills", 15000), sharedPreferences.getInt("HZ_Miter", 0) , listener);
		return manager.getLastKnownLocation(bastProvider);
	}
	/**
	 * 停止定位
	 * @param manager 位置管理类
	 * @param listener 监听器
	 */
	public  static void stopLocation(LocationManager manager,LocationListener listener){
		if (listener!=null) manager.removeUpdates(listener);
	}
	/**
	 * 将设备经纬度转换为发向百度转换器需要的格式
	 * @param geoPoints
	 * @return
	 */
	public static String convertFromConvertedGeoPoint(List<LocalGeoPoint> geoPoints){
		
		String x,y;
		StringBuffer url = new StringBuffer("");
		for(LocalGeoPoint geoPoint:geoPoints){
			
			y = geoPoint.getLat() + "";
			
			x = geoPoint.getLng() + "";
			
			url.append(x).append(",").append(y).append(";");
		}
		
		return url.deleteCharAt(url.length()-1).toString();
		
	}
	
}
