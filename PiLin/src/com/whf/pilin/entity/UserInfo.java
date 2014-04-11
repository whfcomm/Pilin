package com.whf.pilin.entity;

import java.io.Serializable;

import com.whf.pilin.utils.PinYinUtils;

/**
 * 好友列表框显示的好友信息
 * @author Administrator
 *
 */
public class UserInfo extends Entity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 昵称
	 */
	private String niName;
	/**
	 * 心情
	 */
	private String mood;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 年龄
	 */
	private int age;
	/**
	 * 当前登录状态
	 */
	private int state;
	/**
	 * 首字符拼音
	 */
	private String py;
	/**
	 * 头像
	 */
	private int user_info;
	
	public UserInfo(String niName, String mood, int sex, int age, int state) {
		super();
		this.niName = niName;
		this.mood = mood;
		this.sex = sex;
		this.age = age;
		this.state = state;
		this.setPy(PinYinUtils.getAlpha(niName));
	}
	
	public UserInfo(int user_info,String niName, String mood, int sex, int age, int state) {
		super();
		this.niName = niName;
		this.mood = mood;
		this.sex = sex;
		this.age = age;
		this.state = state;
		this.user_info = user_info;
		this.setPy(PinYinUtils.getAlpha(niName));
	}
	
	
	public UserInfo(){}
	
	public String getNiName() {
		return niName;
	}
	public void setNiName(String niName) {
		this.niName = niName;
	}
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getUser_info() {
		return user_info;
	}

	public void setUser_info(int user_info) {
		this.user_info = user_info;
	}
}
