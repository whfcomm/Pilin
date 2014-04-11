package com.whf.pilin;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jivesoftware.smack.XMPPConnection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.whf.pilin.activitys.main.MainFragmentActivity;
import com.whf.pilin.adapter.chat.FaceGridViewAdapter;
import com.whf.pilin.android_service.LocationService.LocationChangedListener;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.BaseDAO;
import com.whf.pilin.dao.impl.ChattingPeopleDaoImpl;
import com.whf.pilin.dao.impl.MessageDaoImpl;
import com.whf.pilin.dao.impl.NearByPeopleDaoImpl;
import com.whf.pilin.db.DBHelper;
import com.whf.pilin.utils.ToastUtils;


public class PiLinApplication extends Application {
	private Bitmap mDefaultAvatar;
	private static final String AVATAR_DIR = "avatar/";
    private static PiLinApplication mInstance = null;
    public boolean m_bKeyRight = true;
    /**
     * 定位管理器
     */
    private static LocationManager locationManager;
    /**
     * 当位置变化时的监听器
     */
    public LocationChangedListener locationChangedListener;
    /**
     * 保存打开过的Activity
     */
    private List<Activity> activities = new ArrayList<Activity>();
    
    public Map<String, SoftReference<Bitmap>> mPhotoOriginalCache = new HashMap<String, SoftReference<Bitmap>>();
    
    public Map<String,SoftReference<Bitmap>> mSendbarCache = new HashMap<String,SoftReference<Bitmap>>();
    
    ///保存定位信息集合数组
  	private static ArrayList<String []> positions = new ArrayList<String []>();
    /**
     * 表情名
     */
  	public static List<String> mEmotions_Zme = new ArrayList<String>();
  	/**
  	 * 备用表情名
  	 */
  	public static List<String> mEmotions = new ArrayList<String>();
  	/**
  	 * 表情名与其对应的图片ID
  	 */
  	public static Map<String,Integer> mEmotions_Id = new HashMap<String,Integer>();
  	/**
  	 * 连接管理者
  	 */
  	public static XMPPConnection xmppConnection;
  	
    public static final String strKey = "nu2BKKqcigzc0OvjE38EloTe";
    /**
     * 用户名
     */
    public static Map<String,String> friendsNames = new HashMap<String,String>();
    
    /**
     * 所有会话，以对话用户JID命名
     */
    public static Map<String,Object> mJIDChats = new TreeMap<String,Object>();
    /**
     * 数据库连接
     */
    public Map<String,BaseDAO> dabatases; 
    
    public DBHelper dbHelper;
    
    /**
     * 用户聊天界面当前滚动到的信息在数据库中的页数,String-uid;Integer-页码
     */
    public static Map<String,Integer> mUsrChatMsgPage = new HashMap<String,Integer>();;
    
	@Override
    public void onCreate() {
	    super.onCreate();
	    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mInstance = this;
		initDataBase();
		initEmotions();
		dbHelper= new DBHelper(mInstance);
		//初始化服务器连接
		MXmppConnManager.getInstance().new InitXmppConnectionTask(handler).execute();
	}
	
	private void initDataBase(){
		dabatases = new HashMap<String,BaseDAO>();
		dabatases.put(CustomConst.DAO_MESSAGE, new MessageDaoImpl(this));
		dabatases.put(CustomConst.DAO_NEARBY, new NearByPeopleDaoImpl(this));
		dabatases.put(CustomConst.DAO_CHATTING, new ChattingPeopleDaoImpl(this));
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == CustomConst.XMPP_HANDLER_ERROR){
				
				if(msg.arg1 == CustomConst.XMPP_ERROR_CONNETERROR){
					ToastUtils.createCenterNormalToast(getInstance(), "网络存在异常", Toast.LENGTH_SHORT);
				}else{
					ToastUtils.createCenterNormalToast(getInstance(), "账号/密码错误", Toast.LENGTH_SHORT);
				}
			}
			
		};
		
	};
	
	/**
	 * 初始化表情
	 */
	private void initEmotions(){
		
		for(int i = 1;i < 66;i++){
			
			String emotionName = "[zme" + i + "]";
			
			//int emotionId = getResources().getIdentifier("zme" + i, "drawable", getPackageName());
			
			mEmotions.add(emotionName);
			
			mEmotions_Zme.add(emotionName);
			
			mEmotions_Id.put(emotionName, FaceGridViewAdapter.faces[i-1]);
			
		}
		
	} 
	
	/**
	 * 用于监听定位的全局变量
	 */
	public void setLocationChangedListener(LocationChangedListener locationChangedListener){
		
		this.locationChangedListener = locationChangedListener;
		
	}
	/**
	 * 获取当前保存的地址信息
	 * @return
	 */
	public ArrayList<String []> getPositions(){
		return positions;
	}
	/**
	 * 清空地址信息
	 */
	public void clearPositions(){
		positions.clear();
	}
	
	public static PiLinApplication getInstance() {
		return mInstance;
	}
	
	public LocationManager getLocationManager(){
		
		return locationManager;
		
	}
	/**
	 * 将activity加到全局的Activity中
	 * @param activity
	 */
	public void addActivity(Activity activity){
		
		getInstance().activities.add(activity);
		
	}
	/**
	 * 判断一个主页面是否已经被加载
	 * @param activity 判断的activity
	 * @return
	 */
	public boolean isExistMainActivity(){
		
		for(Activity ac:getInstance().activities){
			
			if(ac instanceof MainFragmentActivity){
				
				return true;
				
			}
		}
		return false;
	}
	
	/**
	 * 将activity从全局的Activity中移除
	 * @param activity
	 */
	public void removeActivity(Activity activity){
		
		getInstance().activities.remove(activity);
		
	}
	
	public void exit(){
		
		for(Activity activity : getInstance().activities){
			
			activity.finish();
			
		}
		getInstance().activities.clear();
		
	}
	
	private static Map<String,List<Handler>> handlers = new HashMap<String,List<Handler>>();
	
	public static List<Handler> getHandlers(String uid){
		Log.i("MReceiveChatListener", "====>GETUID====Handler");
		return handlers.get(uid);
		
	}
	public static void removeHandler(String key,Handler handler){
		handlers.get(key).remove(handler);
		Log.i("MReceiveChatListener", "====>Remove====Handler");
		
	}
	
	public static void putHandler(String key,Handler handler){
		Log.i("MReceiveChatListener", "====>Add====Handler");
		if(handlers.get(key) == null){
			handlers.put(key, new ArrayList<Handler>());
		}
		handlers.get(key).add(handler);
		
	}
	
}