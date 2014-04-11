package com.whf.pilin.activitys.splash;

import java.io.File;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.pilin.PiLinApplication;
import com.whf.pilin.R;
import com.whf.pilin.activitys.BaseActivity;
import com.whf.pilin.activitys.login.LoginActivity;
import com.whf.pilin.activitys.main.MainFragmentActivity;
import com.whf.pilin.entity.VersionInfo;
import com.whf.pilin.utils.HttpUtils;
import com.whf.pilin.utils.UpdateVersionParser;

public class SplashActivity extends BaseActivity {

	private String version;

	private VersionInfo lastVersionInfo;

	private ProgressDialog progressDialog;

	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (PiLinApplication.getInstance().isExistMainActivity()) {
			mStartActivity(MainFragmentActivity.class);
			return;
		}
		SharedPreferences sf = getSharedPreferences("config", MODE_PRIVATE);
		setContentView(R.layout.splash_layout);
		// if(!sf.getBoolean("guide", false)){
		// Intent intent = new
		// Intent(getApplicationContext(),GuideActivity.class);
		// startActivity(intent);
		// finish();
		// }
		new Thread() {

			@Override
			public void run() {

				try {
					sleep(2000);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		}.start();
		initViews();
		initEvents();
	}

	@Override
	protected void initViews() {
		tv_version = (TextView) findViewById(R.id.tv_version);
		version = getVersion();
		tv_version.setText("版本:v" + version);
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("正在下载...");
	}

	@Override
	protected void initEvents() {
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == 0) {

				new VersionTask().execute();

			} else if (msg.what == 1) {

				showUpdateDialog();

			}

		};

	};

	/**
	 * 显示下载提示
	 */
	private void showUpdateDialog() {
		if (lastVersionInfo == null) {
			mStartActivity(LoginActivity.class);
			return;
		}

		if (!version.equals(lastVersionInfo.getVersion())) {

			showAlertDialog("新版本更新", lastVersionInfo.getUpdateDescription(),
					android.R.drawable.ic_dialog_info, false,
					android.R.style.Theme_DeviceDefault_Light_Dialog,
					"确定",new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

							String appName = getApplicationInfo().name;

							String path = Environment
									.getExternalStorageDirectory()
									+ "/"
									+ appName + "/update";

							File dir = new File(path);

							if (!dir.exists()) {

								dir.mkdirs();

							}

							String apkPath = path + "/" + appName + "_"
									+ lastVersionInfo.getVersion() + ".apk";

							UpdateTask updateTask = new UpdateTask(
									lastVersionInfo.getUpdateUrl(), apkPath);

							progressDialog.show();

							new Thread(updateTask).start();
						}
					},
					"取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
							
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									mStartActivity(LoginActivity.class);
								}
							}, 2000);

						}

					});

		}

	}

	/**
	 * 下载文件线程
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateTask implements Runnable {

		private String updateUrl;

		private String savePath;

		public UpdateTask(String updateUrl, String savePath) {

			this.updateUrl = updateUrl;

			this.savePath = savePath;

		}

		public void run() {

			try {
				Looper.prepare();
				File file = HttpUtils.sendGetFile(updateUrl, savePath,
						progressDialog, "GET");
				progressDialog.dismiss();
				install(file);
				Looper.loop();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "下载失败",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}
	}

	/**
	 * 安装文件
	 * 
	 * @param file
	 */
	private void install(File file) {

		Intent intent = new Intent();

		intent.setAction(Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");

		finish();

		startActivity(intent);

	}

	/**
	 * 获取当前版本�?
	 * 
	 * @return
	 */
	private String getVersion() {

		PackageManager packageManager = getPackageManager();

		try {

			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);

			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "获取版本错误";
	}

	/**
	 * 获取最新版本信息
	 * 
	 * @return
	 */
	private VersionInfo getLastVersion() {

		InputStream is = HttpUtils.getInputStreamFromNet(this
				.getString(R.string.updateUrl));

		if (is == null)
			return null;

		VersionInfo versionInfo = UpdateVersionParser
				.getVersionInfoFromInputStream(is);

		if (versionInfo == null)
			return null;

		return versionInfo;

	}

	class VersionTask extends AsyncTask<VersionInfo, Void, VersionInfo> {

		@Override
		protected VersionInfo doInBackground(VersionInfo... params) {

			lastVersionInfo = getLastVersion();
			handler.sendEmptyMessage(1);

			return lastVersionInfo;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

}
