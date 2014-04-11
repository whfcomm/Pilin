package com.whf.pilin.activitys.chat.fragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.entity.CommonMessage;

public class ChatVoiceMessage extends ChatCommonMessage implements OnClickListener,OnLongClickListener{

	public ChatVoiceMessage(CommonMessage message, Context context,ImageFetcher imageFetcher) {
		super(message, context, imageFetcher);
	}

	@Override
	public boolean onLongClick(View arg0) {
		return false;
	}

	@Override
	public void onClick(View arg0) {
		
	}

	@Override
	protected void onInitViews() {
		
	}

	@Override
	protected void onFillMessage() {
		
	}

	

}
