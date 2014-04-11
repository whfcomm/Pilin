package com.whf.pilin.activitys.chat.fragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.whf.pilin.R;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.view.EmotionTextView;

public class ChatTextMessage extends ChatCommonMessage implements OnLongClickListener{
	
	private EmotionTextView mEtvContent;
	
	public ChatTextMessage(CommonMessage mMsg,Context context,ImageFetcher imageFetcher){
		
		super(mMsg,context,imageFetcher);
		
	}

	@Override
	protected void onInitViews() {
		
		View view = mInflater.inflate(R.layout.message_text, null);
		
		mLayoutMessageContainer.addView(view);
		
		mEtvContent = (EmotionTextView)view.findViewById(R.id.message_etv_msgtext);
		
		mEtvContent.setText(mMsg.getContent());
		
		//mEtvContent.setOnClickListener(this);
		
		mLayoutMessageContainer.setOnLongClickListener(this);
		
	}

	@Override
	protected void onFillMessage() {
		
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	
	
}
