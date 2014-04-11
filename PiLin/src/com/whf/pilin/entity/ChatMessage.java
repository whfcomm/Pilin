package com.whf.pilin.entity;

import java.io.Serializable;

import com.whf.pilin.engien.MsgEume.MSG_STATE;

public class ChatMessage extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 昵称
	 */
	private String niName;
	/**
	 * 最后的通讯时间
	 */
	private String chatLstTime;
	/**
	 * 最后的信息
	 */
	private String chatLastMsg;
	/**
	 * 消息状态
	 */
	private MSG_STATE msgState;
	
	public ChatMessage(){}
	
	public ChatMessage(int user_icon,String niName, String chatLstTime, String chatLastMsg, MSG_STATE msgState){
		this.setNiName(niName);
		this.chatLastMsg =chatLastMsg;
		this.chatLstTime = chatLstTime;
		this.msgState = msgState;
	}

	public ChatMessage(int user_icon,String niName, String mood, int sex, int age,
			int state, String chatLstTime, String chatLastMsg,
			MSG_STATE msgState) {
		this.chatLstTime = chatLstTime;
		this.chatLastMsg = chatLastMsg;
		this.msgState = msgState;
	}

	public String getChatLstTime() {
		return chatLstTime;
	}
	public void setChatLstTime(String chatLstTime) {
		this.chatLstTime = chatLstTime;
	}
	public String getChatLastMsg() {
		return chatLastMsg;
	}
	public void setChatLastMsg(String chatLastMsg) {
		this.chatLastMsg = chatLastMsg;
	}

	public MSG_STATE getMsgState() {
		return msgState;
	}

	public void setMsgState(MSG_STATE msgState) {
		this.msgState = msgState;
	}

	public String getNiName() {
		return niName;
	}

	public void setNiName(String niName) {
		this.niName = niName;
	}
	
	
	
}
