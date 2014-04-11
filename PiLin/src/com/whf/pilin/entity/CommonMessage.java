package com.whf.pilin.entity;

import java.io.Serializable;

import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;

public class CommonMessage extends Entity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String uid;
	
	private String name;
	
	private String avatar;
	
	private long time;
	
	private String distance;
	
	private String content;
	
	private MSG_CONTENT_TYPE contentType;
	
	private MSG_DERATION msgComeFromType;
	
	private MSG_STATE state;
	
	public CommonMessage(int id,String uid, String avatar, long time, String distance,
			String content, MSG_STATE state,MSG_CONTENT_TYPE contentType,
			MSG_DERATION msgComeFromType,String name) {
		super();
		this.id = id;
		this.uid = uid;
		this.avatar = avatar;
		this.time = time;
		this.distance = distance;
		this.content = content;
		this.state = state; 
		this.contentType = contentType;
		this.msgComeFromType = msgComeFromType;
		this.name = name;
	}
	public CommonMessage(String uid, String avatar, long time, String distance,
			String content, MSG_STATE state,MSG_CONTENT_TYPE contentType,
			MSG_DERATION msgComeFromType,String name) {
		super();
		this.uid = uid;
		this.avatar = avatar;
		this.time = time;
		this.distance = distance;
		this.content = content;
		this.state = state;
		this.contentType = contentType;
		this.msgComeFromType = msgComeFromType;
		this.name = name;
	}
	public CommonMessage(String avatar, long time, String distance,
			String content, MSG_CONTENT_TYPE contentType,
			MSG_DERATION msgComeFromType) {
		super();
		this.avatar = avatar;
		this.time = time;
		this.distance = distance;
		this.content = content;
		this.contentType = contentType;
		this.msgComeFromType = msgComeFromType;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public MSG_CONTENT_TYPE getContentType() {
		return contentType;
	}
	public void setContentType(MSG_CONTENT_TYPE contentType) {
		this.contentType = contentType;
	}
	public MSG_DERATION getMsgComeFromType() {
		return msgComeFromType;
	}
	public void setMsgComeFromType(MSG_DERATION msgComeFromType) {
		this.msgComeFromType = msgComeFromType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public MSG_STATE getState() {
		return state;
	}

	public void setState(MSG_STATE state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
