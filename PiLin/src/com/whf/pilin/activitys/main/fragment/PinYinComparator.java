package com.whf.pilin.activitys.main.fragment;

import java.util.Comparator;

import com.whf.pilin.entity.NearByPeople;
import com.whf.pilin.entity.UserInfo;
import com.whf.pilin.utils.PinYinUtils;

public class PinYinComparator implements Comparator{

	@Override
	public int compare(Object lhs, Object rhs) {
		 String str1 = PinYinUtils.convertChineseToPinYin(((NearByPeople) lhs).getPy());
	     String str2 = PinYinUtils.convertChineseToPinYin(((NearByPeople) rhs).getPy());
	     return str1.compareTo(str2);
	}
	
	
	
}
