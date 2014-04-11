package com.whf.pilin.adapter.location;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whf.pilin.R;

public class ListAdapter extends BaseAdapter {
	
	private List<String []> positions;
	
	private Context context;
	
	public ListAdapter(List<String []> positions,Context context){
		this.positions = positions;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return positions.size();
	}

	@Override
	public Object getItem(int position) {
		return positions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		
		if(convertView == null){
			
			view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
			
		}else{
			
			view = convertView;
			
		}
		
		TextView tv_position = (TextView)view.findViewById(R.id.tv_position);
		
		TextView tv_time = (TextView)view.findViewById(R.id.tv_time);
		
		TextView tv_latlng = (TextView)view.findViewById(R.id.tv_latlng);
		
		tv_position.setText(positions.get(position)[0]);
		tv_time.setText("定位时间:"  + positions.get(position)[1]);
		tv_latlng.setText("经纬度:" + positions.get(position)[2]);
		
		return view;
	}

}
