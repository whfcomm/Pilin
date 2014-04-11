package com.whf.pilin.activitys.main.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
/**
 * 毗邻界面
 * @author WHF
 *
 */
@SuppressLint("ValidFragment")
public class NearFragment extends ListFragment {
	
	private BaseAdapter adapter;
	
	public  NearFragment(BaseAdapter adapter){
		
		this.adapter = adapter;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setListAdapter(adapter);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
}
