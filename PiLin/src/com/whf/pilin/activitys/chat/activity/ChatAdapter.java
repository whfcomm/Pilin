package com.whf.pilin.activitys.chat.activity;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.chat.fragments.ChatCommonMessage;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;

public class ChatAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<CommonMessage> messages;
	
	private MessageDAO messageDAO;
	
	private String uid;
	
	private ImageFetcher mImageFetcher;
	
	public ChatAdapter(Context context,List<CommonMessage> messages){
		
		messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
		
		 uid = MXmppConnManager.hostUid;
		
		this.context = context;
		
		this.messages = messages;
		
		initImageFetcher();
		
	}
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		
		CommonMessage msgs = messages.get(position);
		
		MSG_STATE state = msgs.getState();
		
		if(!state.equals(MSG_STATE.READED) &&
		   !state.equals(MSG_STATE.RECEIVEING) && 
		   !state.equals(MSG_STATE.SENDDING) && 
		   msgs.getMsgComeFromType().equals(MSG_DERATION.RECEIVE)){
			
			messageDAO.updateById(msgs.getId(),uid,0);
			
		}
		
		
		
		return msgs;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CommonMessage msg = (CommonMessage)getItem(position);
		
		ChatCommonMessage commonMessage = ChatCommonMessage.getInstance(msg, context,mImageFetcher);
		
		commonMessage.fillContent();
		
		View view = commonMessage.getRootView();
		
		return view;
	}

	protected void initImageFetcher() {
		
		mImageFetcher = new ImageFetcher(context, 40 ,40);
		
		mImageFetcher.setImageCache(CacheUtils.getImageCache(context, "imageCache/"));
		
		mImageFetcher.setLoadingImage(R.drawable.people_icon_selector);
		
		mImageFetcher.setImageFadeIn(true);
		
	}
	
}
