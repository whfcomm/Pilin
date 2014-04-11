package com.whf.pilin.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/**
 * 自定义的对话框
 * @author WHF
 *
 */
public class CommonDialog extends Dialog {
	
	public CommonDialog(Context context) {
		super(context);
	}
	
	protected CommonDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	/**
	 * 自定义对话框设置
	 * @param context 上下文
	 * @param theme 主题
	 * @param layout 挂载界面
	 */
	public CommonDialog(Context context,int theme,int layout) {
		
		super(context, theme);
		
		setCancelable(false);
		
		getWindow().setGravity(Gravity.CENTER);
		
		setContentView(LayoutInflater.from(context).inflate(layout, null),
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));
		
	}
	
}
