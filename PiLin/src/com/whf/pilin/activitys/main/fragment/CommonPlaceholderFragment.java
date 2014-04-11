package com.whf.pilin.activitys.main.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.activitys.main.fragment.FriendsSiderView.OnTouchLetterChangedListener;
import com.whf.pilin.adapter.main.friends.FriendListAdapter;

@SuppressLint("ValidFragment")
public class CommonPlaceholderFragment extends Fragment implements OnTouchLetterChangedListener{
	
	private FriendListAdapter adapter;
	
	private String type;
	
	private ListView lv_friends;
	
	private FriendsSiderView siderView;
	
	private TextView tv_overlay;
	
	private OverLayThread overLayThread = new OverLayThread();
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	/**
	 * 创建本类的一个实例，并设置标识
	 * @param sectionNumber
	 * @return
	 */
	public static Fragment newInstance(int sectionNumber,FriendListAdapter adapter,String type){
		
		CommonPlaceholderFragment fragment = new CommonPlaceholderFragment();
		
		fragment.adapter = adapter;
		
		fragment.type = type;
		
		Bundle args = new Bundle();
		
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		
		fragment.setArguments(args);
		
		return fragment;
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if("friends".equals(type)){
			
			View view = inflater.inflate(R.layout.main_friends_list_layout, null);
			
			lv_friends = (ListView)view.findViewById(R.id.lv_friends);
			
			siderView = (FriendsSiderView)view.findViewById(R.id.fsv_sidebar);
			
			tv_overlay = (TextView)view.findViewById(R.id.tv_letter);
			
			lv_friends.setTextFilterEnabled(true);
			
			tv_overlay.setVisibility(View.INVISIBLE);
			
			lv_friends.setAdapter(adapter);
			
			siderView.setOnTouchLetterChangedListener(this);
			
			return view;
			
		}
		
		return null;
		
	}


	private Handler handler = new Handler(){
	};
	
	private class OverLayThread implements Runnable{
		
		@Override
		public void run() {
			
			tv_overlay.setVisibility(View.GONE);
			
		}
		
	}
	
	@Override
	public void onTouchLetterChanged(String letter) {
		
		tv_overlay.setText(letter);
		
		tv_overlay.setVisibility(View.VISIBLE);
		
		handler.removeCallbacks(overLayThread);
		
		handler.postDelayed(overLayThread, 1000);
		
		if(alphaIndexer(letter) > 0){
			
			int position = alphaIndexer(letter);
			
			lv_friends.setSelection(position);
		}
		
	}
	
	public int alphaIndexer(String s){
		
		int position = 0;
		
		for(int i = 0;i<adapter.getFriends().size();i++){
			
			if(adapter.getFriends().get(i).getPy().startsWith(s)){
				
				position = i;
				break;
				
			}
			
		}
		
		return position;
		
	}
}
