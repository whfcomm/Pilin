package com.whf.pilin.activitys.main.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.activitys.chat.activity.ChatActivity;
import com.whf.pilin.activitys.main.fragment.FriendsSiderView.OnTouchLetterChangedListener;
import com.whf.pilin.adapter.main.friends.FriendListAdapter;
import com.whf.pilin.entity.NearByPeople;

@SuppressLint("ValidFragment")
public class FriendsFragment extends Fragment implements OnTouchLetterChangedListener,OnItemClickListener{
	
	private FriendListAdapter adapter;
	
	private static final String ARG_SECTION_NUMBER = "section_number";
	
	private ListView lv_friends;
	
	private FriendsSiderView siderView;
	
	private TextView tv_overlay;
	
	private OverLayThread overLayThread = new OverLayThread();
	
	public FriendsFragment(){}
	
	public FriendsFragment(FriendListAdapter adapter,int sectionNumber){
		
		this.adapter = adapter;
		
		Bundle bundle = new Bundle();
		
		bundle.putInt(ARG_SECTION_NUMBER, sectionNumber);
		
		this.setArguments(bundle);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_friends_list_layout, null);
		
		lv_friends = (ListView)view.findViewById(R.id.lv_friends);
		
		siderView = (FriendsSiderView)view.findViewById(R.id.fsv_sidebar);
		
		tv_overlay = (TextView)view.findViewById(R.id.tv_letter);
		
		lv_friends.setTextFilterEnabled(true);
		
		tv_overlay.setVisibility(View.INVISIBLE);
		
		lv_friends.setAdapter(adapter);
		
		lv_friends.setOnItemClickListener(this);
		
		siderView.setOnTouchLetterChangedListener(this);
		
		return view;
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	
	public Handler getHandler(){
		
		return handler;
		
	}
	
	Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == 0){
				
				int type = msg.arg1;
				
				String uid = (String)msg.obj;
				
				for(NearByPeople people : adapter.getFriends()){
					
					if(people.getUid().equals(uid)){
						
						people.setState(type);
						
						break;
						
					}
					
				}
				adapter.notifyDataSetChanged();
			}
			
		};
		
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

	@Override
	public void onItemClick(AdapterView<?> parent, View convertView, int position, long mills) {
		
		NearByPeople userInfo = adapter.getFriends().get(position);
		
		Bundle bundle = new Bundle();
		
		bundle.putSerializable("user", userInfo);
		
		Intent intent = new Intent(getActivity(),ChatActivity.class);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		
	}
}
