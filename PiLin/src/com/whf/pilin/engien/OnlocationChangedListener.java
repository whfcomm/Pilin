package com.whf.pilin.engien;

import android.location.Location;
/**
 * 当定位点发生改变的触发
 * @author WHF
 *
 */
public interface OnlocationChangedListener {
	
	public void monLocationChanged(Location location);
	
}
