package com.whf.pilin.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.whf.pilin.dao.ChattingPeopleDAO;
import com.whf.pilin.db.DBHelper;

public class ChattingPeopleDaoImpl implements ChattingPeopleDAO {
	
	public static String TABLE = "BLIT_CHATTING_PEOPLE_";
	
	private DBHelper dbHelper;
	
	public ChattingPeopleDaoImpl(Context context){
		
		dbHelper = new DBHelper(context);
		
	}
	
	@Override
	public void save(String uid,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT uid FROM " + TABLE + hostUid.split("@")[0] + " WHERE uid = ? ", new String [] {uid});		
		
		if(cursor.moveToNext()){
			
			cursor.close();
			
			return;
			
		}
		
		cursor.close();
		
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		
		values.put("uid", uid);
		
		long rowid = db.insert(TABLE + hostUid.split("@")[0], null, values);
		
		System.out.println("CHATTING PEOPLE INSERT " + rowid);
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
		
	}

	@Override
	public void delete(String uid,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.beginTransaction();
		
		db.delete(TABLE + hostUid.split("@")[0], "uid = ?", new String [] {uid});
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
		
	}
	
	@Override
	public boolean peopleChatting(String uid,String hostUid){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT uid FROM " + TABLE + hostUid.split("@")[0] + " WHERE uid = ? ", new String [] {uid});		
		
		if(cursor.moveToNext()){ 
			
			cursor.close();
			
			return true;
			
		}else{ 
			
			cursor.close();
			
			return false;
		}
		
	}
	
	@Override
	public void closeDB() {
		dbHelper.close();
	}
	

}
