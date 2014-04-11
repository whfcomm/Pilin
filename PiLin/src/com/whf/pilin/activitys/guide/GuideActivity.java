package com.whf.pilin.activitys.guide;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whf.pilin.R;
import com.whf.pilin.activitys.splash.SplashActivity;
import com.whf.pilin.adapter.guide.GuideAdapter;


public class GuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	
private int currentIndex;
	
	private ImageView [] points;
	
	private GuideAdapter vApdapter;
	
	private List<View> views;
	
	private ViewPager viewPager;
	
	private Button btnStart;
	
	private SharedPreferences sf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.guide_layout);
		
		initView();
		
		initData();
		
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		
		views = new ArrayList<View>();
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		vApdapter = new GuideAdapter(views);
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		views.add(LinearLayout.inflate(this, R.layout.guide1, null));
		views.add(LinearLayout.inflate(this, R.layout.guide2, null));
		views.add(LinearLayout.inflate(this, R.layout.guide3, null));
		views.add(LinearLayout.inflate(this, R.layout.guide4, null));
		views.add(LinearLayout.inflate(this, R.layout.guide_welcome, null));
		
		viewPager.setAdapter(vApdapter);
		
		viewPager.setOnPageChangeListener(this);
		
		btnStart = (Button)views.get(views.size()-1).findViewById(R.id.btn_start_app);
		
		btnStart.setOnClickListener(this);
		
		initPoint();
	}

	private void initPoint() {
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
		
		points = new ImageView[views.size()];
		
		for(int i=0;i<points.length;i++){
			
			points[i] = (ImageView) ll.getChildAt(i);
			
			points[i].setEnabled(true);
			
			points[i].setOnClickListener(this);
			
			points[i].setTag(i);
			
		}
		
		currentIndex = 0;
		
		points[currentIndex].setEnabled(false);
	}

	private void setCurView(int position){
		
		if(position < 0 || position >= points.length){
			return ;
		}
		
		viewPager.setCurrentItem(position);
		
	}
	
	private void setCurDot(int position){
		
		if(position < 0 || position >= points.length || currentIndex == position){
			
			return;
			
		}
		
		points[position].setEnabled(false);
		
		points[currentIndex].setEnabled(true);
		
		currentIndex = position;
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		setCurDot(position);
	}

	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.btn_start_app){
					
				sf = getSharedPreferences("config", MODE_PRIVATE);
	
				Editor editor = sf.edit();
	
				editor.putBoolean("guide", true);
	
				editor.commit();
			
				Intent intent = new Intent(this, SplashActivity.class);
				
				finish();
				
				startActivity(intent);
					
		}else{
				
				int position =(Integer) v.getTag();
				
				setCurView(position);
				
				setCurDot(position);
				
		}
		
	}

}
