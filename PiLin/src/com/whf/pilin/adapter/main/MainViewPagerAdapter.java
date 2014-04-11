package com.whf.pilin.adapter.main;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
	
	private int titleArrays;
	
	private Resources resources;
	
	private Fragment [] fragments;
	
	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public MainViewPagerAdapter(FragmentManager fm,int titleArrays,Resources resources,Fragment [] fragments) {
		super(fm);
		this.titleArrays = titleArrays;
		this.resources = resources;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return  fragments[position];
				//CommonPlaceholderFragment.newInstance(position + 1,adapters[position],types[position]); 
				//fragments.get(position);
				
	}
	
	
	
	@Override
	public int getCount() {
		return resources.getStringArray(titleArrays).length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return resources.getStringArray(titleArrays)[position];
		
	}
	
}
