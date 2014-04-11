package com.whf.pilin.adapter.main.chatting;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.ChattingPeople;
import com.whf.pilin.utils.TypeConverter;
import com.whf.pilin.view.EmotionTextView;

public class ChattingMsgAdapter extends BaseAdapter {
	
	//private List<List<CommonMessage>> mUsrmessages;
	
	private List<ChattingPeople> chattingPeoples;
	
	private Context context;
	
	private ImageFetcher imageFetcher;
	
	public ChattingMsgAdapter(Context context ,List<ChattingPeople> chattingPeoples){
		
		this.context = context;
		
		this.chattingPeoples = chattingPeoples;
		
		initImageFetcher();
		
	}
	public List<ChattingPeople> getChattingPeoples(){
		
		return chattingPeoples;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ChattingPeople mUsrmessage = chattingPeoples.get(position);
		
		View view = convertView;
		
		ViewHolder holder = null;
		
		if(view == null){
			
			view = LayoutInflater.from(context).inflate(R.layout.user_info_item_layout, null);
			
			holder = new ViewHolder();
			
			view.setTag(holder);
			
			holder.tv_catalog = (TextView)view.findViewById(R.id.tv_catalog);
			
			holder.iv_icon = (ImageView)view.findViewById(R.id.iv_usr_icon);
			
			holder.tv_niName = (TextView)view.findViewById(R.id.tv_usr_niname);
			
			holder.tv_lastChatTime = (TextView)view.findViewById(R.id.tv_user_state);
			
			holder.tv_lastMsg = (EmotionTextView)view.findViewById(R.id.tv_usr_mood);
			
			holder.tv_msgState = (TextView)view.findViewById(R.id.tv_usr_sex);
			
			holder.tv_msgState.setTextColor(0xff000000);
			
		}else{
			
			holder = (ViewHolder)view.getTag();
			
		}
		
		holder.tv_catalog.setVisibility(View.GONE);
		
		imageFetcher.loadImage("http://imgt5.bdstatic.com/it/u=2795935915,75223816&fm=116&gp=0.jpg", holder.iv_icon);
		
		holder.tv_niName.setText(mUsrmessage.getLastMsg().getName());
		
		holder.tv_lastChatTime.setText(TypeConverter.formatDate(new Date(mUsrmessage.getLastMsg().getTime()), "MM-dd HH:mm:ss"));
		
		MSG_STATE state = mUsrmessage.getLastMsg().getState();
		
		MSG_DERATION deration = mUsrmessage.getLastMsg().getMsgComeFromType();
		
		if(mUsrmessage.getLastMsg().getContentType() == MSG_CONTENT_TYPE.IMAGE){
			
			holder.tv_lastMsg.setTextColor(0xff47b8ff);
			
			if(state == MSG_STATE.SENDDING)
				
				holder.tv_lastMsg.setText("正在发送:[图片消息]");
			
			else if(state == MSG_STATE.RECEIVEING)
				
				holder.tv_lastMsg.setText("正在接收:[图片消息]");
			
			else
				
				holder.tv_lastMsg.setText("[图片消息]");
			
		}else
			
			holder.tv_lastMsg.setText(mUsrmessage.getLastMsg().getContent());
		
		if(deration == MSG_DERATION.SEND){
			String text = "";
			if(state == MSG_STATE.SENDDING){
				holder.tv_msgState.setBackgroundResource(R.drawable.bg_message_status_sendding);
				text = "发送中";
			}else if(state == MSG_STATE.ARRIVED){
				holder.tv_msgState.setBackgroundResource(R.drawable.bg_message_status_sended);
				text = "送达";
			}else{
				holder.tv_msgState.setBackgroundResource(R.drawable.bg_message_status_readed);
				text = "已读"; 
			}	
			holder.tv_msgState.setText(text);
			holder.tv_msgState.setPadding(10, 5, 10, 5);
			
		}else{
			
			if(mUsrmessage.getMsgNotReadCount()> 0){
				
				holder.tv_msgState.setTextColor(0xffffffff);
				holder.tv_msgState.setBackgroundResource(R.drawable.chatmsg_not_read_background);
				holder.tv_msgState.setText(mUsrmessage.getMsgNotReadCount()+"");
				
			}else{
				
				holder.tv_msgState.setBackgroundColor(0x00000000);;
				holder.tv_msgState.setText("");
				
			}
			
		}
		
		
		return view;
	}
	
	class ViewHolder{
		
		 ImageView iv_icon;
		 
		 TextView tv_catalog;
		 
		 TextView tv_niName;
		
		 TextView tv_lastChatTime;
		
		 EmotionTextView tv_lastMsg;
		
		 TextView tv_msgState;
		
		
	}

	protected void initImageFetcher() {
		
		imageFetcher = new ImageFetcher(context, 80 ,80);
		
		imageFetcher.setImageCache(CacheUtils.getImageCache(context, "imageCache/"));
		
		imageFetcher.setLoadingImage(R.drawable.people_icon_selector);
		
		imageFetcher.setImageFadeIn(true);
		
	}

	@Override
	public int getCount() {
		return chattingPeoples.size();
	}

	@Override
	public Object getItem(int position) {
		return chattingPeoples.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	
}
