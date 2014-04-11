package com.whf.pilin.activitys.chat.activity;

import java.io.IOException;
import java.lang.ref.SoftReference;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.adapter.chat.FaceGridViewAdapter;
import com.whf.pilin.bitmap.cache.ImageResizer;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.connection.MChatManager;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.entity.NearByPeople;
import com.whf.pilin.utils.FileUtils;
import com.whf.pilin.utils.PictureViewUtils;
import com.whf.pilin.utils.ToastUtils;
import com.whf.pilin.utils.TypeConverter;
import com.whf.pilin.view.CommonChatListView;
import com.whf.pilin.view.EmotionEditText;

public class ChatActivity extends BaseChatActivity implements OnRefreshListener{

	private CommonMessage message;

	private String prevMsg;

	private Chat mChat;
	
	private MessageDAO messageDAO;
	
	private NearByPeople userInfo;
	
	private MChatManager mChatManager;
	
	private SwipeRefreshLayout mRfLayout;
	
	private int page = 1;
	
	private int maxPage = 1;
	
	private long sendRowid = 0;
	
	private String hostUid = MXmppConnManager.hostUid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 获取从上面获取来的用户数据
		Bundle bundle = getIntent().getExtras();
		userInfo = (NearByPeople) bundle.getSerializable("user");
		getActionBar().setTitle("与 " + userInfo.getName() + " 聊天中");
		mChatManager = new MChatManager(MXmppConnManager.getInstance().getChatManager());
		mChat = mChatManager.createChat(userInfo.getUid(),MXmppConnManager.getInstance().getChatMListener().new MsgProcessListener());
		messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
		
		if(PiLinApplication.mUsrChatMsgPage.get(userInfo.getUid())== null){
			PiLinApplication.mUsrChatMsgPage.put(userInfo.getUid(), 1);
		}else{
			page = PiLinApplication.mUsrChatMsgPage.get(userInfo.getUid());
		}
		
		maxPage = messageDAO.getMaxPage(userInfo.getUid(),CustomConst.MESSAGE_PAGESIZE,hostUid);
		
		messages.addAll(messageDAO.
				findMessageByUid(1,CustomConst.MESSAGE_PAGESIZE*page, userInfo.getUid().trim(),hostUid));
		
		PiLinApplication.putHandler(userInfo.getUid() , handler);
		initViews();
		initEvents();
		refreshAdapter();
	}
	
	@Override
	protected void initViews() {
		
		mLayoutEmotionMedia = (FrameLayout)findViewById(R.id.fl_emotion_media);
		
		mLoPlusBarPic = (View)findViewById(R.id.include_chat_plus_pic);
		
		mLoPlusBarCarema = (View)findViewById(R.id.include_chat_plus_camera);
		
		mLoPlusBarLocation = (View)findViewById(R.id.include_chat_plus_location);
		
		mIvPlusBarPic = (ImageView)mLoPlusBarPic.findViewById(R.id.iv_chat_plus_image);
		
		mIvPlusBarCarema = (ImageView)mLoPlusBarCarema.findViewById(R.id.iv_chat_plus_image);
		
		mIvPlusBarLocation = (ImageView)mLoPlusBarLocation.findViewById(R.id.iv_chat_plus_image);
		
		mTvPlusBarPic = (TextView)findViewById(R.id.include_chat_plus_pic).findViewById(R.id.tv_chat_plus_description);
		
		mTvPlusBarCarema = (TextView)findViewById(R.id.include_chat_plus_camera).findViewById(R.id.tv_chat_plus_description);
		
		mTvPlusBarLocation = (TextView)findViewById(R.id.include_chat_plus_location).findViewById(R.id.tv_chat_plus_description);
		
		mTvPlusBarCarema.setText("拍照");
		
		mTvPlusBarPic.setText("图片");
		
		mTvPlusBarLocation.setText("位置");
		
		mRfLayout = (SwipeRefreshLayout)findViewById(R.id.srfl_chat);
		
		mRfLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		
		mBtnMsgSend = (Button) findViewById(R.id.btn_chat_send);

		mEmtMsg = (EmotionEditText) findViewById(R.id.et_chat_msg);

		mIvEmotion = (ImageView) findViewById(R.id.iv_chat_biaoqing);
		
		mIvMedia = (ImageView)findViewById(R.id.iv_chat_media);
		
		mLvCommonMsg = (CommonChatListView) findViewById(R.id.lv_chat);

		mGvEmotion = (GridView)findViewById(R.id.gv_chat_biaoqing);
		
		mLayoutEmotionMedia.setVisibility(View.GONE);
		
		mLayoutEmotion = (LinearLayout)findViewById(R.id.ll_chat_face);
		
		mLayoutMedia = (LinearLayout)findViewById(R.id.ll_chat_plusbar);
		
		loadImage();
		
		mAdapter = new ChatAdapter(this, messages);

		mLvCommonMsg.setAdapter(mAdapter);
		
		mGvEmotion.setAdapter(new FaceGridViewAdapter(this));
		
	}
	
	private Bitmap getBitmap(String key,int res){
		
		SoftReference<Bitmap> soft = PiLinApplication.getInstance().mSendbarCache.get(key);
		Bitmap bitmap = null;
		if(soft == null){
			bitmap = ImageResizer.decodeSampledBitmapFromResource(getResources(), res, 50, 50);
			soft = new SoftReference<Bitmap>(bitmap);
			PiLinApplication.getInstance().mSendbarCache.put(key,soft);
		}else{
			bitmap = soft.get();
		}
		return bitmap;
	}
	
	/**
	 * 加载多媒体框内的图片
	 */
	private void loadImage(){
		//添加多媒体窗口内的多媒体图表--图片
		Bitmap bitmap = getBitmap(getString(R.string.plusbar_pic),R.drawable.ic_chat_plusbar_pic_normal);
		mIvPlusBarPic.setImageBitmap(bitmap);
		
		//添加多媒体窗口内的多媒体图表--拍照
		bitmap = getBitmap(getString(R.string.plusbar_camera),R.drawable.ic_chat_plusbar_camera_normal);
		mIvPlusBarCarema.setImageBitmap(bitmap);
		
		//添加多媒体窗口内的多媒体图表--位置
		bitmap = getBitmap(getString(R.string.plusbar_location),R.drawable.ic_chat_plusbar_location_normal);
		mIvPlusBarLocation.setImageBitmap(bitmap);
		
	}
	
	
	
	@Override
	protected void initEvents() {
		mRfLayout.setOnRefreshListener(this);
		mBtnMsgSend.setOnClickListener(this);
		mIvEmotion.setOnClickListener(this);
		mEmtMsg.setOnTouchListener(this);
		mLvCommonMsg.setOnTouchListener(this);
		mGvEmotion.setOnItemClickListener(this);
		mIvMedia.setOnClickListener(this);
		mLoPlusBarPic.setOnClickListener(this);
		mLoPlusBarCarema.setOnClickListener(this);
		mLoPlusBarLocation.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.iv_chat_media:
			/*
			 * 如果素材库框显示
			 */
			if(mLayoutEmotionMedia.isShown()){
				/*
				 * 如果表情框是可视的，这时只需跳到多媒体框来显示
				 * 否则就证明当前打开的是多媒体框，就隐藏整个素材库;
				 */
				if(mLayoutEmotion.isShown()){
					mLayoutEmotion.setVisibility(View.GONE);
					mLayoutMedia.setVisibility(View.VISIBLE);
				}else{
					mLayoutEmotionMedia.setVisibility(View.GONE);
				}
			/*
			 * 如果素材库当前不显示
			 * 无论怎样，先把表情框隐藏，然后设置	
			 * 多媒体框为显示
			 */
			}else{
				mLayoutEmotion.setVisibility(View.GONE);
				mLvCommonMsg.setSelection(messages.size() - 1);
				mInputManager.hideSoftInputFromWindow(mEmtMsg.getWindowToken(),
						0);
				mLayoutEmotionMedia.setVisibility(View.VISIBLE);
				mLayoutMedia.setVisibility(View.VISIBLE);
			}
			
			break;
		//点击的是表情框
		case R.id.iv_chat_biaoqing:
			/*
			 * 如果素材库显示
			 */
			if(mLayoutEmotionMedia.isShown()){
				/*
				 *如果表情框显示
				 *则隐藏整个素材库 
				 */
				if(mLayoutEmotion.isShown()){
					mLayoutEmotionMedia.setVisibility(View.GONE);
				/*
				 * 如果表情框隐藏
				 * 则隐藏多媒体框
				 * 设置表情库的标志
				 * 显示表情框	
				 */
				}else{
					mLayoutMedia.setVisibility(View.GONE);
					mLayoutEmotion.setVisibility(View.VISIBLE);
				}
			/*
			 * 如果素材库不显示，
			 * 无论怎样先隐藏掉多媒体框；
			 */
			}else {
				mLayoutMedia.setVisibility(View.GONE);
				mLvCommonMsg.setSelection(messages.size() - 1);
				mInputManager.hideSoftInputFromWindow(mEmtMsg.getWindowToken(),
						0);
				mLayoutEmotion.setVisibility(View.VISIBLE);
				mLayoutEmotionMedia.setVisibility(View.VISIBLE);
			}
			break;
		//点击发送按钮
		case R.id.btn_chat_send:

			if ("".equals(mEmtMsg.getText().toString())) {

				Toast.makeText(this, "先输入信息", Toast.LENGTH_SHORT).show();

			} else {
				prevMsg = mEmtMsg.getText().toString();
				mEmtMsg.setText("");
				try {
					mChat.sendMessage(prevMsg);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				
				message = new CommonMessage(
						userInfo.getUid().trim(),
						"nearby_people_other",
						System.currentTimeMillis(),
						"0.12km",
						prevMsg,
						MSG_STATE.ARRIVED,
						MSG_CONTENT_TYPE.TEXT,
						MSG_DERATION.SEND,
						userInfo.getName()
						) ;
				
				messageDAO.save(message,hostUid);
				
				messages.add(message);
				
				refreshAdapter();
			}

			break;
			
		case R.id.include_chat_plus_pic:
			
			Intent intent = PictureViewUtils.getPictureIntent();
			
			startActivityForResult(intent, CustomConst.MEDIA_CODE_PICTURE);
			
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(requestCode == CustomConst.MEDIA_CODE_PICTURE && resultCode == RESULT_OK){
				
			String path = FileUtils.getPictureSelectedPath(intent, this);
			String newPath = CacheUtils.getImagePath(mApplication, "sendImage/" + TypeConverter.getUUID() + ".pilin");
			try {
			Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(path, 400, 800);
			FileUtils.compressAndWriteFile(bitmap, mApplication, newPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			long mills = System.currentTimeMillis();
			
			message = new CommonMessage(
						userInfo.getUid().trim(),
						"nearby_people_other",
						mills,
						"0.12km",
						newPath,
						MSG_STATE.SENDDING,
						MSG_CONTENT_TYPE.IMAGE,
						MSG_DERATION.SEND,
						userInfo.getName()
						) ;
				
			sendRowid = messageDAO.save(message, hostUid);
				
			messages.add(message);
			
			new SendFileThread(mills,sendRowid,newPath, userInfo.getUid(),CustomConst.FILETYPE_IMAGE).start();
			
			refreshAdapter();
			
		}else if(resultCode == CustomConst.MEDIA_CODE_CAMERA && resultCode == RESULT_OK){
			
			Bundle bundle = intent.getExtras();
			
			Bitmap bitmap = (Bitmap)bundle.get("data");
			
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {

		case R.id.et_chat_msg:

			if (event.getAction() == MotionEvent.ACTION_UP) {

				if (mLayoutEmotionMedia.isShown()) {
					mLayoutEmotionMedia.setVisibility(View.GONE);
				}
			}
			break;

		case R.id.lv_chat:

			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (mLayoutEmotionMedia.isShown()) {
					mLayoutEmotionMedia.setVisibility(View.GONE);
				}
				mInputManager.hideSoftInputFromWindow(
						mEmtMsg.getWindowToken(), 0);
			}
			break;

		}

		return false;
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			
			switch(msg.what){
				
				case CustomConst.HANDLER_MGS_ADD:
			
					long rowid = (long)msg.obj;
					
					message = messageDAO.findByRownum(rowid,hostUid);
					
					messages.add(message);
					
					refreshAdapter();
					
					break;
				
				case CustomConst.HANDLER_MSG_FILE_SUCCESS:
					
					long mills = (long)msg.obj;
					
					updateMsgByMills(mills);
					
					refreshAdapter();
					
					break;
			}

		};

	};
	
	private void updateMsgByMills(long mills){
		
		for(int i=0;i<messages.size();i++){
			
			CommonMessage msg = messages.get(i);
			
			if(msg.getTime() == mills){
				
				msg.setState(MSG_STATE.ARRIVED);
				
				break;
				
			}
			
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long mills) {
		
		String text = PiLinApplication.mEmotions_Zme.get(position);
		
		if(!TextUtils.isEmpty(text)){
			
			int start = mEmtMsg.getSelectionStart();
			
			CharSequence content = mEmtMsg.getText().insert(start, text);
			
			mEmtMsg.setText(content);
			//定位光标
			CharSequence info = mEmtMsg.getText();
			
			if(info instanceof Spannable){
				
				Spannable spanText = (Spannable)info;
				
				Selection.setSelection(spanText, start + text.length());
				
			}
			
		}
		
		
	}
	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
		mLvCommonMsg.setSelection(messages.size()-1);
	}
	
	@Override
	protected void onDestroy() {
		messageDAO.closeDB();
		PiLinApplication.removeHandler(userInfo.getUid(),handler);
		super.onDestroy();
	}

	@Override
	public void onRefresh() {
		
		page = PiLinApplication.mUsrChatMsgPage.get(userInfo.getUid()) + 1;
		
		if(page <= maxPage){
			
			PiLinApplication.mUsrChatMsgPage.put(userInfo.getUid(), page);
			
			messages.addAll(0,messageDAO.
					findMessageByUid(page, CustomConst.MESSAGE_PAGESIZE, userInfo.getUid().trim(),hostUid));
			
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mAdapter.notifyDataSetChanged();
					mRfLayout.setRefreshing(false);
				}
			}, 2000);
			
		}else{
			ToastUtils.createCenterNormalToast(this, "已经刷新到最后", Toast.LENGTH_SHORT);
			mRfLayout.setRefreshing(false);
		}
		
	}
	
	/**
	 * 发送文件线程
	 * @author WHF
	 *
	 */
	class SendFileThread  extends Thread{
		
		private String file;
		
		private String userid;
		
		private String type;
		
		private long rowid;
		
		private long mills;
		
		public  SendFileThread (long mills,long rowid,String file,String userid,String type){
			this.file = file;
			this.userid = userid;
			this.type = type;
			this.rowid = rowid;
			this.mills = mills;
		}
		
		@Override
		public synchronized void run() {
			
			MXmppConnManager.getInstance().sendFile(mills,rowid,type,file, handler, userid);
			
		}
	}
}
