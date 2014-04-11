package com.whf.pilin.dao;

import java.util.List;

public interface PointNotSendedDAO extends BaseDAO{
	/**
	 * 保存未发的经纬度
	 * @param rowid
	 */
	public void save(long rowid);
	/**
	 * 根据rowid查找
	 * @param rowid
	 */
	public void findByGeoPointRowId(long rowid);
	/**
	 * 查找所有未发的经纬度
	 * @return
	 */
	public List<String> findAll();
	
	/**
	 * 查询是否有未发送的数据
	 * @return
	 */
	public boolean mHasNotSend();
}
