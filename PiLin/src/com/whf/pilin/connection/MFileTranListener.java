package com.whf.pilin.connection;

import java.io.File;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import android.os.Handler;
import android.util.Log;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.dao.ChattingPeopleDAO;
import com.whf.pilin.dao.MessageDAO;
import com.whf.pilin.engien.MsgEume.MSG_CONTENT_TYPE;
import com.whf.pilin.engien.MsgEume.MSG_DERATION;
import com.whf.pilin.engien.MsgEume.MSG_STATE;
import com.whf.pilin.entity.CommonMessage;
import com.whf.pilin.utils.FileUtils;
import com.whf.pilin.utils.TypeConverter;

/**
 * 文件接收类
 * @author WHF
 *
 */
public class MFileTranListener implements FileTransferListener {
	
	private String hostId;
	
	private MessageDAO messageDAO = (MessageDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_MESSAGE);
	
	private ChattingPeopleDAO cPeopleDAO = (ChattingPeopleDAO)PiLinApplication.getInstance().dabatases.get(CustomConst.DAO_CHATTING);
	
	public MFileTranListener(){
		
		this.hostId = MXmppConnManager.hostUid;
		
	}
	
	@Override
	public void fileTransferRequest(FileTransferRequest request) {
		
		final IncomingFileTransfer accept = request.accept();
		
		String uid = request.getRequestor().split("/")[0];
		
		String content = CacheUtils.getImagePath(PiLinApplication.getInstance(), "receiveImage/" + TypeConverter.getUUID() + ".pilin");
		
		File file = new File(content);
		
		if(FileUtils.mkdirs(content)){
		
			try {
				///创建聊天会话
				Chat chat = MXmppConnManager.getInstance().getChatManager().createChat(uid, MXmppConnManager.getInstance().getChatMListener().new MsgProcessListener());
				
				accept.recieveFile(file);
				
				long mills = System.currentTimeMillis();
				
				CommonMessage message = new CommonMessage(uid,
						"",
						mills,
						"0.12km",
						content,
						MSG_STATE.RECEIVEING,
						MSG_CONTENT_TYPE.IMAGE,
						MSG_DERATION.RECEIVE,
						TypeConverter.nullStringDefaultValue(
								PiLinApplication.friendsNames.get(uid.trim()), uid.split("@")[0]));
				
				long rowid = messageDAO.save(message, hostId);
				
				new updateStatusthread(mills,rowid, accept,uid).start();
				
				if(!PiLinApplication.mJIDChats.containsKey(uid)){
					
					PiLinApplication.mJIDChats.put(uid, chat);
					
				}
				//刷新消息列表
				if(!cPeopleDAO.peopleChatting(uid,hostId)){
					
					android.os.Message om = new android.os.Message();
					
					om.what = CustomConst.HANDLER_CHATPEOPLE_LIST_ADD;
					
					om.obj = uid;
					
					PiLinApplication.getHandlers("MsgFragment").get(0).sendMessage(om);
					
				}
				
				handRefreshSession(uid);
				
				refreshChatDialog(uid, rowid);
				
			} catch (XMPPException e) {
				
				e.printStackTrace();
				
			}
			
		}
	}
	/**
	 * 文件接收状态更新线程
	 * @author whf
	 *
	 */
	class updateStatusthread extends Thread{
		
		private long rowid;
		
		private IncomingFileTransfer accept;
		
		private String uid;
		
		private long mills;
		
		public updateStatusthread(long mills,long rowid,IncomingFileTransfer accept,String uid){
			this.rowid = rowid;
			this.accept = accept;
			this.uid = uid;
			this.mills = mills;
		}
		@Override
		public void run() {
			
			while(!accept.isDone()){
			}
			messageDAO.updateStateByRowid(rowid, hostId, 1);
			refreshChatImageMsg(uid, mills,CustomConst.HANDLER_MSG_FILE_SUCCESS);
			handRefreshSession(uid);
		}
		
	}
	
	///
	public static void handRefreshSession(String uid) {
		
		android.os.Message om = new android.os.Message();
		
		om.what = CustomConst.HANDLER_CHATPEOPLE_LIST_UPDATE;
		
		om.obj = uid;
		
		PiLinApplication.getHandlers("MsgFragment").get(0).sendMessage(om);
		
	}
	/**
	 * 刷新聊天窗口信息
	 * @param uid
	 */
	public void refreshChatDialog(String uid,long rowid){
		
		//聊天对话框内刷新
		List<Handler> handlers = PiLinApplication.getHandlers(uid);
		
		for(Handler hand : handlers){
			
			Log.i("MReceiveChatListener", hand.getClass().toString().split("$")[0]);
			
			if(hand.getClass().toString().contains("ChatActivity")){
				
				handChatActivity(hand,rowid);
				
			}
			
			
		}
		
	}
	
	public void refreshChatImageMsg(String uid,long mills,int what){
		
		// 聊天对话框内刷新
		List<Handler> handlers = PiLinApplication.getHandlers(uid);
		
		if(handlers == null) return;
		
		for (Handler hand : handlers) {

			Log.i("MReceiveChatListener",
					hand.getClass().toString().split("$")[0]);

			if (hand.getClass().toString().contains("ChatActivity")) {

				refreshImageMsg(hand, mills,what);

			}

		}
		
	}
	/**
	 * 更新图片消息状态
	 * @param handler
	 * @param mills
	 */
	public void refreshImageMsg(Handler handler,long mills,int what){
		
		android.os.Message osMsg = new android.os.Message();
		
		osMsg.what = what;
		
		osMsg.obj = mills;
		
		handler.sendMessage(osMsg);
		
	}
	
	/**
	 * 刷新聊天窗口信息
	 * @param handler
	 * @param mMsg
	 */
	public void handChatActivity(Handler handler,long mMsg){
		
		android.os.Message osMsg = new android.os.Message();
		
		osMsg.what = CustomConst.HANDLER_MGS_ADD;
		
		osMsg.obj = mMsg;
		
		handler.sendMessage(osMsg);
		
	}
	

}
