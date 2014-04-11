package com.whf.pilin.entity;

import java.io.Serializable;
/**
 * 在信息列表项的Bean
 * @author WHF
 *
 */
public class ChattingPeople extends Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String uid;
	
	private CommonMessage lastMsg;
	
	private long msgNotReadCount;
	
	public ChattingPeople(){
		
	}

	public ChattingPeople(String people, CommonMessage lastMsg,
			long msgNotReadCount) {
		super();
		this.uid = people;
		this.lastMsg = lastMsg;
		this.msgNotReadCount = msgNotReadCount;
	}

	public String getPeople() {
		return uid;
	}

	public void setPeople(String people) {
		this.uid = people;
	}

	public CommonMessage getLastMsg() {
		return lastMsg;
	}

	public void setLastMsg(CommonMessage lastMsg) {
		this.lastMsg = lastMsg;
	}
	
	public long getMsgNotReadCount() {
		return msgNotReadCount;
	}

	public void setMsgNotReadCount(long msgNotReadCount) {
		this.msgNotReadCount = msgNotReadCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
