package com.whf.pilin.activitys.chat.fragments;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.bitmap.cache.ImageResizer;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;

public class ChatImageMessage extends ChatCommonMessage implements OnClickListener,OnLongClickListener{
	
private ImageView mIvImage;
	
	private FrameLayout mLayoutLoading;
	
	private ImageView mIvLoading;
	
	private TextView mHtvLoadingText;
	
	private AnimationDrawable mAnimation;
	
	private ImageView mIvDefault;
	
	private int mProcess;
	
	private Bitmap mBitmap;
	
	public ChatImageMessage(CommonMessage message, Context context,ImageFetcher imageFetcher) {
		super(message, context,imageFetcher);
	}
	
	protected void initImageFetcher() {
		
		mImageFetcher = new ImageFetcher(mContext, 40 ,40);
		
		mImageFetcher.setImageCache(CacheUtils.getImageCache(mContext, "imageCache/"));
		
		mImageFetcher.setLoadingImage(R.drawable.people_icon_selector);
		
		mImageFetcher.setImageFadeIn(true);
		
	}
	
	@Override
	protected void onInitViews() {
		
		View view = mInflater.inflate(R.layout.message_image, null);
		
		mLayoutMessageContainer.addView(view);
		
		mIvImage = (ImageView)view.findViewById(R.id.message_iv_msgimage);
		
		mLayoutLoading = (FrameLayout)view.findViewById(R.id.message_layout_loading);
		
		mIvDefault = (ImageView)view.findViewById(R.id.message_iv_def_pic);
		
		mIvLoading = (ImageView)view.findViewById(R.id.message_iv_loading);
		
		mHtvLoadingText = (TextView)view.findViewById(R.id.message_htv_loading_text);
		
		mIvImage.setOnClickListener(this);
		
		mIvImage.setOnLongClickListener(this);
		
	}
	
	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			super.handleMessage(msg);
			
			switch (msg.what) {
			
			case 0:
				
				startLoadingAnimation();
				
				break;

			case 1:
				
				updateLoadingProcess();
				
				break;
				
			case 2:
				
				stopLoadingAnimation();
				
				break;
			}
			
		};
		
	};
	
	private void startLoadingAnimation(){
		
		mAnimation = new AnimationDrawable();
		
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_01), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_02), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_03), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_04), 300);
		
		mAnimation.setOneShot(false);
		mIvImage.setVisibility(View.VISIBLE);
		mLayoutLoading.setVisibility(View.VISIBLE);
		mIvLoading.setVisibility(View.VISIBLE);
		//mHtvLoadingText.setVisibility(View.VISIBLE);
		if(mMsg.getMsgComeFromType() == MSG_DERATION.RECEIVE) mIvDefault.setVisibility(View.VISIBLE);
		mIvLoading.setImageDrawable(mAnimation);
		mIvImage.setImageBitmap(mBitmap);
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				
				mAnimation.start();
				
			}
		});
		
		//mHandler.sendEmptyMessage(1);
		
	}
	
	private void stopLoadingAnimation(){
		
		if(mAnimation != null){
			
			mAnimation.stop();
			
			mAnimation = null;
			
		}
		mLayoutLoading.setVisibility(View.GONE);
		mHtvLoadingText.setVisibility(View.GONE);
		mIvImage.setVisibility(View.VISIBLE);
		if(mBitmap != null){
			mIvImage.setImageBitmap(mBitmap);
		}
	}
	
	private void updateLoadingProcess(){
		
		if(mProcess < 100){
			mProcess+=5;
			mHtvLoadingText.setText(mProcess + "%");
			mHandler.sendEmptyMessageDelayed(1, 1);
		}else{
			mProcess = 0;
			mHandler.sendEmptyMessage(2);
		}
		
	}
	
	@Override
	protected synchronized void  onFillMessage() {
		
		SoftReference<Bitmap> softReference = PiLinApplication.getInstance().mPhotoOriginalCache.get(mMsg.getContent());
		if(softReference == null){
			putBitmap();
		}else{
			mBitmap = softReference.get();
			if(mBitmap == null){
				putBitmap();
			}
		}
		
		if(mMsg.getState() == MSG_STATE.RECEIVEING || mMsg.getState() == MSG_STATE.SENDDING){
			mHandler.sendEmptyMessage(0);
		}else{
			mHandler.sendEmptyMessage(2);
		}
		
	}
	
	public void putBitmap(){
		mBitmap = ImageResizer.decodeSampledBitmapFromFile(mMsg.getContent(),100,200);
		PiLinApplication.getInstance().mPhotoOriginalCache.put(mMsg.getContent(), new SoftReference<Bitmap>(mBitmap));
		
	}
	
	@Override
	public boolean onLongClick(View arg0) {
		return false;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	@SuppressWarnings("deprecation")
	private Drawable getDrawable(int resid){
		
		return new BitmapDrawable(BitmapFactory.
				decodeResource(mContext.getResources(), resid));
		
	}
	
	
}
