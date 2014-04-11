package com.whf.pilin.entity;

public class ChatMessagePeople extends Entity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String uid;
	
	private int type;//0-文字；1-图片、地图；2-语音
	
	private String imgcacheDir;
	
	private int state;//0-已发送；1-已读；2-失败
	
	private String textmsg;//文字信息
	
	private String msgtime;//发送时间
	
	private ChatMessagePeople(){
		
	}
	
	public ChatMessagePeople(int id, String uid, int type, String imgcacheDir,
			int state, String textmsg, String msgtime) {
		super();
		this.id = id;
		this.uid = uid;
		this.type = type;
		this.imgcacheDir = imgcacheDir;
		this.state = state;
		this.textmsg = textmsg;
		this.msgtime = msgtime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImgcacheDir() {
		return imgcacheDir;
	}

	public void setImgcacheDir(String imgcacheDir) {
		this.imgcacheDir = imgcacheDir;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTextmsg() {
		return textmsg;
	}

	public void setTextmsg(String textmsg) {
		this.textmsg = textmsg;
	}

	public String getMsgtime() {
		return msgtime;
	}

	public void setMsgtime(String msgtime) {
		this.msgtime = msgtime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
