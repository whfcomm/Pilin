package com.whf.pilin.engien;

public class MsgEume {
	/**
	 * 发送信息状态;
	 * ARRIVED - 0;
	 * READED  - 1;
	 * FAILED  - 2;
	 * RECEIVEING - 3;
	 * SENDDING - 4;
	 * @author WHF
	 */
	public enum MSG_STATE{
		//到达
		ARRIVED,
		//已读
		READED,
		//发送失败
		FAILED,
		//正在发送
		SENDDING,
		//正在接收
		RECEIVEING
	
	}
	/**
	 * 信息流向
	 * RECEIVE - 0;
	 * SEND - 1;
	 * @author WHF
	 *
	 */
	public enum MSG_DERATION{
		//接收
		RECEIVE,
		//发送
		SEND
		
	}
	/**
	 * 内容类型
	 * TEXT - 0;
	 * IMAGE - 1;
	 * VOICE - 2;
	 * MAP - 3;
	 * @author WHF
	 *
	 */
	public enum MSG_CONTENT_TYPE{
		//文本
		TEXT,
		//图片
		IMAGE,
		//语音
		VOICE,
		//地图
		MAP
		
	}
	
	/**
	 * 用户信息界面类型
	 * @author WHF
	 *
	 */
	public enum USERINFO_TYPE{
		//正在聊天
		CHATTING,
		//用户列表
		FRIEND_NEAR
		
	}
	/**
	 * 提示广播类型
	 * @author WHF
	 *
	 */
	public enum BORCAST_RECEIVER{
		//群
		GROUP,
		//单人
		SINGLE_PEOPLE
		
	}
	
}
