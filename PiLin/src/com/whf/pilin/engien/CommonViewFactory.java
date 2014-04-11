package com.whf.pilin.engien;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;
/**
 * 通用View工厂类
 * @author WHF
 *
 */
public class CommonViewFactory implements TabContentFactory {
	
	private Context conetxt;
	
	public CommonViewFactory(Context conetxt){
		this.conetxt = conetxt;
	}
	
	@Override
	public View createTabContent(String tag) {
		
		View v = new View(conetxt);
		
		return v;
	}

}
