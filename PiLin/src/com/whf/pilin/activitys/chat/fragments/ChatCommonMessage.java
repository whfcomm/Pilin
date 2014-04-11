package com.whf.pilin.activitys.chat.fragments;

import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.activitys.chat.activity.ChatActivity;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.utils.TypeConverter;


public abstract class ChatCommonMessage {
	
	protected Context mContext;
	protected View mRootView;
	
	protected ImageFetcher mImageFetcher;
	
	/**
	 * 每条信息上提示的时间与距离
	 */
	private RelativeLayout mLayoutTimeStampContainer;
	private TextView mHtvTimeStampTime;
	private TextView mHtvTimeStampDistance;
	
	/**
	 * 左边消息送达情况
	 */
	private RelativeLayout mLayoutLeftContainer;
	private LinearLayout mLayoutStatus;
	private TextView mHtvStatus;
	
	/**
	 * 消息容器
	 */
	protected LinearLayout mLayoutMessageContainer;
	
	/**
	 * 右边消息状况
	 */
	private LinearLayout mLayoutRightContainer;
	private ImageView mIvPhotoView;
	/**
	 * 布局加载器
	 */
	protected LayoutInflater mInflater;
	/**
	 * 消息
	 */
	protected CommonMessage mMsg;
	/**
	 * 背景颜色
	 */
	protected int mBackground;
	
	public ChatCommonMessage(CommonMessage message,Context context,ImageFetcher imageFetcher){
		
		mMsg = message;
		
		mContext = context;
		
		mInflater = LayoutInflater.from(context);
		
		this.mImageFetcher = imageFetcher;
	}
	
	public static ChatCommonMessage getInstance(CommonMessage message,Context context,ImageFetcher imageFetcher){
		
		ChatCommonMessage cMessage = null;
		
		switch (message.getContentType()) {
		
		case TEXT:
			
			cMessage = new ChatTextMessage(message,context,imageFetcher);
			
			break;

		case IMAGE:
			
			cMessage = new ChatImageMessage(message,context,imageFetcher);
			
			break;
		
		case MAP:
			
			cMessage = new ChatMapMessage(message,context,imageFetcher);
			
			break;
			
		case VOICE:
			
			cMessage = new ChatVoiceMessage(message,context,imageFetcher);
			
		}
		
		cMessage.init(message.getMsgComeFromType());
		
		return cMessage;
		
	}
	
	private void init(MSG_DERATION type){
		
		switch (type) {
		
		case RECEIVE:
			
			mBackground = R.drawable.chat_from_msg_background_selector;
			
			mRootView = mInflater.inflate(R.layout.message_group_receive_template, null);
			
			break;

		case SEND:
			
			mBackground = R.drawable.chat_to_msg_background_selector;
			
			mRootView = mInflater.inflate(R.layout.message_group_send_template, null);
			
			break;
		}
		
		if(mRootView != null){
			
			initView(mRootView);
			
		}
		
	}

	protected void initView(View view) {
		
		mLayoutTimeStampContainer = (RelativeLayout)view.findViewById(R.id.message_layout_timecontainer);
		
		mHtvTimeStampTime = (TextView)view.findViewById(R.id.message_timestamp_htv_time);
		 
		mHtvTimeStampDistance = (TextView)view.findViewById(R.id.message_timestamp_htv_distance);
		
		
		mLayoutLeftContainer = (RelativeLayout)view.findViewById(R.id.message_layout_leftcontainer);
		
		mLayoutStatus = (LinearLayout)view.findViewById(R.id.message_layout_status);
		
		mHtvStatus = (TextView)view.findViewById(R.id.message_htv_status);
		
		
		mLayoutMessageContainer = (LinearLayout)view.findViewById(R.id.message_layout_messagecontainer);
		mLayoutMessageContainer.setBackgroundResource(mBackground);
		
		
		mLayoutRightContainer = (LinearLayout)view.findViewById(R.id.message_layout_rightcontainer);
		mIvPhotoView = (ImageView)view.findViewById(R.id.message_iv_userphoto);
		
		onInitViews();
	}
	
	public void fillContent(){
		
		fillTimeStamp();
		
		fillState();
		
		fillMessage();
		
		fillPhotoView();
		
	}
	
	protected void fillMessage() {
		onFillMessage();
	}
	/**
	 * 设置发送消息时间及距离
	 */
	protected void fillTimeStamp() {
		
		mLayoutTimeStampContainer.setVisibility(View.VISIBLE);
		
		if(mMsg.getTime() != 0){
			
			mHtvTimeStampTime.setText(TypeConverter.formatDate(new Date(mMsg.getTime()), "MM-dd HH:mm:ss"));
			
		}
		if(mMsg.getDistance() != null){
			
			mHtvTimeStampDistance.setText(mMsg.getDistance());
			
		}else{
			
			mHtvTimeStampDistance.setText("未知");
			
		}
	}
	/**
	 * 设置消息发送状态
	 */
	protected void fillState() {
		
		mLayoutLeftContainer.setVisibility(View.VISIBLE);
		
		mLayoutStatus.setBackgroundResource(R.drawable.bg_message_status_sended);
		
		mHtvStatus.setText("送达");
		
	}
	/**
	 * 设置发送消息的头像
	 */
	protected void fillPhotoView() {
		
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		
		mImageFetcher.loadImage("http://a.xnimg.cn/n/apps/login/v6/res/logo_v6.png", mIvPhotoView);
		
	}
	
	
	
	protected void refreshAdapter(){
		
		((ChatActivity)mContext).refreshAdapter();
		
	}
	/**
	 * 获取最外部元素
	 * @return
	 */
	public View getRootView(){
		return mRootView;
	}
	
	protected abstract void onInitViews();
	
	protected abstract void onFillMessage();
	
}
