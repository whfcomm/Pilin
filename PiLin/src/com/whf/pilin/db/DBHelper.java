package com.whf.pilin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context){
		super(context, "BLIT_MAP.db", null, 3);
	}
	
	public void CreateSelfTable(String uid){
		
		SQLiteDatabase db = getWritableDatabase();
		
		db.beginTransaction();
		
		Cursor cursor = null;
			//登录的人与别人的聊天记录
			cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?" ,new String[]{"BLIT_MESSAGE_PEOPLE_" + uid});
			cursor.moveToNext();
			if(cursor.getLong(0)==0){
				db.execSQL("CREATE TABLE  BLIT_MESSAGE_PEOPLE_"+ uid  + " (id integer primary key autoincrement,"
						+ "uid varchar(50),"
						+ "contenttype integer check (contenttype >=0 and contenttype <=3),"
						+ "avatar varchar(400),"
						+ "state integer," 
						+ "content varchar(2000),"
						+ "msgtime varchar(30),"
						+ "msgDirection integer check (msgDirection >=0 and msgDirection <= 1),"
						+ "distance varchar(40),"
						+ "name varchar(30))");
				
			}
	
			cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?",new String[]{"BLIT_CHATTING_PEOPLE_"+uid});
			cursor.moveToNext();
			if(cursor.getLong(0)==0){
				//正在对话的联系人列表
				db.execSQL("CREATE TABLE  BLIT_CHATTING_PEOPLE_" + uid  +" (id integer primary key autoincrement,uid varchar(50))");
			}

			cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name = ?",new String[]{"BLIT_PEOPLE_" + uid});
			cursor.moveToNext();
			if(cursor.getLong(0)==0){
				//人员信息表
				db.execSQL("CREATE TABLE BLIT_PEOPLE_" + uid  +" (id integer primary key autoincrement,"
														   + "uid varchar(50) unique,"
														   + "avatar varchar(200),"
														   + "isVip integer,"
														   + "isGroupRole integer,"
														   + "industry varchar(200),"
														   + "isbindWeibo integer,"
														   + "isbindRenRen integer,"
														   + "isbindTxWeibo integer,"
														   + "device integer,"
														   + "isRelation integer,"
														   + "isMultipic integer,"
														   + "name varchar(200),"
														   + "gender integer,"
														   + "genderId integer,"
														   + "gengerBgId integer,"
														   + "age integer,"
														   + "distance varchar(20),"
														   + "time varchar(10),"
														   + "sign varchar(300),"
														   + "state integer)");
			}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//本地保存经纬度
		db.execSQL("CREATE TABLE BLIT_OVERLAY (id integer primary key autoincrement,imie varchar(20),lat varchar(20),lng varchar(20),insertTime varchar(20),position varchar(200))");
		//没有发送出去的坐标点
		db.execSQL("CREATE TABLE BLIT_OVERLAY_NOTSEND (id integer primary key autoincrement,not_send_rowid varchar(20))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldv, int newv) {
		//db.execSQL("ALTER TABLE BLIT_OVERLAY ADD COLUMN position varchar(200)");
		//db.execSQL("CREATE TABLE BLIT_OVERLAY_NOTSEND (id integer primary key autoincrement,not_send_rowid varchar(20))");
		//db.execSQL("CREATE TABLE BLIT_MESSAGE_PEOPLE (id integer primary key autoincrement,uid varchar(50),type integer check (type in(0,1)),imgcacheDir varchar(400),state integer check (state in(0,1,2)),textmsg varchar(2000),msgtime varchar(30))");
	}

}
