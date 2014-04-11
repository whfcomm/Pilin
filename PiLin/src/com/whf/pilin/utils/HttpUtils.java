package com.whf.pilin.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class HttpUtils {
	
	public static boolean sendPosition(String [] str,String url) {
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("position", str[0]));
			pairs.add(new BasicNameValuePair("latlng", str[2]));
			pairs.add(new BasicNameValuePair("imie", str[3]));
			
			HttpEntity entity = new UrlEncodedFormEntity(pairs,HTTP.UTF_8);
			
			post.setEntity(entity);
			
			HttpResponse response = httpClient.execute(post);
			
			if(response.getStatusLine().getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			
			httpClient.getConnectionManager().shutdown();
			
		}
		
	}
	/**
	 * 发送未发送成功的坐标点
	 * @param postionArrayToString
	 * @param url
	 * @return
	 */
	public static boolean  mResendPosition(String postionArrayToString,String url){
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("positions", postionArrayToString));
			
			HttpEntity entity = new UrlEncodedFormEntity(pairs,HTTP.UTF_8);
			
			post.setEntity(entity);
			
			HttpResponse response = httpClient.execute(post);
			
			if(response.getStatusLine().getStatusCode() == 200){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			
			httpClient.getConnectionManager().shutdown();
			
		}
		
	}
	
	/**
	 * 判断网络是否可用
	 * @param connectivityManager
	 * @return
	 */
	public static boolean isNetWorkAccess(ConnectivityManager connectivityManager,Context context){
		
		NetworkInfo mobileNet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(!mobileNet.isConnected() && !wifiNet.isConnected()){
			Toast.makeText(context, "当前网络不可用...", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
		
	}
	/**
	 * 获取一个网络请求结果字符串
	 * @param url
	 * @return
	 */
	public static String queryGet(String url){
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet httpGet = new HttpGet(url);
		
		HttpResponse response = null;
		
		try {
			response = httpClient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() == 200){
				
				return EntityUtils.toString(response.getEntity());
				
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			httpClient.getConnectionManager().shutdown();
			
		}
		
		return null;
		
	}
	/**
	 * 从网络获取一个文件 比较大的
	 * @param url 网络文件地址
	 * @param savePath 保存本地地址
	 * @param progressDialog 下载进度对话框
	 * @return
	 * @throws Exception 
	 */
	public static File sendGetFile(String httUrl,String savePath,ProgressDialog progressDialog,String requestType) throws Exception{
		
		URL url = new URL(httUrl);
		
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		
		connection.setConnectTimeout(3000);
		
		connection.setRequestMethod(requestType);
		
		if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK){
			
			int total = connection.getContentLength();
			
			progressDialog.setMax(total);
			
			InputStream is = connection.getInputStream();
			
			File file = new File(savePath);
			
			FileOutputStream fos = new FileOutputStream(file);
			
			byte [] buff = new byte[1024];
			
			int len = 0;
			
			int progress = 0;
			
			while((len = is.read(buff))>0){
				
				fos.write(buff, 0, len);
				
				progress += len;
				
				progressDialog.setProgress(progress);
			}
			
			fos.flush();
			
			fos.close();
			
			is.close();
			
			return file;
		}
		
		return null;
	}
	
	/**
	 * 从网络获取一个输入流 较小
	 * @param netUrl 网络地址
	 * @return
	 * @throws MalformedURLException 
	 */
	public static InputStream getInputStreamFromNet(String netUrl){
		
		try {
			
			URL url = new URL(netUrl);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.setReadTimeout(4000);
			
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				
				InputStream is = connection.getInputStream();
				
				return is;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
