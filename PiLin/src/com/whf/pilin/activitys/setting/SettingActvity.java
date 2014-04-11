package com.whf.pilin.activitys.setting;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.connection.MXmppConnManager;

public class SettingActvity extends Activity implements OnClickListener,OnCheckedChangeListener{
	
	private SharedPreferences sf;
	
	private View includeSwService;
	
	private View includeRunInit;
	
	private View includeText_mills;
	
	private View includeText_miter;
	
	private View includeRadio;
	
	private TextView tv_mills_value;
	
	private TextView tv_miter_value;
	
	private Dialog dialogSetLocationHz;
	
	private View dialogView;
	/**
	 * 设置定位频率参数
	 */
	private Button btn_commit_ok;
	
	private Button btn_commit_cancel;
	
	private EditText ed_dialog;
	//判断是设置 mills  还是meter  mills:true;meter:false
	private boolean isMillsMeter;
	
	private RadioGroup radioGroup;
	
	private RadioButton radio_auto;
	
	private RadioButton radio_gps;
	
	private RadioButton radio_net;
	
	private RadioButton radio_passive;
	
	private Button btn_logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_tableview_layout);
		getActionBar().setBackgroundDrawable(new ColorDrawable(0xff47b8ff));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		sf = getSharedPreferences("config",MODE_PRIVATE);
		
		initView();
		
		loadSharedPreferences();
		
	}
	
	private void loadSharedPreferences() {
		
		Editor editor = sf.edit();
		
//		swButtonService.setChecked(sf.getBoolean("RunAsService", true));
//		
//		switchButtonRunOnStart.setChecked(sf.getBoolean("RunOnStart", true));
//		
		tv_mills_value.setText(sf.getInt("HZ_Mills", 15000) + "");
		
		tv_miter_value.setText(sf.getInt("HZ_Miter", 0) + "");
		
		int radio = sf.getInt("LocationType", 0);
		
		switch (radio) {
		case 0:
			radio_auto.setChecked(true);
			break;
		case 1:
			radio_gps.setChecked(true);
			break;
		case 2:
			radio_net.setChecked(true);
			break;
		case 3:
			radio_passive.setChecked(true);
			break;
		}
		
		editor.commit();
		
	}

	private void initView(){
		
		TextView tv_radio = (TextView)findViewById(R.id.line_radio).findViewById(R.id.tv_line);
		//TextView tv_switch = (TextView)findViewById(R.id.line_switch).findViewById(R.id.tv_line);
		TextView tv_text = (TextView)findViewById(R.id.line_text).findViewById(R.id.tv_line);
		
		TextView tv_logout = (TextView)findViewById(R.id.line_logout).findViewById(R.id.tv_line);
		
		//includeSwService = findViewById(R.id.include_sw_service);
		//includeRunInit = findViewById(R.id.include_sw_runinit);
		includeRadio = findViewById(R.id.include_ra);
		
		btn_logout = (Button)findViewById(R.id.btn_logout);
		
		includeText_mills = findViewById(R.id.include_text_mills);
		includeText_miter = findViewById(R.id.include_text_miter);
		
//		swButtonService = (SwitchButton)includeSwService.findViewById(R.id.sw_item); 
//		switchButtonRunOnStart = (SwitchButton)includeRunInit.findViewById(R.id.sw_item);
//		
		radioGroup = (RadioGroup)includeRadio.findViewById(R.id.group);
		
		radio_auto = (RadioButton)includeRadio.findViewById(R.id.rb_auto);
		radio_gps = (RadioButton)includeRadio.findViewById(R.id.rb_gps);
		radio_net = (RadioButton)includeRadio.findViewById(R.id.rb_net);
		radio_passive = (RadioButton)includeRadio.findViewById(R.id.rb_passive);
		
		//TextView tv_service = (TextView)includeSwService.findViewById(R.id.tv_item);
		//TextView tv_runinit = (TextView)includeRunInit.findViewById(R.id.tv_item);
		TextView tv_mills = (TextView)includeText_mills.findViewById(R.id.tv_item);
		TextView tv_miter = (TextView)includeText_miter.findViewById(R.id.tv_item);
		tv_mills_value = (TextView)includeText_mills.findViewById(R.id.tv_value);
		tv_miter_value = (TextView)includeText_miter.findViewById(R.id.tv_value);
		
		tv_mills.setText(R.string.text_mills);
		tv_miter.setText(R.string.text_meters);
		//tv_service.setText(R.string.switch_isservice);
		//tv_runinit.setText(R.string.switch_runonstart);
		
		//includeSwService.setOnClickListener(this);
		//includeRunInit.setOnClickListener(this);
		includeText_mills.setOnClickListener(this);
		includeText_miter.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(this);
		
		tv_radio.setText("定位方式");
		tv_logout.setText("账号");
		tv_text.setText("定位频率");
		
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
	
//	private void includeRadioClick(SwitchButton switchButton,String sfName,Editor editor){
//		editor.putBoolean(sfName, !switchButton.isChecked());
//		switchButton.setChecked(!switchButton.isChecked());
//	}
	
	@Override
	public void onClick(View v) {
		
		Editor editor = sf.edit();
		
		switch(v.getId()){
			
//			case R.id.include_sw_service:
//					includeRadioClick(swButtonService,"RunAsService",editor);
//				break;
//			case R.id.include_sw_runinit:
//					includeRadioClick(switchButtonRunOnStart,"RunOnStart",editor);
//				break;
//			case R.id.switch_isservice:	
//					includeRadioClick(swButtonService,"RunAsService",editor);
//				break;
//			case R.id.switch_runoninit:
//					includeRadioClick(switchButtonRunOnStart,"RunOnStart",editor);
//				break;
				
			case R.id.include_text_mills:
				
				isMillsMeter = true;
				
				settingDialog();
				
				dialogSetLocationHz.setContentView(dialogView);
				
				dialogSetLocationHz.setTitle("请输入定位间隔时间(毫秒)");
				
				dialogSetLocationHz.show();
				
				break;
			case R.id.include_text_miter:
				
				isMillsMeter = false;
				
				settingDialog();
				
				dialogSetLocationHz.setContentView(dialogView);
				
				dialogSetLocationHz.setTitle("请输入定位间隔距离(米)");
				
				dialogSetLocationHz.show();
				
				break;	
			/////////////////////设置对话框取消按钮	
			case R.id.btn_commit_cancel:
				dialogSetLocationHz.dismiss();
				ed_dialog.setText("");
				break;
			case R.id.btn_commit_ok:
				if(isMillsMeter&&(!"".equals(ed_dialog.getText().toString()))){
					tv_mills_value.setText(ed_dialog.getText());
					editor.putInt("HZ_Mills", Integer.valueOf(ed_dialog.getText().toString()));
				}else if(!isMillsMeter&&(!"".equals(ed_dialog.getText().toString()))){
					tv_miter_value.setText(ed_dialog.getText());
					editor.putInt("HZ_Miter", Integer.valueOf(ed_dialog.getText().toString()));
				}else return;
				dialogSetLocationHz.dismiss();
				ed_dialog.setText("");
				break;
			
			case R.id.btn_logout:
				MXmppConnManager.getInstance().stopChatListener();
				MXmppConnManager.getInstance().closeConnection();
				PiLinApplication.xmppConnection = null;
				editor.putString("n", "");
				editor.putString("p", "");
				PiLinApplication.getInstance().exit();
				editor.commit();
				
				//finish();
				
				System.exit(0);
				
				break;
				
		}
		
		editor.commit();
		
	}
	/**
	 * 初始化设置对话框
	 */
	private void settingDialog(){
		
		if(dialogSetLocationHz == null){
			
			dialogSetLocationHz = new Dialog(this);
			
			dialogView = LayoutInflater.from(this).inflate(R.layout.setting_mills_miter_layout, null);
			
			btn_commit_ok = (Button)dialogView.findViewById(R.id.btn_commit_ok);
			
			btn_commit_cancel = (Button)dialogView.findViewById(R.id.btn_commit_cancel);
			
			ed_dialog = (EditText)dialogView.findViewById(R.id.ed_dialog);
		}
		
		btn_commit_ok.setOnClickListener(this);
		
		btn_commit_cancel.setOnClickListener(this);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Editor editor = sf.edit();
		switch (checkedId) {
		
			case R.id.rb_auto:
				
				editor.putInt("LocationType", 0);
				
				break;
	
			case R.id.rb_gps:
				
				editor.putInt("LocationType", 1);
				
				break;
				
			case R.id.rb_net:
				
				editor.putInt("LocationType", 2);
				
				break;
				
			case R.id.rb_passive:
				
				editor.putInt("LocationType", 3);
				
				break;
		}
		editor.commit();
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == android.R.id.home){
			
			finish();
			
		}
		
		return super.onOptionsItemSelected(item);
	}
}
