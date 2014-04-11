package com.whf.pilin.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.whf.pilin.dao.NearByPeopleDAO;
import com.whf.pilin.db.DBHelper;
import com.whf.pilin.entity.NearByPeople;

public class NearByPeopleDaoImpl implements NearByPeopleDAO {
	
	public static String TABLE = "BLIT_PEOPLE_";
	
	private DBHelper dbHelper;
	
	public NearByPeopleDaoImpl(Context context){
		dbHelper = new DBHelper(context);
	}
	
	
	
	@Override
	public void save(NearByPeople byPeople,String uid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT COUNT(1) FROM " + TABLE + uid.split("@")[0] + " WHERE uid = ?", new String[]{byPeople.getUid()});
		
		if(cursor.moveToNext()){
			
			cursor.close(); 
			
			return;
			
		}
		
		cursor.close();
		
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		
		values.put("uid", byPeople.getUid());
		values.put("avatar", byPeople.getAvatar());
		values.put("isVip", byPeople.getIsVip());
		values.put("isGroupRole", byPeople.getIsGroupRole());
		values.put("industry", byPeople.getIndustry());
		values.put("isbindWeibo", byPeople.getIsbindWeibo());
		values.put("isbindTxWeibo", byPeople.getIsbindTxWeibo());
		values.put("isbindRenRen", byPeople.getIsbindRenRen());
		values.put("device", byPeople.getDevice());
		values.put("isRelation", byPeople.getIsRelation());
		values.put("isMultipic", byPeople.getIsMultipic());
		values.put("name", byPeople.getName());
		values.put("gender", byPeople.getGender());
		values.put("genderId", byPeople.getGenderId());
		values.put("genderBgId", byPeople.getGenderBgId());
		values.put("age", byPeople.getAge());
		values.put("distance", byPeople.getDistance());
		values.put("time", byPeople.getTime());
		values.put("sign", byPeople.getSign());
		values.put("state", byPeople.getSign());
		
		db.insert(TABLE, null, values);
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
		
		
	}

	@Override
	public List<NearByPeople> findByRelationShip(int type,String uid) {
		
		List<NearByPeople> nearByPeoples = new ArrayList<NearByPeople>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + uid + " WHERE isRelation = ? ", new String [] {type+""});
		
		NearByPeople people = null;
		
		while(cursor.moveToNext()){
			
			people = new NearByPeople(cursor.getString(1),
									  cursor.getString(2),
									  cursor.getInt(3),
									  cursor.getInt(4),
									  cursor.getString(5),
									  cursor.getInt(6),
									  cursor.getInt(7),
									  cursor.getInt(8),
									  cursor.getInt(9),
									  cursor.getInt(10),
									  cursor.getInt(11),
									  cursor.getString(12),
									  cursor.getInt(13),
									  cursor.getInt(14),
									  cursor.getInt(15),
									  cursor.getInt(16),
									  cursor.getString(17),
									  cursor.getString(18),
									  cursor.getString(19),
									  cursor.getInt(20));
			nearByPeoples.add(people);
		}
		
		cursor.close();
		
		return nearByPeoples;
	}

	@Override
	public NearByPeople findByUid(String uid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE uid = ? ", new String [] {uid});
		
		NearByPeople people = null;
		
		while(cursor.moveToNext()){
			
			people = new NearByPeople(cursor.getString(1),
									  cursor.getString(2),
									  cursor.getInt(3),
									  cursor.getInt(4),
									  cursor.getString(5),
									  cursor.getInt(6),
									  cursor.getInt(7),
									  cursor.getInt(8),
									  cursor.getInt(9),
									  cursor.getInt(10),
									  cursor.getInt(11),
									  cursor.getString(12),
									  cursor.getInt(13),
									  cursor.getInt(14),
									  cursor.getInt(15),
									  cursor.getInt(16),
									  cursor.getString(17),
									  cursor.getString(18),
									  cursor.getString(19),
									  cursor.getInt(20));
		}
		
		cursor.close();
		
		return people;
		
	}

	
	
	@Override
	public void closeDB() {
		dbHelper.close();
	}

	@Override
	public void updatePeople(NearByPeople byPeople,String uid) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		
		values.put("uid", byPeople.getUid());
		values.put("avatar", byPeople.getAvatar());
		values.put("isVip", byPeople.getIsVip());
		values.put("isGroupRole", byPeople.getIsGroupRole());
		values.put("industry", byPeople.getIndustry());
		values.put("isbindWeibo", byPeople.getIsbindWeibo());
		values.put("isbindTxWeibo", byPeople.getIsbindTxWeibo());
		values.put("isbindRenRen", byPeople.getIsbindRenRen());
		values.put("device", byPeople.getDevice());
		values.put("isRelation", byPeople.getIsRelation());
		values.put("isMultipic", byPeople.getIsMultipic());
		values.put("name", byPeople.getName());
		values.put("gender", byPeople.getGender());
		values.put("genderId", byPeople.getGenderId());
		values.put("genderBgId", byPeople.getGenderBgId());
		values.put("age", byPeople.getAge());
		values.put("distance", byPeople.getDistance());
		values.put("time", byPeople.getTime());
		values.put("sign", byPeople.getSign());
		values.put("state", byPeople.getSign());
		
		db.update(TABLE + uid.split("@")[0], values, "uid = ?", new String [] {byPeople.getUid()});
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
		
		
	}

}
