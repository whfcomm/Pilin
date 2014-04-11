package com.whf.pilin.connection;

import org.jivesoftware.smack.ConnectionListener;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.utils.ToastUtils;

public class ReConnectionListener implements ConnectionListener {
	
	private Context context;
	
	public ReConnectionListener(Context context){
		this.context  = context;
	}
	
	@Override
	public void connectionClosed() {
		//Looper.prepare();
		ToastUtils.createCenterNormalToast(context, "连接断开。。。", Toast.LENGTH_LONG);
		//Looper.loop();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		Looper.prepare();
		if(e.getMessage().equals("stream:error (conflict)")){
			ToastUtils.createCenterNormalToast(context, "账号在别处登录", Toast.LENGTH_SHORT);
			PiLinApplication.xmppConnection.disconnect();
			MXmppConnManager.getInstance().closeConnection();
			PiLinApplication.getInstance().exit();
			System.exit(0);
		}
		Looper.loop();
		
	}

	@Override
	public void reconnectionFailed(Exception e) {
		Looper.prepare();
		ToastUtils.createCenterNormalToast(context, "连接断开。。。" + e.getMessage(), Toast.LENGTH_LONG);
		Looper.loop();
		
	}

	@Override
	public void reconnectionSuccessful() {
		Looper.prepare();
		ToastUtils.createCenterNormalToast(context, "重新连接成功。。。", Toast.LENGTH_LONG);
		Looper.loop();
	}
	@Override
	public void reconnectingIn(int arg0) {
		
	}
	
}
