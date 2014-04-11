package com.whf.pilin.entity;

import java.io.Serializable;
/**
 * 网络获取的版本信息类
 * @author WHF
 *
 */
public class VersionInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String version;
	
	private String updateDescription;
	
	private String updateUrl;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	
}
