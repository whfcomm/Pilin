package com.whf.pilin.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;

public class FileUtils {
	/**
	 * 创建文件夹
	 * @param file
	 * @return
	 */
	public static boolean mkdirs(String file){
		
		String path = file.substring(0, file.lastIndexOf("/") + 1);
		
		File fi = new File(path);
		
		if(!fi.exists()){
			
			return fi.mkdir();
			
		}else{
			
			return true;
		}
		
	}
	
	/*
	 * 将byte数组写到文件
	 */
	public static void writeFileByByteArray(byte [] array,String path) throws IOException{
	
		File file = new File(path);
		
		File p = new File(path.substring(0, path.lastIndexOf("/")+1));
		
		if(!p.exists()){
			
			System.out.println(p.mkdir());
			
		}
		FileOutputStream fos = new FileOutputStream(file);
		
		fos.write(array,0,array.length);
		
		fos.close();
		
	}
	/**
	 * 获取打开相册获取的图片的路径
	 * @param intent
	 * @param context
	 * @return
	 */
	public static String getPictureSelectedPath(Intent intent,Activity activity){
		Uri uri = intent.getData();
		Cursor cursor = activity.managedQuery(uri, new String [] {MediaStore.Images.Media.DATA}, null, null, null);
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToNext();
		String path = cursor.getString(index);
		return path;
	}
	
	/**
	 * 将传进来的Bitmap经过压缩处理并写到缓存目录，再将压缩的图片返回其字节数组
	 * @param bitmap 传进的图片
	 * @param context 上下文
	 * @return
	 * @throws IOException
	 */
	public static byte[] compressAndWriteFile(Bitmap bitmap,Context context,String path) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
		FileUtils.writeFileByByteArray(baos.toByteArray(),path);
		return baos.toByteArray();
	}
	
}
