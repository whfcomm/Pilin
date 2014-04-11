package com.whf.pilin.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.whf.pilin.dao.GeoPointDAO;
import com.whf.pilin.db.DBHelper;
import com.whf.pilin.entity.LocalGeoPoint;
import com.whf.pilin.utils.TypeConverter;

public class GeoPointDaoImpl implements GeoPointDAO {

	private final static String BLIT_OVERLAY = "BLIT_OVERLAY";
	
	private DBHelper dbHelper;
	
	public GeoPointDaoImpl(Context context) {
		
		dbHelper = new DBHelper(context);

	}
	/**
	 * 查找所有坐标点
	 * @return
	 */
	@Override
	public List<LocalGeoPoint> findAll() {

		SQLiteDatabase db = dbHelper.getReadableDatabase();

		List<LocalGeoPoint> points = new ArrayList<LocalGeoPoint>();
		
		Cursor cursor = db.query(BLIT_OVERLAY, null, null, null, null, null, "insertTime");

		LocalGeoPoint point;
		
		while(cursor.moveToNext()){
			
			point = new LocalGeoPoint(cursor.getInt(0),
									  cursor.getString(1), 
									  cursor.getString(2), 
									  cursor.getString(3), 
									  cursor.getString(4),
									  cursor.getString(5));
			points.add(point);
		}
		
		cursor.close();
		
		return points;
		
	}
	/**
	 * 按时间段查询地点
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public List<LocalGeoPoint> findByTime(String startTime, String endTime) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String [] args = {startTime,endTime};
		
		List<LocalGeoPoint> points = new ArrayList<LocalGeoPoint>();
		
		Cursor cursor = db.query(BLIT_OVERLAY, null, " datetime(insertTime) BETWEEN datetime(?) AND (?) ", args, null, null, "insertTime");
		
		LocalGeoPoint point;
		
		while(cursor.moveToNext()){
			
			point = new LocalGeoPoint(cursor.getInt(0),
									  cursor.getString(1), 
									  cursor.getString(2), 
									  cursor.getString(3), 
									  cursor.getString(4),
									  cursor.getString(5));
			points.add(point);
		}
		cursor.close();
		
		return points;
	}
	/**
	 * 保存坐标点
	 * @param point
	 */
	@Override
	public long savePoint(LocalGeoPoint point) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("imie", point.getImie());
		values.put("lat", point.getLat());
		values.put("lng", point.getLng());
		values.put("insertTime", point.getInsertTime());
		long rowid = db.insert(BLIT_OVERLAY, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		return rowid;
	}
	@Override
	public List<LocalGeoPoint> findToday() {
		
		Calendar c = Calendar.getInstance();
		
		String date = c.get(Calendar.YEAR) + "";
		
		date = date + "-" + (c.get(Calendar.MONTH) + 1);
		
		date = date + "-" + c.get(Calendar.DAY_OF_MONTH);
		
		date = date + " 00:00:00";
		
		try {
		date = TypeConverter.formatDate(TypeConverter.getDateFromString(date, "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String endDate = TypeConverter.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		
		return findByTime(date,endDate);
	}
	@Override
	public void deleteAll() {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.execSQL("delete from " + BLIT_OVERLAY);
		
	}
	/**
	 * 关闭数据库
	 */
	public void closeDB(){
		
		dbHelper.close();
		
	}
	
	@Override
	public List<LocalGeoPoint> findByRowId(List<String> rowids) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String array = Arrays.toString(rowids.toArray(new String [rowids.size()]));
		
		array = array.replace("[", "").replace("]", "");
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + BLIT_OVERLAY  + " WHERE rowid in (" + array  +")",null);
		
		List<LocalGeoPoint> localGeoPoints = new ArrayList<LocalGeoPoint>();
		
		while(cursor.moveToNext()){
			
			localGeoPoints.add(new LocalGeoPoint(cursor.getInt(0),
											  cursor.getString(1), 
											  cursor.getString(2), 
											  cursor.getString(3), 
											  cursor.getString(4),
											  cursor.getString(5)));
			
		}
		
		cursor.close();
		
		return localGeoPoints;
	}
	
}
