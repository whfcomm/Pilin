package com.whf.pilin.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.whf.pilin.entity.VersionInfo;

public class UpdateVersionParser {
	/**
	 * 从一个XML输入流中获取XML数据
	 * @param is 输入流
	 * @return
	 */
	public static VersionInfo getVersionInfoFromInputStream(InputStream is){
		
		VersionInfo versionInfo = new VersionInfo();
		
		XmlPullParser xmlPullParser = Xml.newPullParser();
		
		try {
			xmlPullParser.setInput(is, "UTF-8");
			
			int eventType = xmlPullParser.getEventType();
			
			while(eventType != XmlPullParser.END_DOCUMENT){
				
				switch (eventType) {
				
				case XmlPullParser.START_TAG:
					
					if("version".equalsIgnoreCase(xmlPullParser.getName())){
						
						versionInfo.setVersion(xmlPullParser.nextText());
						
					}else if("description".equalsIgnoreCase(xmlPullParser.getName())){
						
						String update = "";
						
						eventType = xmlPullParser.next();
						
						while(true){
							
							if(eventType == XmlPullParser.START_TAG){
								
								if("enter".equalsIgnoreCase(xmlPullParser.getName())){
									
									update = update  + xmlPullParser.nextText() + "\n";
									
								}
								
							}
							if(eventType == XmlPullParser.END_TAG && "description".equals(xmlPullParser.getName())){
								
								break;
								
							}
							
							eventType = xmlPullParser.next();
						}
						
						versionInfo.setUpdateDescription(update);
						
						
						
					}else if("apkurl".equalsIgnoreCase(xmlPullParser.getName())){
						
						versionInfo.setUpdateUrl(xmlPullParser.nextText());
						
					}
					
					break;

				}
				
				eventType = xmlPullParser.next();
				
			}
			
			return versionInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
