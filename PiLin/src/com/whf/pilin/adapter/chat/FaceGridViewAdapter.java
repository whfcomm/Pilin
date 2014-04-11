package com.whf.pilin.adapter.chat;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.bitmap.cache.ImageResizer;
/**
 * 表情库适配器
 * @author WHF 2014-03-25
 *
 */
public class FaceGridViewAdapter extends BaseAdapter {
	
	public static final int [] faces = {
		R.drawable.zem1,R.drawable.zem2,R.drawable.zem3,
		R.drawable.zem4,R.drawable.zem5,R.drawable.zem6,
		R.drawable.zem7,R.drawable.zem8,R.drawable.zem9,
		R.drawable.zem10,R.drawable.zem11,R.drawable.zem12,
		R.drawable.zem13,R.drawable.zem14,R.drawable.zem15,
		R.drawable.zem16,R.drawable.zem17,R.drawable.zem18,
		R.drawable.zem19,R.drawable.zem20,R.drawable.zem21,
		R.drawable.zem22,R.drawable.zem23,R.drawable.zem24,
		R.drawable.zem25,R.drawable.zem26,R.drawable.zem27,
		R.drawable.zem28,R.drawable.zem29,R.drawable.zem30,
		R.drawable.zem31,R.drawable.zem32,R.drawable.zem33,
		R.drawable.zem34,R.drawable.zem35,R.drawable.zem36,
		R.drawable.zem37,R.drawable.zem38,R.drawable.zem39,
		R.drawable.zem40,R.drawable.zem41,R.drawable.zem42,
		R.drawable.zem43,R.drawable.zem44,R.drawable.zem45,
		R.drawable.zem46,R.drawable.zem47,R.drawable.zem48,
		R.drawable.zem49,R.drawable.zem50,R.drawable.zem51,
		R.drawable.zem52,R.drawable.zem53,R.drawable.zem54,
		R.drawable.zem55,R.drawable.zem56,R.drawable.zem57,
		R.drawable.zem58,R.drawable.zem59,R.drawable.zem60,
		R.drawable.zem61,R.drawable.zem62,R.drawable.zem63,
		R.drawable.zem64,R.drawable.zem65
	};
	
	private Context context;
	
	public FaceGridViewAdapter(Context context){
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return faces.length;
	}

	@Override
	public Object getItem(int position) {
		return faces[position];
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
			
			view = LayoutInflater.from(context).inflate(R.layout.chat_biaoqing_item_layout, null);
			
			holder = new ViewHolder();
			
			view.setTag(holder);
			
			holder.iv_biaoqing = (ImageView)view.findViewById(R.id.iv_biaoqing_item);
			
		}else{
			
			holder = (ViewHolder)view.getTag();
			
		}
		//将图片从缓存里面取出，如果没有则加到缓存里面去
		Bitmap bitmap = getBitmap(PiLinApplication.mEmotions.get(position), faces[position]);
		//将缓存图片放到控件上
		holder.iv_biaoqing.setImageDrawable(new BitmapDrawable(context.getResources(), bitmap));
		
		return view;
	}

	private Bitmap getBitmap(String key,int res){
		SoftReference<Bitmap> soft = PiLinApplication.getInstance().mSendbarCache.get(key);
		Bitmap bitmap = null;
		if(soft == null){
			bitmap = ImageResizer.decodeSampledBitmapFromResource(context.getResources(), res, 60, 60);
			soft = new SoftReference<Bitmap>(bitmap);
			PiLinApplication.getInstance().mSendbarCache.put(key,soft);
		}else{
			bitmap = soft.get();
			if(bitmap == null){
				bitmap = ImageResizer.decodeSampledBitmapFromResource(context.getResources(), res, 60, 60);
				soft = new SoftReference<Bitmap>(bitmap);
				PiLinApplication.getInstance().mSendbarCache.put(key,soft);
			}
		}
		return bitmap;
	}
	
	class ViewHolder{
		
		ImageView iv_biaoqing;
		
	}
	
}
