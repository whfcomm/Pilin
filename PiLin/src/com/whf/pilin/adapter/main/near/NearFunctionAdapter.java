package com.whf.pilin.adapter.main.near;

import java.util.List;

import com.whf.pilin.R;
import com.whf.pilin.entity.NearFunction;
import com.whf.pilin.view.EmotionTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NearFunctionAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<NearFunction> nearFunctions;
	
	public NearFunctionAdapter(Context context,List<NearFunction> nearFunctions){
		this.context = context;
		this.nearFunctions = nearFunctions;
	}
	
	@Override
	public int getCount() {
		return nearFunctions.size();
	}

	@Override
	public Object getItem(int position) {
		return nearFunctions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		ViewHolder holder = null;
		
		if(view == null){
			
			view = LayoutInflater.from(context).inflate(R.layout.near_common_list_item, null);
			
			holder = new ViewHolder();
			
			view.setTag(holder);
			
			holder.iv_icon   = (ImageView)view.findViewById(R.id.iv_func_icon);
			
			holder.etv_name = (EmotionTextView)view.findViewById(R.id.etv_name);
			
			holder.etv_description = (EmotionTextView)view.findViewById(R.id.etv_description);
			
			
		}else {
			
			holder = (ViewHolder)view.getTag();
			
		}
		
		holder.iv_icon.setBackgroundResource(nearFunctions.get(position).getFuncion_icon());
		
		holder.etv_name.setText(nearFunctions.get(position).getFunction_name());
		
		holder.etv_description.setText(nearFunctions.get(position).getFunction_description());
		
		return view;
	}

	class ViewHolder{
		
		EmotionTextView etv_name;
		
		EmotionTextView etv_description;
		
		ImageView iv_icon;
		
	}
	
}
