package com.whf.pilin.connection;

import java.io.File;
import java.util.Collection;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.engien.MReceiveChatListener;

public class MXmppConnManager {
	
	private XMPPConnection connection;
	
	private static MXmppConnManager  instance; 
	
	private ConnectionConfiguration config;
	
	private AccountManager accountManager;
	
	private ChatManager chatManager;
	
	private OfflineMessageManager offlineMessageManager;
	
	private ConnectionListener connectionListener;
	
	private MReceiveChatListener chatManagerListener;
	
	public  FileTransferManager transferManager;
	
	public static String hostUid;
	
	public FileTransferListener fileTranListener ;
	
	private static String IP = "218.244.150.50";
	
	private static int PORT = 5222;
	
	private static String HOST = "AY140326090233737f4cZ";
	
	private  MXmppConnManager(){
		
		instance = this;
		
	}
	/**
	 * 获取离线信息管理者
	 * @return
	 */
	public OfflineMessageManager getOffMsgManager(){
		return offlineMessageManager;
	}
	/**
	 * 获取会话管理者
	 * @return
	 */
	public ChatManager getChatManager(){
		
		return chatManager;
		
	}
	/**
	 * 获取账户管理者
	 * @return
	 */
	public AccountManager getAccountManager(){
		return accountManager;
	}
	/**
	 * 获取连接配置
	 * @return
	 */
	public ConnectionConfiguration getConnectionConfig(){
		
		return config;
		
	}
	/**
	 * 获取连接状态监听器
	 * @return
	 */
	public ConnectionListener getConnListener(){
		
		return connectionListener;
		
	}
	/**
	 * 获取会话监听器
	 * @return
	 */
	public MReceiveChatListener getChatMListener(){
		return chatManagerListener;
	}
	
	
	
	/**
	 * 关闭连接
	 */
	public void closeConnection(){
		
		if(connection!=null){
			
			if(connection.isConnected()){
				connection.removeConnectionListener(connectionListener);
				connection.disconnect();
			}
			connection = null;
		}	
		
	}
	
	/**
	 * 获取一个连接
	 * @return
	 * @throws XMPPException 
	 */
	public XMPPConnection getConnection() throws XMPPException{
		
		if(connection == null || !connection.isConnected()){
			
			closeConnection();
			
			
			connection = new XMPPConnection(config);
			config = new ConnectionConfiguration(IP, PORT,
					HOST);
			config.setSASLAuthenticationEnabled(false);
			config.setReconnectionAllowed(true);
			config.setSendPresence(false);
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
			connection = new XMPPConnection(config);
			connection.connect();
			accountManager = connection.getAccountManager();
			chatManager = connection.getChatManager();
			transferManager = new FileTransferManager(connection);
			connectionListener = new ReConnectionListener(PiLinApplication.getInstance());
			connection.addConnectionListener(connectionListener);
		}
		
		return connection;
		
	}
	/**
	 * 获取一个单例对象
	 * @return
	 */
	public static MXmppConnManager getInstance(){
		
		if(instance == null){
			
			instance = new MXmppConnManager();
			
		}
		return instance;
	}
	/**
	 * 登录
	 * @param name
	 * @param pwd
	 * @param context
	 * @return
	 */
	public boolean mXmppLogin(String name,String pwd,Context context,Handler handler){
		
		if(connection==null || !connection.isConnected()){
			
			new InitXmppConnectionTask(handler).execute();
			
			return false;
		}
		
		try {
			
			connection.login(name, pwd);
			
			if(connection.getUser() == null){
				
				return false;
				
			}
			offlineMessageManager = new OfflineMessageManager(connection);
			
			hostUid = connection.getUser();
			
			fileTranListener = new MFileTranListener();
			
			transferManager.addFileTransferListener(fileTranListener);
			
			startChatLinstener();
			
			OfflineMessageSendBrocast.sendBrocast(context, OfflineMessageSendBrocast.getOfflineMegs());
			
			return true;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			if(e.getMessage().equals("not-authorized(401)")){
				
				handler.sendEmptyMessage(CustomConst.XMPP_ERROR_LOGINFAIL);
				
			}
			return false;
			
		}
		
	}
	/**
	 * 设置发送状态
	 * @param code
	 */
	public void setPresence(int code){
		
		if(connection == null){
			
			return;
			
		}
		Presence presence = null;
		
		switch (code) {
			//设置在线
			case CustomConst.USER_STATE_ONLINE:
				
				presence = new Presence(Presence.Type.available);
				
				break;
			//Q我吧
			case CustomConst.USER_STATE_Q_ME:
				
				presence = new Presence(Presence.Type.available);
				
				presence.setMode(Presence.Mode.chat);
				
				break;
			//忙碌
			case CustomConst.USER_STATE_BUSY:
				
				presence = new Presence(Presence.Type.available);
				
				presence.setMode(Presence.Mode.dnd);
				
				break;
			//离开	
			case CustomConst.USER_STATE_AWAY:
				
				presence = new Presence(Presence.Type.available);
				
				presence.setMode(Presence.Mode.away);
				
				break;
			//	
			case CustomConst.USER_STATE_SETSELFOFFLINE:
				
				Roster roster = connection.getRoster();
				
				Collection<RosterEntry> entries = roster.getEntries();
				
				for(RosterEntry entry : entries){
					
					presence = new Presence(Presence.Type.unavailable);
					
					presence.setPacketID(Packet.ID_NOT_AVAILABLE);
					
					presence.setFrom(entry.getUser());
					
					presence.setTo(entry.getUser());
					
					connection.sendPacket(presence);
				}
				// 向同一用户的其他客户端发送隐身状态  
				presence = new Presence(Presence.Type.unavailable);
				
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				
				presence.setFrom(connection.getUser());
				
				presence.setTo(StringUtils.parseBareAddress(connection.getUser()));
				
				break;
				
			case CustomConst.USER_STATE_OFFLINE:
				
				presence = new Presence(Presence.Type.unavailable);
				
				break;
				
			}
			connection.sendPacket(presence);
		
	}
	
	/**
	 * 开始会话监听
	 * @param context
	 */
	public void startChatLinstener(){
		
		if(MXmppConnManager.getInstance().getChatManager().getChatListeners().isEmpty()){
			 
			chatManagerListener = new MReceiveChatListener();
			
			MXmppConnManager.getInstance().getChatManager().addChatListener(chatManagerListener);
			
		}
		
	}
	/**
	 * 停止会话监听
	 */
	public void stopChatListener(){
		
		chatManager.removeChatListener(chatManagerListener);
		
	}
	/**
	 * 发送文件
	 * @param file 文件路径
	 * @param handler 文件发送实时情况反馈
	 */
	public synchronized void sendFile(final long mills,final long rowid,String type,String file,final Handler handler,final String toUserId){
		
		final MessageDAO messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
		
		Presence presence = connection.getRoster().getPresence(toUserId);
		
		final OutgoingFileTransfer transfer = transferManager.createOutgoingFileTransfer(presence.getFrom());
		
		try {
			
			transfer.sendFile(new File(file), type);
			
			new Thread(){
				
				public void run() {
					
					while(!transfer.isDone()){
						
						while(!transfer.isDone()){
						}
						messageDAO.updateStateByRowid(rowid, hostUid, 1);
						Message msg = handler.obtainMessage(CustomConst.HANDLER_MSG_FILE_SUCCESS, mills);
						handler.sendMessage(msg);
						MFileTranListener.handRefreshSession(toUserId);
					}
					
				};
				
			}.start();

		} catch (XMPPException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	/**
	 * 异步初始化连接任务
	 * @author WHF
	 *
	 */
	public class InitXmppConnectionTask extends AsyncTask<String, Void, Boolean>{
		
		private Handler handler;
		
		public InitXmppConnectionTask(Handler handler){
			
			this.handler = handler;
			
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			try {
				
				connection = null;
				
				PiLinApplication.xmppConnection = getInstance().getConnection();
						//initConnection();
			} catch (XMPPException e) {
				
				e.printStackTrace();
				
				Message msg = new Message();
				
				if(e.getMessage().contains("XMPPError connection to")){
					
					msg.what = CustomConst.XMPP_HANDLER_ERROR;
					
					msg.arg1 = CustomConst.XMPP_ERROR_CONNETERROR;
					
				}else{
					
					msg.what = CustomConst.XMPP_HANDLER_ERROR;
					
					msg.arg1 = CustomConst.XMPP_ERROR_CONNETERROR;
					
				}
				
				handler.sendMessage(msg);
				
				return false;
			}
			
			if(connection != null
					&& accountManager != null
					&& chatManager != null){
				
				handler.sendEmptyMessage(CustomConst.XMPP_HANDLER_SUCCESS);
				
				return true;
				
			}
			
			
			
			return false;
		}
		
	}
	
}
