package com.whf.pilin.activitys.main.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
/**
 * 好友列表右边的字母视图
 * @author WHF 2014-03-24
 *
 */
public class FriendsSiderView extends View {
	
	//触摸发生改变触发的监听器
	OnTouchLetterChangedListener onTouchLetterChangedListener;
	//初始化选择项
	int chose = -1;
	//画笔
	Paint paint = new Paint();
	//是否在中间显示字幕
	boolean showBkg = false;
	//26个字母
	static final String [] letters = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
		"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
		"V", "W", "X", "Y", "Z" };
	
	//创建三个构造器
	public FriendsSiderView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public FriendsSiderView(Context context,AttributeSet attrs) {
		super(context, attrs);
	}
	
	public FriendsSiderView(Context context){
		super(context);
	}
	
	/**
	 * 重写绘制方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(showBkg){
			
			canvas.drawColor(Color.parseColor("#40000000"));
			
		}
		
		int height = getHeight();
		
		int width = getWidth();
		
		int singleHeight = height / letters.length;
		
		for(int i = 0;i < letters.length; i++){
			//设置画笔颜色
			paint.setColor(Color.BLACK);
			//设置画笔样式
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			
			paint.setAntiAlias(true);
			//设置字体大小
			paint.setTextSize(20);
			//判断是否是当前选择的行对应的首字母
			if(i == chose){
				//设置颜色
				paint.setColor(Color.parseColor("#3399ff"));
				//设置
				paint.setFakeBoldText(true);
				
			}
			//获取X坐标
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			//获取Y坐标
			float yPos = singleHeight * i + singleHeight;
			//画字体
			canvas.drawText(letters[i], xPos, yPos, paint);
			//重置
			paint.reset();
		}
		
	}
	/**
	 * 重写触摸事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//获取触摸事件类型
		final int action = event.getAction();
		//获取触摸的Y坐标
		final float y = event.getY();
		//上一次选中的
		final int oldChose = chose;
		//设置监听器
		final OnTouchLetterChangedListener listener = onTouchLetterChangedListener;
		
		final int c = (int)(y / getHeight() * letters.length);
		
		switch (action) {
			
			case MotionEvent.ACTION_DOWN:
				
				showBkg = true;
				
				if(oldChose != c && listener != null){
					
					if(c > 0 && c < letters.length){
						
						listener.onTouchLetterChanged(letters[c]);
						
						chose = c;
						
						invalidate();
						
					}
					
				}
				
				break;
	
			case MotionEvent.ACTION_MOVE:
				
				if(oldChose != c && listener != null){
					
					if(c > 0 && c < letters.length){
						
						listener.onTouchLetterChanged(letters[c]);
						
						chose = c ;
						
						invalidate();
						
					}
					
				}
				break;
			//当抬起时设置为结束监听，隐藏字母中间显示
			case MotionEvent.ACTION_UP:
				
				showBkg = false;
				
				chose = -1;
				
				invalidate();
				
				break;
				
		}
		
		return true;
	}
	
	
	/**
	 * 对外公开的设置监听器的方法
	 * @param listener
	 */
	public void setOnTouchLetterChangedListener(OnTouchLetterChangedListener listener){
		
		this.onTouchLetterChangedListener = listener;
		
	}
	
	public interface OnTouchLetterChangedListener{
		
		public void onTouchLetterChanged(String letter);
		
	}
	
}
