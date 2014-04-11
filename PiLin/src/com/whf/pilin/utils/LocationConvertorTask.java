package com.whf.pilin.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.entity.LocalGeoPoint;

/**
 * 将设备获得经纬度转换为百度的经纬度任务类
 * 
 * @author Administrator
 * 
 */
public class LocationConvertorTask extends AsyncTask<String, Void, String> {
	// 外部传入的位置操作者
	private Handler handler;
	private List<LocalGeoPoint> localGeoPoints;
	private int size;
	public LocationConvertorTask(Handler handler,
			List<LocalGeoPoint> localGeoPoints,int size) {
		this.handler = handler;
		this.localGeoPoints = localGeoPoints;
		this.size = size;
	}

	@Override
	protected String doInBackground(String... arg0) {

		try {
			
			int isDiv = localGeoPoints.size()/size;
			//循环的次�?
			int time = isDiv + (isDiv==0&&localGeoPoints.size()%size==0?0:1);
			
			List<String> lines = new ArrayList<String>();
			
			int sub = 1;
			//每次的开始位�?
			int k = 0;
			
			List<LocalGeoPoint> lPoints;
			//判断第一次的结束位置
			int maxIndex = (isDiv>0?size:localGeoPoints.size());
			for(int i=0;i<time;i++){
				lPoints = localGeoPoints.subList(k, maxIndex);
				lines.add(httpConvertPoints(lPoints));
				//如果是最后一次，判断结束位置
				if(i == time-2){
					int b = localGeoPoints.size() - k;
					maxIndex = (sub-1)*size + b;
				}else{
					sub++;
					maxIndex = (sub)*size;
				}
				k = k + size;
				
			}
			
			Message meg = new Message();

			meg.what = CustomConst.Handler_BaiDuConvertorTask;

			Bundle bundle = new Bundle();

			bundle.putStringArrayList("baiduJson", (ArrayList<String>)lines);

			meg.setData(bundle);

			handler.sendMessage(meg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 将设备经纬度转化为百度经纬度方法
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String httpConvertPoints(List<LocalGeoPoint> localGeoPoints) throws IOException {

		String url = "http://api.map.baidu.com/geoconv/v1/?from=1&to=5&ak="
				+ CustomConst.BROWSER_AK_KEY + "&coords=";

		url = url + LocationUtils.convertFromConvertedGeoPoint(localGeoPoints);

		String line = HttpUtils.queryGet(url);
		
		return line;
	}

}
