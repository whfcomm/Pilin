package com.whf.pilin.activitys.overlay;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.BaseActivity;
import com.whf.pilin.activitys.location.LocationMainActivity;
import com.whf.pilin.activitys.location.LocationMainActivity.SendDataToOveryLayService;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.GeoPointDAO;
import com.whf.pilin.dao.impl.GeoPointDaoImpl;
import com.whf.pilin.engien.OnLastGeoPointChangedListener;
import com.whf.pilin.entity.ConvertedGeoPoint;
import com.whf.pilin.entity.LocalConvertedGeoPoint;
import com.whf.pilin.entity.LocalGeoPoint;
import com.whf.pilin.popwindow.OverLayPopWindow;
import com.whf.pilin.utils.ConvertToBaiduPosition;
import com.whf.pilin.utils.HttpUtils;
import com.whf.pilin.utils.LocationConvertorTask;

public class LocationOverlayActivity extends BaseActivity implements
		OnTouchListener, OnClickListener {

	private MapView mapView;
	
	private MapController mapController;
	
	private ArrayList<String[]> list;

	private GeoPointDAO geoPointDAO;

	private EditText et_start;

	private EditText et_end;

	private Button btn_check;

	private String selectTime;

	// 用于标记是开始时间还是结束时�?false:�?��时间;true:结束时间
	private boolean timeMarker;

	private OverLayPopWindow popupWindow;

	private BMapManager mBMapManager;

	private List<LocalGeoPoint> localGeoPoints;

	private List<GeoPoint[]> gPoints;

	private String[] center;

	private ConnectivityManager connectivityManager;
	// 发�?到百度进行转换的�?��数量<100
	private int sendToConvertSize = 80;

	private SendDataToOveryLayService overyLayService;
	//判断是主界面点击进来的还是这个界面接收主界面用Service传进来的
	private boolean MainTorrgleOrRefresh = true; //true:主界面刚刚传过来的，false:已转为服务更�?
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(PiLinApplication.strKey, null);
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		setContentView(R.layout.overlay_map);
		
		geoPointDAO = new GeoPointDaoImpl(getApplicationContext());
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		list = (ArrayList<String[]>) bundle.getSerializable("positions");
		initViews();
		initEvents();
		//这里可以判断出主界面有没有传数据过来
		//null 没有传过�?则查询今天的数据
		if (list == null || list.size() == 0) {

			localGeoPoints = geoPointDAO.findToday();
		//有传进来则转换传过来的数据，并开启实时数据更新服�?
		} else {
			//绑定服务
			mApplication.bindService(new Intent(this,LocationMainActivity.SendDataToOveryLayService.class)
			 , connection, Context.BIND_AUTO_CREATE);
			
			center = list.get(0)[2].split(",");
			
			localGeoPoints = convertStringArrayToLocalPoint(list);

		}
		justDataIsEmpty();
	}
	

	@Override
	protected void initViews() {
		mapView = (MapView) findViewById(R.id.bmapView);
		et_start = (EditText) findViewById(R.id.et_start);
		et_end = (EditText)findViewById(R.id.et_end);
		btn_check = (Button) findViewById(R.id.btn_check);
		// mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected void initEvents() {
		et_start.setOnTouchListener(this);
		et_end.setOnTouchListener(this);
		btn_check.setOnClickListener(this);
	}
	
	/**
	 * 画线路
	 * @param MainTorrgleOrRefresh 判断是主界面点击进来的还是这个界面接收主界面用Service传进来的
	 */
	public void drawRoad() {

		center = new String[] {
				((ConvertedGeoPoint) (gPoints.get(0)[0])).getLng() + "",
				((ConvertedGeoPoint) (gPoints.get(0)[0])).getLat() + "" };
		if(mapController==null){
			mapController = mapView.getController();
		}
		mapController.enableClick(true);
		//这里分布判断是不是主界面第一次点进来，是缩放大小是手动缩放大小后的，且中心是你缩放或者拖放后的地图内的中心，这样用户就不会有刷新时界面跳动的感觉
		//给用户更好的体验效果
		mapController.setZoom(MainTorrgleOrRefresh?16:mapView.getZoomLevel());
		mapController.setCenter(MainTorrgleOrRefresh?new ConvertedGeoPoint(center[1], center[0]):mapView.getMapCenter());
		onverlayDraw(gPoints);

	}

	/**
	 * 将集合的字符串数组转换为地点的坐标
	 * 
	 * @param strSrc
	 * @return
	 */
	public List<LocalGeoPoint> convertStringArrayToLocalPoint(
			ArrayList<String[]> strSrc) {

		List<LocalGeoPoint> localList = new ArrayList<LocalGeoPoint>();

		for (String[] str : strSrc) {

			String[] arr = str[2].split(",");

			localList.add(new LocalGeoPoint(null, arr[1], arr[0], null, null));

		}

		return localList;
	}

	/**
	 * 画路径公共方法
	 * 
	 * @param gpionts
	 */
	private void onverlayDraw(List<GeoPoint[]> gpionts) {
		// 用站点数据构建一个MKRoute
		MKRoute route = new MKRoute();
		route.customizeRoute(gpionts.get(0)[0],
				gpionts.get(gpionts.size() - 1)[0],
				gpionts.toArray(new ConvertedGeoPoint[gpionts.size()][]));
		RouteOverlay routeOverlay = new RouteOverlay(
				LocationOverlayActivity.this, mapView);
		routeOverlay.setData(route);
		//如果是刚刚点击进来的，则这里删除掉原来的路径
		if(MainTorrgleOrRefresh){
			// 删除原来的路径，至于为什么是1，我也不知道
				mapView.getOverlays().remove(1);
		}
		
		// 向地图添加构造好的RouteOverlay
		mapView.getOverlays().add(routeOverlay);
		// 执行刷新使生效
		mapView.refresh();
	}
	/**
	 * 
	 * @param gps
	 * @return
	 */
	private List<GeoPoint[]> convertToArrayGeoPoints(List<ConvertedGeoPoint> gps) {

		if (gps == null) {

			return null;

		}

		List<GeoPoint[]> gPoints = new ArrayList<GeoPoint[]>();

		for (ConvertedGeoPoint geoPoint : gps) {
			gPoints.add(new ConvertedGeoPoint[] { geoPoint });
		}

		return gPoints;

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			//转换百度经纬度返回数据
			if (msg.what == CustomConst.Handler_BaiDuConvertorTask) {
				Bundle bundle = msg.getData();

				List<String> jsonStr = bundle.getStringArrayList("baiduJson");

				try {

					List<ConvertedGeoPoint> gps = ConvertToBaiduPosition
							.convert(jsonStr, getApplicationContext());
					
					if(gPoints == null){
						gPoints = new ArrayList<GeoPoint[]>();
					}
					
					List<GeoPoint[]> list = convertToArrayGeoPoints(gps);
					/**
					 * 判断网络传回的数据是否存在异常
					 */
					//不存在就将路径显示在地图上
					if(list != null){
						gPoints.addAll(list);
						new convertTASK().execute();
					}else {
						Toast.makeText(getApplicationContext(), "网络环境存在异常，请�?��", Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			//日期选择返回数据	
			} else if (msg.what == CustomConst.Handler_PopDateSelectWindow) {

				Bundle bundle = msg.getData();

				timeMarker = bundle.getBoolean("timeMarker");

				selectTime = bundle.getString("selectTime");

				if (!timeMarker) {
					et_start.setText(selectTime);
				} else {
					et_end.setText(selectTime);
				}
			}

		}

	};

	class convertTASK extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			drawRoad();
			if(MainTorrgleOrRefresh)MainTorrgleOrRefresh=false;
			return null;
		}

	}
	
	/**
	 * 将转换为本地修正LocalConvertedGeoPoint经纬度数据
	 * 
	 * @param lGeoPoints
	 * @return
	 */
	private List<GeoPoint[]> converListGeoPoints(List<LocalGeoPoint> lGeoPoints) {

		List<GeoPoint[]> gPoints = new ArrayList<GeoPoint[]>();

		for (LocalGeoPoint geoPoint : lGeoPoints) {

			gPoints.add(new LocalConvertedGeoPoint[] { geoPoint.getlGeoPoint() });

		}
		return gPoints;
	}


	/**
	 * 填出日期选择窗口
	 * 
	 * @param v
	 */
	private void popSelectWindow(View v) {
		if(popupWindow == null){
			popupWindow = new OverLayPopWindow(getApplicationContext(), handler);
		}
		popupWindow.timeMarker = timeMarker;
		popupWindow.showAsDropDown(v);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if (v.getId() == R.id.et_start
				&& event.getAction() == MotionEvent.ACTION_UP) {

			timeMarker = false;

			popSelectWindow(v);

		} else if (v.getId() == R.id.et_end
				&& event.getAction() == MotionEvent.ACTION_UP) {

			timeMarker = true;

			popSelectWindow(v);

		}

		return true;
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.btn_check) {

			if ("".equals(et_start.getText().toString())
					|| "".equals(et_end.getText().toString())) {
				Toast.makeText(this, "请先选择日期", Toast.LENGTH_SHORT).show();
				return;
			}

			localGeoPoints = geoPointDAO.findByTime(et_start.getText()
					.toString(), et_end.getText().toString());
			
			gPoints = new ArrayList<GeoPoint[]>();
			
			MainTorrgleOrRefresh = true;
			
			justDataIsEmpty();

		}

	}

	/**
	 * 判断查询是否为空
	 * 
	 * @return
	 */
	private boolean justDataIsEmpty() {

		if (localGeoPoints == null || localGeoPoints.size() == 0) {

			Toast.makeText(getApplicationContext(), "查询范围内没有数据",
					Toast.LENGTH_LONG).show();
			return false;
		}

		// 如果网络可用，就用网络转换经纬度
		if (HttpUtils.isNetWorkAccess(connectivityManager, this)) {

			new LocationConvertorTask(handler, localGeoPoints,
					sendToConvertSize).execute();

			// 否则本地
		} else {
			
			gPoints = converListGeoPoints(localGeoPoints);

		}
		return true;
	}
	/**
	 * 与主界面Service的链接
	 */
	ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			overyLayService = ((LocationMainActivity.SendDataToOveryLayService.GeoPointSendBinder)service).getService();

			overyLayService.setOnLastGeoPointChangedListener(new OnLastGeoPointChangedListener() {
				@Override
				public void onPointChanged(LocalGeoPoint point) {
					
					List<LocalGeoPoint> lastPoints = new ArrayList<LocalGeoPoint>();
					
					lastPoints.add(point);
					// 如果网络可用，就用网络转换经纬度
					if (HttpUtils.isNetWorkAccess(connectivityManager, getApplicationContext())) {

						new LocationConvertorTask(handler, lastPoints,
								sendToConvertSize).execute();

						// 否则本地
					} else {
						
						localGeoPoints.addAll(lastPoints);
						gPoints = converListGeoPoints(localGeoPoints);

					}
				}
			});
			
		}
	};
	
	@Override
	protected void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mapView.destroy();
		geoPointDAO.closeDB();
		if(overyLayService != null)mApplication.unbindService(connection);
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mapView.onRestoreInstanceState(savedInstanceState);
	}

}
