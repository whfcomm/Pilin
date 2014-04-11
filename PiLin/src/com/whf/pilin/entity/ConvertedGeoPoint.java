package com.whf.pilin.entity;

import java.io.Serializable;

import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 百度坐标偏移纠正
 * @author Administrator
 *
 */
public class ConvertedGeoPoint extends GeoPoint implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double lat;
	
	private double lng;
	
	public ConvertedGeoPoint(double lat, double lng) {
		
		super((int)(lat*1E6), (int)(lng*1E6));
		
		this.lat = lat;
		
		this.lng = lng;
	}
	
	

	public ConvertedGeoPoint(String lat, String lng) {
		
		super((int)(Double.valueOf(lat)*1E6), (int)(Double.valueOf(lng)*1E6));
		
		this.lat = Double.valueOf(lat);
		
		this.lng = Double.valueOf(lng);
	}
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
