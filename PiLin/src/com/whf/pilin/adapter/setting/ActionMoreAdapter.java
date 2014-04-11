package com.whf.pilin.adapter.setting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.utils.BitMapUtils;

public class ActionMoreAdapter extends BaseAdapter {

	private int [] itemNames = {R.string.menu_option_log,R.string.menu_option_voice,R.string.menu_option_setting};
	
	private int [] itemDreawble = {R.drawable.app_panel_location_icon,R.drawable.app_panel_voice_icon,R.drawable.app_panel_setting_icon};
	
	private Context context;
	
	public ActionMoreAdapter(Context context){
		
		this.context = context;
		
	}
	
	@Override
	public int getCount() {
		return itemNames.length;
	}

	@Override
	public Object getItem(int position) {
		return itemNames[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		
		if(convertView==null){
			
			view = LayoutInflater.from(context).inflate(R.layout.action_more_list_item, null);
			
			convertView = view;
			
		}else{
			
			view = convertView;
			
		}
		
		ImageView iv = (ImageView)view.findViewById(R.id.iv_actionbar);
		
		TextView tv = (TextView)view.findViewById(R.id.tv_actionbar);
		
		///iv.setBackgroundResource(itemDreawble[position]);
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),itemDreawble[position]);
		
		bitmap = BitMapUtils.zoomImg(bitmap, 50, 50);
		
		BitmapDrawable  bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
		
		iv.setBackgroundDrawable(bitmapDrawable);
		
		tv.setText(itemNames[position]);
		
		return view;
	}

}
