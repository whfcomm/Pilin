package com.whf.pilin.entity;

public class NearFunction {
	/**
	 * 图标
	 */
	private int funcion_icon;
	/**
	 * 名称
	 */
	private String function_name;
	/**
	 * 介绍说明
	 */
	private String function_description;

	public NearFunction(){}
	
	public NearFunction(int funcion_icon, String function_name,
			String function_description) {
		this.funcion_icon = funcion_icon;
		this.function_name = function_name;
		this.function_description = function_description;
	}
	
	public int getFuncion_icon() {
		return funcion_icon;
	}

	public void setFuncion_icon(int funcion_icon) {
		this.funcion_icon = funcion_icon;
	}

	public String getFunction_name() {
		return function_name;
	}

	public void setFunction_name(String function_name) {
		this.function_name = function_name;
	}

	public String getFunction_description() {
		return function_description;
	}

	public void setFunction_description(String function_description) {
		this.function_description = function_description;
	}
	
}
