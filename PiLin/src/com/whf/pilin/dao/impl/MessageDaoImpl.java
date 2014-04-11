package com.whf.pilin.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.db.DBHelper;
import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;

public class MessageDaoImpl implements MessageDAO {
	
	public static String TABLE = "BLIT_MESSAGE_PEOPLE_";
	
	private DBHelper dbHelper;
	
	public MessageDaoImpl(Context context){
		
		dbHelper = new DBHelper(context);
		
	}
	
	@Override
	public long save(CommonMessage message,String uid) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		
		values.put("uid", message.getUid());
		
		values.put("msgDirection", getMsgDirectionFromMSG_DIRECTION(message.getMsgComeFromType()));
		
		values.put("avatar", message.getAvatar());
		
		values.put("state", getStateFromMSG_STATE(message.getState()));
		
		values.put("content", message.getContent());
		
		values.put("msgtime", message.getTime()+"");
		
		values.put("distance", message.getDistance());
		
		values.put("contenttype", getContextTypeFromMSG_CONTENT_TYPE(message.getContentType()));
		
		values.put("name", message.getName());
		
		long rowid = db.insert(TABLE + uid.split("@")[0], null, values);
		
		System.out.println("MESSAGE INSERT " + rowid);
		
		db.setTransactionSuccessful();
		
		db.endTransaction();
		
		return rowid;
	}

	@Override
	public List<CommonMessage> findMessageByUid(int page,int pageSize ,String uid,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		List<CommonMessage> megs = new ArrayList<CommonMessage>();
		
		Cursor cursor = db.query(TABLE + hostUid.split("@")[0], null, "uid = ?", new String[]{uid}, null, null, "msgtime desc", 
						page==1?(0+","+pageSize):((((page-1)*pageSize) + "")+ ","+(pageSize)+""));
		
		while(cursor.moveToNext()){
			
			int state = cursor.getInt(4);
			
			int contextType = cursor.getInt(2);
			
			int msgDirection = cursor.getInt(7);
			
			megs.add(new CommonMessage(cursor.getInt(0),
								  uid,
								  cursor.getString(3),
								  Long.valueOf(cursor.getString(6)),
								  cursor.getString(8),
								  cursor.getString(5),
								  convertToMsgStateEnum(state),
								  convertToContextTypeEnum(contextType),
								  convertToMsgDirectionEnum(msgDirection),
								  cursor.getString(9)
								  ));
			
		}
		
		Collections.reverse(megs);
		
		return megs;
	}
	
	public MSG_STATE convertToMsgStateEnum(int state){
		
		MSG_STATE mState = MSG_STATE.READED;
		
		switch (state) {
		
		case 0:
			
			mState = MSG_STATE.READED;
			
			break;

		case 1:
			
			mState = MSG_STATE.ARRIVED;
			
			break;
		
		case 2:
			
			mState = MSG_STATE.FAILED;
			
			break;
			
		case 3:
			
			mState = MSG_STATE.RECEIVEING;
			
			break;	
			
		case 4:
			
			mState = MSG_STATE.SENDDING;
			
			break;
			
		}
		
		return mState;
	}
	
	public MSG_CONTENT_TYPE convertToContextTypeEnum(int state){
		
		MSG_CONTENT_TYPE mState = MSG_CONTENT_TYPE.TEXT;
		
		switch (state) {
		
		case 0:
			
			mState = MSG_CONTENT_TYPE.TEXT;
			
			break;

		case 1:
			
			mState = MSG_CONTENT_TYPE.IMAGE;
			
			break;
		
		case 2:
			
			mState = MSG_CONTENT_TYPE.MAP;
			
			break;	
			
		case 3:
			
			mState = MSG_CONTENT_TYPE.VOICE;
			
			break;		
			
		}
		return mState;
	}
	
	public MSG_DERATION convertToMsgDirectionEnum(int state){
		
		MSG_DERATION mState = MSG_DERATION.SEND;
		
		switch (state) {
		
		case 0:
			
			mState = MSG_DERATION.SEND;
			
			break;

		case 1:
			
			mState = MSG_DERATION.RECEIVE;
			
			break;
		
		}
		return mState;
	}
	
	public int getStateFromMSG_STATE(MSG_STATE mState){
		
		int state = 0;
		
		switch (mState) {
		
		case READED:
			
			state = 0;
			
			break;

		case ARRIVED:
			
			state = 1;
			
			break;
			
		case FAILED:
			
			state = 2;
		
			break;
			
		case RECEIVEING:
			
			state = 3;
			
			break;
		
		case SENDDING:
			
			state = 4;
			
			break;
			
		}
		return state;
	}
	
	public int getContextTypeFromMSG_CONTENT_TYPE(MSG_CONTENT_TYPE mType){
		
		int type = 0;
		
		switch (mType) {
		
		case TEXT:
			
			type = 0;
			
			break;

		case IMAGE:
			
			type = 1;
			
			break;
			
		case MAP:
			
			type = 2;
			
			break;
			
		case VOICE:
			
			type = 3;
			
			break;
		}
		return type;
	}
	
	public int getMsgDirectionFromMSG_DIRECTION(MSG_DERATION mState){
		
		int state = 0;
		
		switch (mState) {
		
		case SEND:
			
			state = 0;
			
			break;

		case RECEIVE:
			
			state = 1;
			
			break;
			
		}
		
		return state;
	}
	@Override
	public long findReceiveButNotReadByUid(String uid,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
				
		Cursor cursor = db.rawQuery("SELECT COUNT(1) FROM " + TABLE + hostUid.split("@")[0] + " WHERE uid = ? and state != ? and msgDirection = 1 ", new String [] {uid,0+""});	
		
		cursor.moveToNext();
		
		long count = cursor.getLong(0);
		
		cursor.close();
		
		return count;
	}
	
	@Override
	public void closeDB() {
		dbHelper.close();
	}

	@Override
	public CommonMessage findLastMesgByUid(String uid,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE + hostUid.split("@")[0], null , "uid = ?", new String [] {uid},null, null, " id desc","0,1");
		
		CommonMessage commonMessage = null;
		
		if(cursor.moveToNext()){
			
			int state = cursor.getInt(4);
			
			int contextType = cursor.getInt(2);
			
			int msgDirection = cursor.getInt(7);
			
			commonMessage = new CommonMessage(cursor.getInt(0),
					  uid,
					  cursor.getString(3),
					  Long.valueOf(cursor.getString(6)),
					  cursor.getString(8),
					  cursor.getString(5),
					  convertToMsgStateEnum(state),
					  convertToContextTypeEnum(contextType),
					  convertToMsgDirectionEnum(msgDirection),
					  cursor.getString(9)
					  );
			
		}
		
		return commonMessage;
	}

	@Override
	public CommonMessage findByRownum(long rowid,String uid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + uid.split("@")[0] + " WHERE rowid = ? ", new String[]{rowid+""});
		
		CommonMessage commonMessage = null;
		
		while(cursor.moveToNext()){
			
			int state = cursor.getInt(4);
			
			int contextType = cursor.getInt(2);
			
			int msgDirection = cursor.getInt(7);
			
			commonMessage = new CommonMessage(cursor.getInt(0),
					  cursor.getString(1),
					  cursor.getString(3),
					  Long.valueOf(cursor.getString(6)),
					  cursor.getString(8),
					  cursor.getString(5),
					  convertToMsgStateEnum(state),
					  convertToContextTypeEnum(contextType),
					  convertToMsgDirectionEnum(msgDirection),
					  cursor.getString(9)
					  );
			
		}
		
		cursor.close();
		
		return commonMessage;
	}

	@Override
	public void updateById(int id ,String uid,int state) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		db.beginTransaction();
		
		db.execSQL("UPDATE " + TABLE + uid.split("@")[0] +" SET state = ? WHERE id = ?", new String[]{state+"",id+""});
	
		db.setTransactionSuccessful();
		
		db.endTransaction();
	}

	@Override
	public int getMaxPage(String uid, int pageSize,String hostUid) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT COUNT(1) FROM " + TABLE + hostUid.split("@")[0] + " WHERE uid = ? ", new String [] {uid});
		
		cursor.moveToNext();
		
		long num = cursor.getLong(0);
		
		int maxPage = ((int)num/pageSize) + (num%pageSize==0?0:1);
		
		return maxPage;
	}

	@Override
	public void updateStateByRowid(long rowid, String uid,int state) {
		
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		
//		db.beginTransaction();
//		
//		db.execSQL("UPDATE " + TABLE + uid.split("@")[0] +" SET state = ? WHERE rowid = ?", new String[]{state+"",""+rowid});
//	
		CommonMessage msg = findByRownum(rowid, uid);
		
		updateById(msg.getId(), uid,state);
		
//		db.update(TABLE + uid.split("@")[0], values, "rowid = ?", new String[]{rowid+""});
//		
//		db.setTransactionSuccessful();
//		
//		db.endTransaction();
		
	}
	
}
