package com.whf.pilin.dao;

import java.util.List;

import com.whf.pilin.entity.LocalGeoPoint;

public interface GeoPointDAO extends BaseDAO{
	/**
	 * 查找所有坐标点
	 * @return
	 * @throws Exception
	 */
	public List<LocalGeoPoint> findAll();
	/**
	 * 按时间区域查找坐标点
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<LocalGeoPoint> findByTime(String startTime,String endTime) ;
	
	/**
	 * 保存坐标点
	 * @param point
	 */
	public long savePoint(LocalGeoPoint point);
	/**
	 * 查找今天的坐标
	 * @return
	 */
	public List<LocalGeoPoint> findToday();
	/**
	 * 清空所有数据
	 */
	public void deleteAll();
	
	/**
	 * 根据ROWID查询
	 * @param rowid
	 * @return
	 */
	public List<LocalGeoPoint> findByRowId(List<String> rowid);
}
