package com.whf.pilin.adapter.main.friends;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whf.pilin.R;
import com.whf.pilin.activitys.main.fragment.PinYinComparator;
import com.whf.pilin.adapter.BaseListViewAdapter;
import com.whf.pilin.bitmap.cache.ImageFetcher;
import com.whf.pilin.bitmap.cache.commonCache.CacheUtils;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.entity.NearByPeople;
import com.whf.pilin.utils.PinYinUtils;
import com.whf.pilin.view.EmotionTextView;
/**
 * 好友列表项适配器
 * @author WHF 2014-03-24
 *
 */
public class FriendListAdapter extends BaseListViewAdapter{
	
	private List<NearByPeople> mFriends;
	
	public FriendListAdapter(Context context,List<NearByPeople> mFriends){
		
		super(context, mFriends);
		
		this.context = context;
		
		NearByPeople [] arr = mFriends.toArray(new NearByPeople[mFriends.size()]);
		
		Arrays.sort(arr, new PinYinComparator());
		
		this.mFriends = (List<NearByPeople>)Arrays.asList(arr);
	}
	
	public List<NearByPeople> getFriends(){
		return mFriends;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		
		ViewHolder holder = null;
		
		if(view == null){
			
			view = LayoutInflater.from(context).inflate(R.layout.user_info_item_layout, null);
			
			holder = new ViewHolder();
			
			view.setTag(holder);
			
			holder.tv_catalog = (TextView)view.findViewById(R.id.tv_catalog);
			
			holder.iv_icon = (ImageView)view.findViewById(R.id.iv_usr_icon);
			
			holder.tv_niName = (TextView)view.findViewById(R.id.tv_usr_niname);
			
			holder.tv_state = (TextView)view.findViewById(R.id.tv_user_state);
			
			holder.tv_mood = (EmotionTextView)view.findViewById(R.id.tv_usr_mood);
			
			holder.tv_sex = (TextView)view.findViewById(R.id.tv_usr_sex);
			
			holder.ll_bind_bussiness = (LinearLayout)view.findViewById(R.id.bind_business);
			
		}else{
			
			holder = (ViewHolder)convertView.getTag();
			
		}
		
		String catalog = PinYinUtils.converterToFirstSpell(mFriends.get(position).getName()).substring(0,1);
		
		if(position == 0){
			holder.tv_catalog.setVisibility(View.VISIBLE);
			holder.tv_catalog.setText(catalog);
		}else{
			
			String lastCatalog = PinYinUtils.converterToFirstSpell(mFriends.get(position - 1).getName()).substring(0, 1);
			
			if(catalog.equalsIgnoreCase(lastCatalog)){
				
				holder.tv_catalog.setVisibility(View.GONE);
				
			}else{
				
				holder.tv_catalog.setVisibility(View.VISIBLE);
				
				holder.tv_catalog.setText(catalog);
				
			}
			
		}
		
		imageFetcher.loadImage("http://imgt5.bdstatic.com/it/u=2795935915,75223816&fm=116&gp=0.jpg", holder.iv_icon);
		
		holder.tv_niName.setText(mFriends.get(position).getName());
		
		holder.tv_state.setText(setStateStr(mFriends.get(position).getState()));
		
		holder.tv_mood.setText(mFriends.get(position).getSign());
		
		holder.tv_sex.setText((mFriends.get(position).getGender() == 1?"男":"女") + " "  + mFriends.get(position).getAge());
		
		holder.tv_sex.setPadding(10, 1, 10, 1);
		
		holder.ll_bind_bussiness.setBackgroundResource(mFriends.get(position).getGender() == 0?R.drawable.bg_gender_famal:R.drawable.bg_gender_male);
		
		return view;
	}
	
	private String setStateStr(int code){
		
		String state = "隐身";
		
		switch (code) {
		//设置在线
		case CustomConst.USER_STATE_ONLINE:
			state = "在线";
			break;
		//Q我吧
		case CustomConst.USER_STATE_Q_ME:
			state = "Q我";
			break;
		//忙碌
		case CustomConst.USER_STATE_BUSY:
			state = "忙碌";
			break;
		//离开	
		case CustomConst.USER_STATE_AWAY:
			state = "离开";
			break;
			
		case CustomConst.USER_STATE_OFFLINE:
			state = "下线";
			break;
			
		}
		return state;
	}
	
	class ViewHolder{
		
		 ImageView iv_icon;
		 
		 TextView tv_catalog;
		 
		 TextView tv_niName;
		
		 TextView tv_state;
		
		 EmotionTextView tv_mood;
		
		 TextView tv_sex;
		 
		 LinearLayout ll_bind_bussiness;
	}

	@Override
	protected void initImageFetcher() {
		
		imageFetcher = new ImageFetcher(context, 60 ,60);
		
		imageFetcher.setImageCache(CacheUtils.getImageCache(context, "imageCache"));
		
		imageFetcher.setLoadingImage(R.drawable.position_selecter);
		
		imageFetcher.setImageFadeIn(true);
		
	}
	
}
