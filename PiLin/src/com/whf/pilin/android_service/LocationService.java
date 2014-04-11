package com.whf.pilin.android_service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.engien.OnlocationChangedListener;
import com.whf.pilin.utils.LocationUtils;

public class LocationService extends Service{
	
	private OnlocationChangedListener mlocationChangedListener;
	
	private LocationChangedListener listener = new LocationChangedListener();
	
	public LocationService(){}
	
	public void setLocationChangedListener(OnlocationChangedListener mlocationChangedListener){
		this.mlocationChangedListener = mlocationChangedListener;
	}
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return new LocalBinder();
		
	}
	
	public void startLocation(PiLinApplication app,LocationManager manager,SharedPreferences sharedPreferences){
		if(app.locationChangedListener !=null) manager.removeUpdates(app.locationChangedListener);
		LocationUtils.startLocation(manager, listener, sharedPreferences);
		app.setLocationChangedListener(listener);
		
	}
	public void pauseLocation(LocationManager manager,PiLinApplication app){
		LocationUtils.stopLocation(manager, app.locationChangedListener);
		PiLinApplication.getInstance().locationChangedListener = null;
	}
	
	public class LocalBinder extends Binder{
		
		public LocationService getService(){
			
			return LocationService.this;
			
		} 
		
	}

	public class LocationChangedListener implements LocationListener{
	
		@Override
		public void onLocationChanged(Location location) {
			
			mlocationChangedListener.monLocationChanged(location);
			
		}
	
		@Override
		public void onProviderDisabled(String provider) {
			
		}
	
		@Override
		public void onProviderEnabled(String provider) {
			
		}
	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	}
	
}
