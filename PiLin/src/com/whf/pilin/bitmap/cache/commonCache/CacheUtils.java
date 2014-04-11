package com.whf.pilin.bitmap.cache.commonCache;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.whf.pilin.bitmap.cache.ImageCache;
import com.whf.pilin.bitmap.cache.ImageCache.ImageCacheParams;

/**
 * 为方便使用缓存而写的工具类
 * @author WHF 2014-03-26
 *
 */
public class CacheUtils {
	/**
	 * 获取缓存
	 */
	public static ImageCache getImageCache(Context context,String imagePath){
		
		ImageCache imageCache = null;
		
		ImageCacheParams imageCacheParams = new ImageCacheParams(new File(getImagePath(context, imagePath)));
		
		imageCacheParams.setMemCacheSizePercent(context, 0.25f);
		
		imageCacheParams.compressFormat = CompressFormat.PNG;
		
		imageCache = new ImageCache(imageCacheParams);
		
		return imageCache;
		
		
	}
	
	/**
	 * 取得本应用的缓存图片位置
	 * @param context 上下文对象
	 * @param imagePath 缓存图片子文件夹
	 * @return
	 */
	public static String getImagePath(Context context,String imagePath){
		
		File dir = null;
		
		if(isSDCardExist()){
			
			dir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + context.getPackageName() + "/" + imagePath);
			
		}else {
			
			dir = new File(getDataPath(context) + "/" + imagePath);
			
		}
		
		return dir.getPath();
		
	}
	/**
	 * 判断内部存储路径
	 * @return
	 */
	public static boolean isSDCardExist(){
		
		String state = Environment.getExternalStorageState();
		
		if(state.equals(Environment.MEDIA_MOUNTED))
			
			return true;
			
		else 
			
			return false;
	}
	/**
	 * 取得内部存储路径
	 * @return
	 */
	public static String getDataPath(Context context){
		
		return "/data/data/" + context.getPackageName();
		
	}
	
}
