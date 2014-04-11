package com.whf.pilin.activitys.login;

import org.jivesoftware.smack.XMPPException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.BaseActivity;
import com.whf.pilin.activitys.main.MainFragmentActivity;
import com.whf.pilin.connection.MXmppConnManager;
import com.whf.pilin.constVar.CustomConst;
import com.whf.pilin.view.CommonDialog;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText et_name;

	private EditText et_pwd;

	private Button btn_login;

	private SharedPreferences sharedPreferences;

	private CommonDialog dialog;

	private boolean success;

	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		try {
			if (autoLogin()) {
				mStartActivity(MainFragmentActivity.class);
				finish();
				return;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		setContentView(R.layout.login_main_layout);
		getActionBar().setTitle("登录");
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		et_name = (EditText) findViewById(R.id.et_username);
		et_pwd = (EditText) findViewById(R.id.et_userpwd);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	@Override
	protected void initEvents() {

		btn_login.setOnClickListener(this);

	}

	private boolean autoLogin() throws XMPPException {
		if (sharedPreferences.getString("n", "").equals("")
				&& sharedPreferences.getString("p", "").equals("")) {
			return false;
		} else {
			try {
				if (PiLinApplication.xmppConnection == null
						|| !PiLinApplication.xmppConnection.isConnected()) {
					
					MXmppConnManager.getInstance().new InitXmppConnectionTask(
							handler).execute().get();
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							
							while (!success) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
						}
					}).start();

				}
				MXmppConnManager.getInstance().mXmppLogin(
						sharedPreferences.getString("n", ""),sharedPreferences.getString("p", ""),
						getApplicationContext(),handler);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "账号或者密码错误", Toast.LENGTH_SHORT).show();
			}
			return true;
		}

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_login) {

			if ("".equals(et_name.getText().toString().trim())
					|| "".equals(et_pwd.getText().toString().trim())) {

				Toast.makeText(LoginActivity.this, "账号/密码不能为空",
						Toast.LENGTH_SHORT).show();

				return;

			}

			dialog = new CommonDialog(this, R.style.Loading_Dialog,
					R.layout.common_loading_dialog_layout);

			dialog.show();

			handler.postDelayed(new LoginRunnable(), 1000);

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (msg.what == CustomConst.XMPP_HANDLER_SUCCESS) {

				if (dialog != null)
					dialog.dismiss();

				if (et_name != null) {

					Editor editor = sharedPreferences.edit();
					editor.putString("n", et_name.getText().toString().trim());
					editor.putString("p", et_pwd.getText().toString().trim());
					editor.commit();

				}
				mStartActivity(MainFragmentActivity.class);

				finish();

			} else if (msg.what == CustomConst.XMPP_HANDLER_ERROR) {

				if (dialog != null)
					dialog.dismiss();

				if (msg.arg1 == CustomConst.XMPP_ERROR_LOGINFAIL) {

					Toast.makeText(LoginActivity.this, "账号或者密码错误",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(LoginActivity.this, "网络存在异常,请检查",
							Toast.LENGTH_SHORT).show();

					handler.postDelayed(new LoginRunnable(), 60000);
				}

			}

		};

	};

	class LoginRunnable implements Runnable {
		@Override
		public void run() {
			String name = "";
			String pwd = "";
			if (et_name != null) {

				name = et_name.getText().toString().trim();
				pwd = et_pwd.getText().toString().trim();

			} else {
				name = sharedPreferences.getString("n", "");
				pwd = sharedPreferences.getString("p", "");
			}

			success = MXmppConnManager.getInstance().mXmppLogin(name, pwd,
					getApplicationContext(),handler);
			if (success)
				handler.sendEmptyMessage(CustomConst.XMPP_HANDLER_SUCCESS);
			else{
				Message msg = new Message();
				msg.what = CustomConst.XMPP_HANDLER_ERROR;
				msg.arg1 = CustomConst.XMPP_ERROR_LOGINFAIL;
				handler.sendMessage(msg);
			}

		}

	}

}
