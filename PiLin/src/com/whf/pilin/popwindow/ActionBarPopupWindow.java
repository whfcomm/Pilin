package com.whf.pilin.popwindow;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.whf.pilin.R;
import com.whf.pilin.activitys.location.LocationMainActivity;
import com.whf.pilin.activitys.setting.SettingActvity;
import com.whf.pilin.adapter.setting.ActionMoreAdapter;

public class ActionBarPopupWindow extends PopupWindow implements OnItemClickListener{
	
	private Context context;
	
	public ActionBarPopupWindow (Context context){
		
		super(context);
		
		this.context = context;
		
		final View view  = LayoutInflater.from(context).inflate(R.layout.common_list_layout, null);
		
		ListView lv_action_more = (ListView)view.findViewById(R.id.lv_common_parent);
		
		lv_action_more.setOnItemClickListener(this);
		
		lv_action_more.setAdapter(new ActionMoreAdapter(context));
		
		this.setContentView(view);
		
		this.setFocusable(true);
		
		this.setHeight(LayoutParams.WRAP_CONTENT);
		
		this.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,context.getResources().getDisplayMetrics()));
		
		this.setAnimationStyle(android.R.style.Animation_Translucent);
		
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = view.findViewById(R.id.lv_common_parent).getTop();
				
				int y = (int)event.getY();
				
				if(event.getAction() == MotionEvent.ACTION_UP){
					
					if(y<height){
						
						dismiss();
						
					}
					
				}
				
				return true;
			}
		});
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long mills) {
		
		switch (position) {
		case 0:
			
			dismiss();
			Intent main = new Intent(context,LocationMainActivity.class);
			main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(main);
			
			break;

		case 1:
			break;
			
		case 2:
			dismiss();
			Intent intent = new Intent(context,SettingActvity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			break;
		}
		
	}
	
	
}
