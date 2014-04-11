package com.whf.pilin.engien;

import java.io.FileNotFoundException;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Handler;
import android.util.Log;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.connection.XmppFriendManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.ChattingPeopleDAO;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.utils.TypeConverter;

public class MReceiveChatListener implements ChatManagerListener{

	XmppFriendManager xManager;

	MessageDAO messageDAO;
	
	ChattingPeopleDAO cPeopleDAO;
	
	String hostUid;
	
	public MReceiveChatListener() {

		this.xManager = XmppFriendManager.getInstance();

		this.hostUid = MXmppConnManager.hostUid;
		
		messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
		
		cPeopleDAO = (ChattingPeopleDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_CHATTING);
		
	}
	
	@Override
	public void chatCreated(Chat chat, boolean isExist){
		if(chat.getListeners().isEmpty()){
			chat.addMessageListener(new MsgProcessListener());
		}
		
	}
	
	public class MsgProcessListener implements MessageListener{
			
			@Override
			public void processMessage(Chat chat, Message msg) {
				
				//与每个用户的会话只应该有一个消息监听器
				if(chat.getListeners().toArray().length>1){
					
					chat.removeMessageListener(this);
					
					return;
					
				}
				
				String uid = msg.getFrom().split("/")[0];
				
				CommonMessage mMsg = null;
				
				long rowid = 0;
				
				//try {
					
					try {
						mMsg = new CommonMessage(uid.trim(), xManager
								.getUserIconAvatar(uid),
								System.currentTimeMillis(), "0.12km",
								msg.getBody(), MSG_STATE.ARRIVED,
								MSG_CONTENT_TYPE.TEXT, MSG_DERATION.RECEIVE,
								TypeConverter.nullStringDefaultValue(
										PiLinApplication.friendsNames.get(uid.trim()), uid.split("@")[0]));
					} catch (FileNotFoundException | XMPPException e) {
						e.printStackTrace();
					}
					
					///messags.add(mMsg); 
					
				rowid = messageDAO.save(mMsg,hostUid);

				if(!PiLinApplication.mJIDChats.containsKey(uid)){
					
					PiLinApplication.mJIDChats.put(uid, chat);
					
				}
				//刷新消息列表
				if(!cPeopleDAO.peopleChatting(uid,hostUid)){
					
					android.os.Message om = new android.os.Message();
					
					om.what = CustomConst.HANDLER_CHATPEOPLE_LIST_ADD;
					
					om.obj = uid;
					
					PiLinApplication.getHandlers("MsgFragment").get(0).sendMessage(om);
					
				}
				
				handRefreshSession(uid);
				
				//聊天对话框内刷新
				List<Handler> handlers = PiLinApplication.getHandlers(uid);
				
				for(Handler hand : handlers){
					
					Log.i("MReceiveChatListener", hand.getClass().toString().split("$")[0]);
					
					if(hand.getClass().toString().contains("ChatActivity")){
						
						handChatActivity(hand,rowid);
						
					}
					
					
				}
				
			}
			///
			public void handRefreshSession(String uid) {
				
				android.os.Message om = new android.os.Message();
				
				om.what = CustomConst.HANDLER_CHATPEOPLE_LIST_UPDATE;
				
				om.obj = uid;
				
				PiLinApplication.getHandlers("MsgFragment").get(0).sendMessage(om);
				
			}

			/**
			 * 刷新聊天窗口信息
			 * @param handler
			 * @param mMsg
			 */
			public void handChatActivity(Handler handler,long mMsg){
				
				android.os.Message osMsg = new android.os.Message();
				
				osMsg.what = 0;
				
				osMsg.obj = mMsg;
				
				handler.sendMessage(osMsg);
				
			}
	}
	
}
