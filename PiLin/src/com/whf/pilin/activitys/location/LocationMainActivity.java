package com.whf.pilin.activitys.location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.overlay.LocationOverlayActivity;
import com.whf.pilin.adapter.location.ListAdapter;
import com.whf.pilin.android_service.LocationService;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.GeoPointDAO;
import com.whf.pilin.dao.PointNotSendedDAO;
import com.whf.pilin.dao.impl.GeoPointDaoImpl;
import com.whf.pilin.dao.impl.GeoPointNotSendedDaoImpl;
import com.whf.pilin.engien.OnLastGeoPointChangedListener;
import com.whf.pilin.engien.OnlocationChangedListener;
import com.whf.pilin.entity.LocalGeoPoint;
import com.whf.pilin.utils.HttpUtils;
import com.whf.pilin.utils.TypeConverter;

public class LocationMainActivity extends Activity implements OnClickListener{
	///保存定位信息集合数组
	private ArrayList<String []> positions;
	
	private ImageView ibtn_send_start;
	
	private ImageView ibtn_overlay;
	
	private ImageView ibtn_send_end;
	
	private ListView lv_position;
	
	private LocationManager locationManager;//定位管理类
	
	private TelephonyManager telephonyManager;//手机信息管理类
	
	private String IMIE;//手机串号
	
	//private static Vibrator vibrator;
	private GeoPointDAO geoPointDAO ;
	
	private PointNotSendedDAO pointNotSendedDAO;
	
	private SharedPreferences sharedPreferences;
	
	private static LocalGeoPoint lastPoint ;
	//是否有未发送的坐标点;false:有;true:没有
	private boolean mHasNotSendPoint;
	
	private long lastPointRowId;//最后一个坐标的rowId
	//地点变化时通知线路界面OverlayActivity
	private static OnLastGeoPointChangedListener geoPointChangedListener;
	
	private LocationService locationService;
	
	private PiLinApplication app;
	
	private BaseAdapter mAdapter;
	
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.location_main_layout);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		app = (PiLinApplication)getApplication();
		locationManager = app.getLocationManager();
		//获取原来的地址信息
		positions = app.getPositions();
		
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		getActionBar().setBackgroundDrawable(new ColorDrawable(0xff47b8ff));
		ibtn_send_start = (ImageView)findViewById(R.id.ibtn_location_start);
		ibtn_overlay = (ImageView)findViewById(R.id.ibtn_overlay);
		ibtn_send_end = (ImageView)findViewById(R.id.ibtn_stop_location);
		
		lv_position = (ListView)findViewById(R.id.lv_positions);
		mAdapter = new ListAdapter(positions, getApplicationContext());
		lv_position.setAdapter(mAdapter);
		
		//获取手机信息管理类
		telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMIE = telephonyManager.getDeviceId();
		//获取震动手机
		//vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		geoPointDAO = new GeoPointDaoImpl(getApplicationContext());
		pointNotSendedDAO = new GeoPointNotSendedDaoImpl(getApplicationContext());
		//进来时检查是否有未发送的数据在数据库中
		mHasNotSendPoint = pointNotSendedDAO.mHasNotSend();
		
		ibtn_overlay.setOnClickListener(this);
		ibtn_send_end.setOnClickListener(this);
		ibtn_send_start.setOnClickListener(this);
		
		Intent intent = new Intent(getApplicationContext(),LocationService.class);
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	
	
	/**
	 * 异步网络请求类
	 * @author Administrator
	 *
	 */
	class NewWorkTask extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			 if(!mHasNotSendPoint){
				 List<String> rowids = pointNotSendedDAO.findAll();
				 List<LocalGeoPoint> list =  geoPointDAO.findByRowId(rowids);
				 mHasNotSendPoint = HttpUtils.mResendPosition(Arrays.toString(convertGeoPointToStringArray(list).toArray()), "http://ip:port/GisRecoed/servlet/ReceiveCoordinateServlet");
						
			 }
			
			 if(!HttpUtils.sendPosition(positions.get(positions.size()-1), "http://ip:port/GisRecoed/servlet/ReceiveCoordinateServlet")) 
					
			 {
				 pointNotSendedDAO.save(lastPointRowId);
				 mHasNotSendPoint = false;
			 }
			
			
			return false;
		}
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(connection);
		geoPointDAO.closeDB();
	}
	//按钮点击事件
	@Override
	public void onClick(View v) {
			
		switch (v.getId()) {
		
		case R.id.ibtn_location_start:
			 
			if(app.locationChangedListener == null)Toast.makeText(this, "如果启动后，很久没有刷出第1个位置，请移动...", Toast.LENGTH_LONG).show();
			else Toast.makeText(getApplicationContext(), "定位已经开启", Toast.LENGTH_SHORT).show();
			locationService.startLocation(app,locationManager, sharedPreferences);
			
			break;
		
		case R.id.ibtn_overlay:
			
			Intent intent = new Intent(getApplicationContext(),LocationOverlayActivity.class);
			
			intent.putExtra("positions",positions);
			
			startActivity(intent);
			
			break;
			
		case R.id.ibtn_stop_location:
			
			locationService.pauseLocation(locationManager,app);
			
			app.clearPositions();
			
			break;
			
		}
		
	}

	/**
	 * 保存数据到数据库并且发送坐标到服务器端
	 * @param location
	 */
	private void saveAndSendPosition(Location location){
		//转换为发生的格式
		String[] msgstr = new String[] { "-----",
				TypeConverter.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
				location.getLongitude() + "," + location.getLatitude(), IMIE };

		// /保存当前坐标点
		lastPoint = new LocalGeoPoint(IMIE, String.valueOf(location
				.getLatitude()), String.valueOf(location.getLongitude()),
				TypeConverter.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
				"------");
		//如果没有点击进查看踪迹界面，listener为null
		System.out.println(geoPointChangedListener);
		if(geoPointChangedListener != null){
			geoPointChangedListener.onPointChanged(lastPoint);
		}
		
		positions.add(msgstr);

		lastPointRowId = geoPointDAO.savePoint(lastPoint);

		new NewWorkTask().execute();

		mAdapter.notifyDataSetChanged();
	}
	
	Handler handler = new Handler(){
		
		public void handleMessage(Message msg) {
			
			if(msg.what == CustomConst.Handler_MainThreadUpdateUI){
				
				String lo = (String)msg.getData().getCharSequence("location");
				
				String [] los = lo.split(",");
				
				Location location = new Location("");
				
				location.setLatitude(Double.valueOf(los[0]));
				location.setLongitude(Double.valueOf(los[1]));
				
				saveAndSendPosition(location);
				
			}
			
		};
	};
	
	/**
	 * 向Overlay传送最新经纬度服务
	 * @author WHF
	 *
	 */
	public static class SendDataToOveryLayService extends Service{
		
		public SendDataToOveryLayService(){
			
		}
		
		public LocalGeoPoint getLastPoint(){
			return lastPoint;
		}
		public void setOnLastGeoPointChangedListener(OnLastGeoPointChangedListener geoPointChangedListener){
			LocationMainActivity.geoPointChangedListener = geoPointChangedListener;
		}
		
		@Override
		public IBinder onBind(Intent intent) {
			return new GeoPointSendBinder();
		}
		public class GeoPointSendBinder extends Binder{
			
			public SendDataToOveryLayService getService(){
				return LocationMainActivity.SendDataToOveryLayService.this;
			}
			
		}
		
		
	}
	
	/**
	 * 将从数据库查询没有发送出去的经纬度
	 * 转换为发送的格式
	 * @param localList
	 * @return
	 */
	private List<String []> convertGeoPointToStringArray(List<LocalGeoPoint> localList){
		
		List<String[]> arrays = new ArrayList<String[]>();
		
		for(LocalGeoPoint point:localList){
			
			String [] array = {point.getPosition(),
							   point.getInsertTime(),
							   point.getLat()+","+point.getLng(),
							   point.getImie()};
			arrays.add(array);
			
		}
		return arrays;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		
		case android.R.id.home:
			
			finish();
			
			break;
		}
		
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	/**
	 * 定位Service链接
	 */
	ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			locationService = ((LocationService.LocalBinder)service).getService();
			
			locationService.setLocationChangedListener(new OnlocationChangedListener() {
				
				@Override
				public void monLocationChanged(Location location) {
					
					saveAndSendPosition(location);
					
				}
			});
		}
	};
	
}
