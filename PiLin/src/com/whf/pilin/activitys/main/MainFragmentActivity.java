package com.whf.pilin.activitys.main;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.main.fragment.FriendsFragment;
import com.whf.pilin.activitys.main.fragment.MsgFragment;
import com.whf.pilin.activitys.main.fragment.NearFragment;
import com.whf.pilin.adapter.main.MainViewPagerAdapter;
import com.whf.pilin.adapter.main.chatting.ChattingMsgAdapter;
import com.whf.pilin.adapter.main.friends.FriendListAdapter;
import com.whf.pilin.adapter.main.near.NearFunctionAdapter;
import com.whf.pilin.bitmap.cache.ImageCache;
import com.whf.pilin.bitmap.cache.ImageCache.ImageCacheParams;
import com.whf.pilin.connection.XmppFriendManager;
import com.whf.pilin.daoService.ChattPeopleService;
import com.whf.pilin.entity.ChattingPeople;
import com.whf.pilin.entity.NearByPeople;
import com.whf.pilin.entity.NearFunction;
import com.whf.pilin.popwindow.ActionBarPopupWindow;


public class MainFragmentActivity extends ActionBarActivity implements ActionBar.TabListener{
	
	private MainViewPagerAdapter mSectionPagerAdapter;
	
	private ViewPager mViewPager;
	
	private ChattPeopleService chattPeopleService;
	
	private List<ChattingPeople> chattingPeoples;
	
	private List<NearByPeople> friends;
	
	private Fragment [] fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setBackgroundDrawable(new ColorDrawable(0xff47b8ff));
		
		///初始化数据库
		if(PiLinApplication.xmppConnection != null){
			PiLinApplication.getInstance().dbHelper.CreateSelfTable(PiLinApplication.xmppConnection.getUser().split("@")[0]);
		}
		chattPeopleService = new ChattPeopleService();
		mViewPager = (ViewPager)findViewById(R.id.pager);
		
		List<NearFunction> nearFunctions = new ArrayList<NearFunction>();
		nearFunctions.add(new NearFunction(R.drawable.ic_discover_group, "附近用户", ""));
		nearFunctions.add(new NearFunction(R.drawable.ic_discover_feed, "附近留言板", "小菜发表了留言:[zem1]..."));
		
		XmppFriendManager xManager = XmppFriendManager.getInstance();
		friends = xManager.getFriends();
		resumeAction();
		fragments = new Fragment [] {new NearFragment(new NearFunctionAdapter(this, nearFunctions)),
												 new MsgFragment(new ChattingMsgAdapter(this,chattingPeoples)),
											     new FriendsFragment(new FriendListAdapter(this, friends), 2)};
		
		mSectionPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),
														R.array.main_tab_names,
														getResources(),fragments);
		
		initViews(actionBar);
		
		PiLinApplication.getInstance().addActivity(this);
	}
	
	public Fragment [] getFragments(){
		
		return fragments;
		
	}
	
	private void initViews(final ActionBar actionBar){
		
		mViewPager.setAdapter(mSectionPagerAdapter);
		
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				actionBar.setSelectedNavigationItem(position);
				
			}
		});
		
		
		for(int i =0;i<mSectionPagerAdapter.getCount();i++){
				actionBar.addTab(actionBar.newTab().setText(mSectionPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menuMore = menu.findItem(R.id.menu_more);
		menuMore.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		ActionBarPopupWindow popupWindow = new ActionBarPopupWindow(getApplicationContext());
		
		popupWindow.showAsDropDown(this.findViewById(R.id.menu_more));
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		
		resumeAction();
		
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK){
			
			moveTaskToBack(true);
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private List<String>  getUids(){
		
		List<String> uids = new ArrayList<String>();
		
		for(NearByPeople cByPeople : friends){
			
			uids.add(cByPeople.getUid());
			
		}
		
		return uids;
		
	}
	
	private void resumeAction(){
		
		List<String> uids = getUids();
		
		chattingPeoples = chattPeopleService.findAll(uids,PiLinApplication.xmppConnection.getUser());
		
	}
	
	public List<ChattingPeople> getChattingPeople(){
		
		return chattingPeoples;
		
	}
	
}
