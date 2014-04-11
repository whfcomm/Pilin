package com.whf.pilin.constVar;

public class CustomConst {
	
	public static final String BROWSER_AK_KEY = "9GuB2R52qo2d96WmZzDz3G0j";
	
	public static final int Handler_BaiDuConvertorTask = 2;
	
	public static final int Handler_MainThreadUpdateUI = 1;
	
	public static final int Handler_PopDateSelectWindow = 3;
	
	public static final boolean GeoPointType_NETWORK = true;
	
	public static final boolean GeoPointType_LOCAL = false;
	///设置里的分类
	public static final int SETTIING_ITEM_TYPE_RADIO = 0;
	
	public static final int SETTIING_ITEM_TYPE_SWITCH = 1;
	
	public static final int SETTING_ITEM_LOCATION_UPDATE = 2;
	
	///定位方式
	public static final int LOCATION_TYPE_AUTO = 0;
	
	public static final int LOCATION_TYPE_GPS = 1;
	
	public static final int LOCATION_TYPE_NETWORK = 2;
	
	public static final int LOCATION_TYPE_PASSIVE = 3;
	
	//XMM连接Handler返回类型
	public static final int XMPP_HANDLER_ERROR = 500;
	
	public static final int XMPP_HANDLER_SUCCESS = 200;
	
	public static final int XMPP_ERROR_CONNETERROR = 0;
	
	public static final int XMPP_ERROR_LOGINFAIL = 1;
	
	//数据连接类型
	public static String DAO_CHATTING = "ChattingPeopleDao";
	
	public static String DAO_MESSAGE = "MessageDao";
	
	public static String DAO_NEARBY = "NearByPeopleDao";
	
	//缓存图片
	public static final String IMAGE_CACHE_PATH = "imageCache";
	
	//用户状态
	public static final int USER_STATE_ONLINE = 0;
	
	public static final int USER_STATE_Q_ME = 1;
	
	public static final int USER_STATE_BUSY = 2;
	
	public static final int USER_STATE_AWAY = 3;
	
	public static final int USER_STATE_SETSELFOFFLINE = 4;
	
	public static final int USER_STATE_OFFLINE = 5;
	
	//查询聊天记录时每页最大行数
	public static final int MESSAGE_PAGESIZE = 5;
	
	//图片或者拍照请求参数
	/**
	 * 使用选择相册图片
	 */
	public static final int MEDIA_CODE_PICTURE = 1000;
	/**
	 * 拍照方式
	 */
	public static final int MEDIA_CODE_CAMERA = 1001;
	
	//发送图片返回进度Handler值
	public static final int HANDLER_MGS_ADD = 0;
	public static final int HANDLER_MSG_SENDFILE_PROCESS = 1;
	public static final int HANDLER_MSG_SENDFILE_ERROR = 2;
	public static final int HANDLER_MSG_SENDFILE_CANNELED = 3;
	public static final int HANDLER_MSG_FILE_SUCCESS = 4;
	public static final int HANDLER_MSG_RECEIVEFILE_ERROR = 5;
	//信息Fragment上聊天用户列表
	/**
	 * 添加
	 */
	public static final int HANDLER_CHATPEOPLE_LIST_ADD = 0;
	/**
	 * 更新
	 */
	public static final int HANDLER_CHATPEOPLE_LIST_UPDATE = 1;
	
	/**
	 * 发送文件类型：图片
	 */
	public static final String FILETYPE_IMAGE = "IMAGE";
	/**
	 * 发送文件类型：语音
	 */
	public static final String FILETYPE_VOICE = "VOICE";
	
}
