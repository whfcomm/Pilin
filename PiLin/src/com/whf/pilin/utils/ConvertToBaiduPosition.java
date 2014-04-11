package com.whf.pilin.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.whf.pilin.entity.ConvertedGeoPoint;

public class ConvertToBaiduPosition {
	/**
	 * 将设备获取的经纬度转换为百度的经纬度，用于在地图上展示
	 * @param jsonStr 传入的百度转后的JSON字符串经纬度
	 * @param context 上下问内容
	 * @return
	 * @throws JSONException
	 */
	public static List<ConvertedGeoPoint> convert(List<String> jsonStr ,Context context) throws JSONException{
		
		List<ConvertedGeoPoint> geoPoints = new ArrayList<ConvertedGeoPoint>();
		
		for(String json : jsonStr){
			
			if(json==null)return null;
			
			JSONObject jsonObject = new JSONObject(json);
			
			if(!"0".equals(jsonObject.getString("status"))){
				
				Toast.makeText(context, "位置信息获取错误", Toast.LENGTH_LONG).show();
				
				return null;
			}
			
			JSONArray array   = jsonObject.getJSONArray("result");
			
			JSONObject jsonObj = null;
			
			ConvertedGeoPoint geoPoint;
			
			for(int i=0;i<array.length();i++){
				jsonObj = array.getJSONObject(i);
				geoPoint = new ConvertedGeoPoint((Double)jsonObj.get("y"), (Double)jsonObj.get("x"));
				geoPoints.add(geoPoint);
			}
		}
		return geoPoints;
	}
	
}
