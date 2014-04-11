package com.whf.pilin.connection;

import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;

import android.util.Log;

import com.whf.pilin.PiLinApplication;

public class MChatManager {
	
	private ChatManager chatManager;
	
	private Map<String, Object> mJIDChats;
	
	public MChatManager(ChatManager chatManager){
		
		this.chatManager = chatManager;
		
		this.mJIDChats = PiLinApplication.mJIDChats;
		
	}
	/**
	 * 创建一个会话
	 * @param threadId 会话线程ID
	 * @param jid 会话对象JID
	 * @param listener 会话消息监听器
	 * @return 返回获取的会话
	 */
	public Chat createChat(String threadId,String jid,MessageListener listener){
		
		if(threadId == null || chatManager.getThreadChat(threadId) == null){
			
			return chatManager.createChat(jid, jid, listener);
			
		}else{
			
			return chatManager.getThreadChat(threadId);
			
		}
		
	}
	
	/**
	 * 创建一个会话
	 * @param jid 会话对象JID
	 * @param listener 会话消息监听器
	 * @return 返回获取的会话
	 */
	public Chat createChat(String jid,MessageListener listener){
		
		if(mJIDChats.get(jid) == null){
			
			Chat chat = chatManager.createChat(jid, listener);
			
			PiLinApplication.mJIDChats.put(jid, chat);
			
			return chat;
			
		}else{
			
			listener = null;
			
			return (Chat)mJIDChats.get(jid);
			
		}
		
	}
	
	
}
