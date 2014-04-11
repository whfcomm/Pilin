package com.whf.pilin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 聊天界面ListView
 * @author WHF
 *
 */
public class CommonChatListView extends ListView {
	
	public CommonChatListView(Context context){
		super(context);
		init();
	}
	
	public CommonChatListView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public CommonChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	/**
	 * 使ListView是从下开始网上的
	 */
	private void init(){
		
		setStackFromBottom(true);
		//setFastScrollEnabled(true);
		
	}
}
