package com.whf.pilin.activitys.main.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.activitys.chat.activity.ChatActivity;
import com.whf.pilin.activitys.main.MainFragmentActivity;
import com.whf.pilin.adapter.main.chatting.ChattingMsgAdapter;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.ChattingPeopleDAO;
import com.whf.pilin.daoService.ChattPeopleService;
import com.whf.pilin.entity.ChattingPeople;
import com.whf.pilin.entity.NearByPeople;

@SuppressLint("ValidFragment")
public class MsgFragment extends ListFragment{
	
	private ChattingMsgAdapter adapter;
	
	private ChattPeopleService chattPeopleService;
	
	private ChattingPeopleDAO cPeopleDAO;
	
	private String hostUid = MXmppConnManager.hostUid;
	
	public MsgFragment(ChattingMsgAdapter adapter){
		cPeopleDAO = (ChattingPeopleDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_CHATTING);
		chattPeopleService = new ChattPeopleService();
		this.adapter = adapter;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PiLinApplication.putHandler("MsgFragment", handler);
		setListAdapter(adapter);
	}
	
	Handler handler = new Handler(){
			
			public void handleMessage(android.os.Message msg) {
				//取得这个消息表明在聊天窗口，更新聊天记录
				if (msg.what == CustomConst.HANDLER_CHATPEOPLE_LIST_UPDATE){
					
					String uid = (String)msg.obj;
					
					refreshSession(uid);
					
					adapter.notifyDataSetChanged();
					
				//取得这个消息表明原先没有这个会话，在消息列添加	
				}else if(msg.what == CustomConst.HANDLER_CHATPEOPLE_LIST_ADD){
					
					String uid = (String)msg.obj;
					//将该会话如果要打开聊天窗口，要获取的聊天记录页数
					PiLinApplication.mUsrChatMsgPage.put(uid, 1);
					
					cPeopleDAO.save(uid,hostUid);
						
					adapter.getChattingPeoples().add(chattPeopleService.findByUid(uid,hostUid));
						
					adapter.notifyDataSetChanged();
				}
				
			}
			
	};
	
	public void onResume() {
		super.onResume();
		//获取Activity对象 更新本个Fragment的列表，因为在Activity的onResume方法中从新获取了一遍聊天列表
		MainFragmentActivity activity =(MainFragmentActivity)getActivity();
		adapter.getChattingPeoples().clear();
		adapter.getChattingPeoples().addAll(activity.getChattingPeople());
		adapter.notifyDataSetChanged();
	};
	
	public void refreshSession(String uid){
		
		for(ChattingPeople people : adapter.getChattingPeoples()){
			
			if(people.getPeople().equals(uid)){
				
				adapter.getChattingPeoples().remove(people);
				
				people = chattPeopleService.findByUid(uid,hostUid);
				
				adapter.getChattingPeoples().add(0,people);
				
				break;
				
			}
			
		}
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		ChattingPeople people = adapter.getChattingPeoples().get(position);
		
		NearByPeople userInfo = new NearByPeople(people.getPeople(), people.getLastMsg().getAvatar(), 0, 0, "", 0, 0, 0, 0, 0, 0, people.getLastMsg().getName(), 0, 0, 0, 0, people.getLastMsg().getDistance(), "", "", 0);
		
		Bundle bundle = new Bundle();
		
		bundle.putSerializable("user", userInfo);
		
		Intent intent = new Intent(getActivity(),ChatActivity.class);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		
		super.onListItemClick(l, v, position, id);
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		PiLinApplication.removeHandler("MsgFragment",handler);
	}
}
