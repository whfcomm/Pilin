package com.whf.pilin.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class LocalGeoPoint implements Parcelable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ConvertedGeoPoint geoPoint;
	
	private LocalConvertedGeoPoint lGeoPoint;

	private int id;
	
	private String imie;
	
	private String lat;
	
	private String lng;
	
	private String insertTime;
	
	private String position;

	public LocalGeoPoint(int id, String imie, String lat, String lng,
			String insertTime,String position) {
		this.id = id;
		this.imie = imie;
		this.lat = lat;
		this.lng = lng;
		this.insertTime = insertTime;
		this.position = position;
		this.geoPoint = new ConvertedGeoPoint(lat, lng);
		this.lGeoPoint = new LocalConvertedGeoPoint(lat, lng);
	}

	public LocalGeoPoint(){}
	
	public LocalGeoPoint(Parcel in){
		
		geoPoint = (ConvertedGeoPoint)in.readSerializable();
		imie = in.readString();
		lat = in.readString();
		lng = in.readString();
		position= in.readString();
		insertTime = in.readString();
	}
	
	public LocalGeoPoint(String imie, String lat, String lng, String insertTime,String position) {
		this.imie = imie;
		this.lat = lat;
		this.lng = lng;
		this.insertTime = insertTime;
		this.position = position;
		this.geoPoint = new ConvertedGeoPoint(lat, lng);
		this.lGeoPoint = new LocalConvertedGeoPoint(lat, lng);
	}

	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public ConvertedGeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(ConvertedGeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImie() {
		return imie;
	}

	public void setImie(String imie) {
		this.imie = imie;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeSerializable(geoPoint);
		dest.writeString(imie);
		dest.writeString(lat);
		dest.writeString(lng);
		dest.writeString(position);
		dest.writeString(insertTime);
		
	}
	public static final Creator<LocalGeoPoint> CREATOR = new Creator<LocalGeoPoint>() {  
        public LocalGeoPoint createFromParcel(Parcel source) {  
            return new LocalGeoPoint(source);  
        }  
  
        public LocalGeoPoint[] newArray(int size) {  
            return new LocalGeoPoint[size];  
        }  
    };

	public LocalConvertedGeoPoint getlGeoPoint() {
		return lGeoPoint;
	}

	public void setlGeoPoint(LocalConvertedGeoPoint lGeoPoint) {
		this.lGeoPoint = lGeoPoint;
	} 
	
	
	
	
	
}
