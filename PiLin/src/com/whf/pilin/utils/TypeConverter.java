package com.whf.pilin.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class TypeConverter {
	
	
	
	/**
	 * 从一个字符串中取得javascript的function名称及去对应的参�?
	 * @param str 入参字符�?
	 * @param startSymbol �?��的符�?
	 * @param endSymbol 结束的符�?
	 * @return
	 */
	public static String [] getFunctionNameAndParamsFomString(String str,String startSymbol,String endSymbol){
		
		if("null".equalsIgnoreCase(str)){
			
			return null;
			
		}
		
		int index = str.indexOf(startSymbol);
		
		int lastSmybolIndex = str.indexOf(endSymbol);
		
		String functionName = str.substring(0, index);
		
		String paramsKeyValue = str.substring(index+1, lastSmybolIndex);
		
		return new String [] {functionName,paramsKeyValue};
		
	}
	
	/**
	 * 根据字符串和给定正则表达式分割字符串
	 * @param srcString
	 * @param regX
	 * @return
	 */
	public static String [] cnverStringToStringArray(String srcString,String regX){
		
		String [] strs = srcString.split(regX);
		
		return strs;
		
	}
	/**
	 * 将字符串转换为Map，先转为Array
	 * @param srcString 原字符串
	 * @param regx 字符串分隔符
	 * @param regy Key与Value的分隔符
	 * @return
	 */
	public static Map<String,String> converStringToMap(String srcString , String regx , String regy){
		
		String [] strs = srcString.split(regx);
		
		Map<String,String> map = new Hashtable<String,String>();
		
		for(String str:strs){
			
			String [] mapKeys = str.split(regy);
			
			if(mapKeys.length>1){
				
				map.put(mapKeys[0], mapKeys[1]);
				
			}
			
		}
		
		return map;
		
	}
	/**
	 * 根据给定日期，添加天数，返回新的日期
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date addDate(Date d,long day){
		long time = d.getTime();
		day = day * 24 * 60 * 60 *1000;
		time+=day;
		return new Date(time);
	}
	/**
	 * 判断输入时间是否为NULL或�?"",是返回当前时间，否取其�?
	 * @param srcDateStr
	 * @param formatStr
	 * @return
	 */
	public static String isDateStrNull(String srcDateStr,String formatStr){
		
//		return (srcDateStr == null || "".equals(srcDateStr)) ? (formatDate(new Date(System.currentTimeMillis()), formatStr))
//		: srcDateStr;
		return nullStringDefaultValue(srcDateStr, formatDate(new Date(System.currentTimeMillis()), formatStr));
		
	}
	/**
	 * 将指定的Date格式化为指定的格�?
	 * @param date 日期
	 * @param formatString 格式字符�?
	 * @return
	 */
	public static String formatDate(Date date,String formatString){
		
		return new SimpleDateFormat(formatString).format(date);
		
	}
	
	/**
	 * 根据�?��字符串获取日�?
	 * @param dateString 日期字符�?
	 * @param formatString 要转换的字符串格�?
	 * @return 返回日期
	 * @throws ParseException
	 */
	public static Date getDateFromString(String dateString,String formatString) throws ParseException{
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		
		Date date = simpleDateFormat.parse(dateString);
		
		return date;
	}
	
	/**
	 * 获取本周�?��日期
	 * @return
	 */
	public static Date theFirstDayOfWeek(){
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		
		return calendar.getTime();
		
	}
	/**
	 *  判断�?��字符串是否在空，为其设置�?��默认�?
	 * @param srcStr 要判断的字符�?
	 * @param defaultValue 默认�?
	 * @return
	 */
	public static String nullStringDefaultValue(String srcStr,String defaultValue){
		
		return (srcStr == null || "".equals(srcStr)) ? defaultValue
		: srcStr;
		
	}
	/**
	 * 判断�?��字符串是否与另一个字符串相等
	 * @param src
	 * @param destSrc
	 * @return
	 */
	public static boolean ajustStringsEqueal(String src,String destSrc){
		
		return destSrc.equals(src);
		
	}
	/**
	 * 将单位的月日时分秒转化为双位�?
	 * @return
	 */
	public static String returnXXMonthDayHourMinte(int src){
		
		return (src >9) ? src+"" : "0"+ src;
		
	}
	/**
	 * 将输入流转换为字符串
	 * @param is
	 * @return
	 */
	public static String convertInputStreamToString(InputStream is) throws IOException{
		
		if(is==null)return null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String line = "";
		
		try {
			StringBuffer sb = new StringBuffer();
			
			while((line=br.readLine())!=null){
				
				sb.append(line);
				
			}
			return line;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 获取唯一的UUID
	 * @return
	 */
	public static String  getUUID(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
		
	}
	/**
	 * 将读取数据写到指定地方
	 * @param writer
	 * @param inputStream
	 * @return
	 */
	public static boolean writeFile(BufferedOutputStream writer,InputStream inputStream){
		
		int len = 0;
		
		byte [] buff = new byte [2014];
		
		try {
			while((len = inputStream.read(buff)) > 0){
				
				writer.write(buff, 0, len);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		//System.out.println(converUrlGetParams("sub('stopUsers.action?authorName=WHF<GSBM,mzoneid>')"));
		
		System.out.println(formatDate(theFirstDayOfWeek(),"yyyy-MM-dd"));
		
	}
	
	
}
