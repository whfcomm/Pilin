package com.whf.pilin.activitys.chat.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.activitys.BaseActivity;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.view.CommonChatListView;
import com.whf.pilin.view.EmotionEditText;

public abstract class BaseChatActivity extends BaseActivity implements 
		OnClickListener,OnTouchListener,OnItemClickListener{
	/**
	 * 保存窗口内的信息
	 */
	protected List<CommonMessage> messages = new ArrayList<CommonMessage>();
	/**
	 * 发送信息按钮
	 */
	protected Button mBtnMsgSend;
	/**
	 * 编辑信息框
	 */
	protected EmotionEditText mEmtMsg;
	/**
	 * 开启表情选择框按钮
	 */
	protected ImageView mIvEmotion;
	/**
	 * 开启多媒体框按钮
	 */
	protected ImageView mIvMedia;
	/**
	 * 消息列表ListView
	 */
	protected CommonChatListView mLvCommonMsg;
	/**
	 * 表情列表
	 */
	protected GridView mGvEmotion; 
	/**
	 * 聊天信息适配器
	 */
	protected ChatAdapter mAdapter;
	/**
	 * 添加图片ImageView
	 */
	protected ImageView mIvPlusBarPic;
	/**
	 * 拍照IV
	 */
	protected ImageView mIvPlusBarCarema;
	/**
	 * 位置IV
	 */
	protected ImageView mIvPlusBarLocation;
	/**
	 * 表情多媒体贞布局
	 */
	protected FrameLayout mLayoutEmotionMedia;
	/**
	 * 表情布局
	 */
	protected LinearLayout mLayoutEmotion;
	/**
	 * 多媒体布局
	 */
	protected LinearLayout mLayoutMedia;
	/**
	 * 包含添加“图片”的布局
	 */
	protected View mLoPlusBarPic;
	/**
	 * 包含打开"拍照"的布局
	 */
	protected View mLoPlusBarCarema;
	/**
	 * 包含打开"定位"的布局
	 */
	protected View mLoPlusBarLocation;
	/**
	 * "图片"的iv
	 */
	protected TextView mTvPlusBarPic;
	/**
	 * "拍照"的iv
	 */
	protected TextView mTvPlusBarCarema;
	/**
	 * "定位"的iv
	 */
	protected TextView mTvPlusBarLocation;
	/**
	 * 输入框控制器
	 */
	protected InputMethodManager mInputManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main_layout);
	}

}
