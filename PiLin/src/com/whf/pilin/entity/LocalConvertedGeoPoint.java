package com.whf.pilin.entity;

import java.io.Serializable;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class LocalConvertedGeoPoint extends GeoPoint implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double lat;
	
	private double lng;
	
	public LocalConvertedGeoPoint(double lat, double lng,boolean netWorkOrLocal) {
		
		super(3999 + (int)(lat*1E6), 10960 + (int)(lng*1E6));
		
		this.lat = lat;
		
		this.lng = lng;
	}
	
	

	public LocalConvertedGeoPoint(String lat, String lng) {
		
		super(3999 + (int)(Double.valueOf(lat)*1E6), 10960 + (int)(Double.valueOf(lng)*1E6));
		
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
	
}
