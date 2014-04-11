package com.whf.pilin.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类：将汉字转换为拼音或获取字符串的首字母
 * 
 * @author Administrator
 * 
 */
public class PinYinUtils {
	/**
	 * 获取字符串首字母
	 * 
	 * @return
	 */
	public static String getAlpha(String chines) {

		String pinYinName = "";

		char[] nameChars = chines.toCharArray();

		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		for (int i = 0; i < nameChars.length; i++) {

			if (nameChars[i] > 128) {

				try {

					pinYinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChars[i], defaultFormat)[0].charAt(0);

				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}

			} else {

				pinYinName += nameChars[i];

			}

		}

		return pinYinName;

	}

	/**
	 * 将字符串中的中文转换为拼音，其他不变
	 * 
	 * @param chinese
	 *            中文字符串
	 * @return
	 */
	public static String convertChineseToPinYin(String chinese) {

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = chinese.trim().toCharArray();

		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {

				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]")) {

					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);

					output += temp[0];

				} else {

					output += java.lang.Character.toString(input[i]);

				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return output;

	}

	/**
	 * 汉字转换为拼音首字母，字母不变
	 * 
	 * @param chinese
	 *            汉语字符串
	 * @return
	 */
	public static String converterToFirstSpell(String chinese) {

		String pinYinName = "";

		char[] nameChar = chinese.toCharArray();
		
		if(!isChinese(nameChar[0])){
			
			return chinese.toUpperCase();
			
		}
		for (int j = 0; j < chinese.length(); j++) {
            char word = chinese.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
            	pinYinName += pinyinArray[0].charAt(0);
            } else {
            	pinYinName += word;
            }
        }
		System.out.println(pinYinName);
		return pinYinName;

	}

	// GENERAL_PUNCTUATION 判断中文的“号
	// CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
	// HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	

}
