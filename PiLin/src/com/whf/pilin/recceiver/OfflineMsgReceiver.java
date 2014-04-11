package com.whf.pilin.recceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.whf.pilin.R;
import com.whf.pilin.activitys.chat.activity.ChatActivity;
import com.whf.pilin.entity.ChatMessage;

public class OfflineMsgReceiver extends BroadcastReceiver {
	
	private TaskStackBuilder builder ;
	
	private NotificationManager manager;
	
	private Notification notification;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		ChatMessage chatListUserInfo = (ChatMessage)intent.getBundleExtra("user").getSerializable("user");
		
		builder = TaskStackBuilder.create(context);
		
		builder.addParentStack(ChatActivity.class);
		
		Intent resultIntent = new Intent(context,ChatActivity.class);
		
		builder.addNextIntent(resultIntent);
		
		PendingIntent resultPendintent = builder.getPendingIntent(getResultCode(), PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.people_icon_selector)
						.setContentTitle(chatListUserInfo.getNiName())
						.setContentText(chatListUserInfo.getChatLastMsg())
						.setContentIntent(resultPendintent)
						.setDefaults(Notification.DEFAULT_ALL).build();
		
		manager.notify(chatListUserInfo.getNiName(),0, notification);
		
		
	}

}
