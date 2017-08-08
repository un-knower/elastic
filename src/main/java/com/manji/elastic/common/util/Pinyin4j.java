package com.manji.elastic.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汉字转拼音(支持多音字)
 *                       
 * @Filename Pinyin4j.java
 *
 * @Description 
 *
 * @Version 1.0
 *
 * @History
 *<li>Date: 2014-7-8</li>
 *<li>Version: 1.0</li>
 *<li>Content: create</li>
 *
 */
public final class Pinyin4j {
	private static final Logger logger = LoggerFactory.getLogger(Pinyin4j.class);
	
	public static String[] getPinYinHeadChar(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		
		char[] chars = str.toCharArray();
		
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		
		String[][] temp = new String[str.length()][];
		
		for (int i = 0; i < chars.length; i++) {
			char c = str.charAt(i);
			if(String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
				try {
					temp[i] = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error("{}", e);
				}
			}
			if(temp[i] != null) {
				if(temp[i].length >= 2) {
					for (int j = 0; j < temp[i].length; j++) {
						temp[i][j] = String.valueOf(temp[i][j].charAt(0));
					}
				} else {
					temp[i][0] = String.valueOf(temp[i][0].charAt(0));
				}
			} else {
				temp[i][0] = "";
			}
		}
		
		return exchange(temp);
	}
	
	public static String[] getPinyin(String str) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		
		char[] chars = str.toCharArray();
		
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
		hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		String[][] temp = new String[str.length()][];
		
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			if(String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
				try {
					temp[i] = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error("{}", e);
				}
			} else if (((int) c >= 65 && (int) c <= 90)
					|| ((int) c >= 97 && (int) c <= 122)) {
				temp[i] = new String[]{String.valueOf(chars[i])}; 
			} else {
				temp[i] = new String[]{""};
			}
		}
		
		return exchange(temp);
	}
	
	private static String[] exchange(String[][] strJaggedArray) {
		String[][] temp = doExchange(strJaggedArray);
		return temp[0];
	}
	
	private static String[][] doExchange(String[][] strJaggedArray) {
		int len = strJaggedArray.length;
		
		if (len >= 2) {
			int len1 = strJaggedArray[0].length;
			int len2 = strJaggedArray[1].length;
			int newlen = len1 * len2;
			String[] temp = new String[newlen];
			int Index = 0;
			
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
					Index++;
				}
			}
			String[][] newArray = new String[len - 1][];
			for (int i = 2; i < len; i++) {
				newArray[i - 1] = strJaggedArray[i];
			}
			
			newArray[0] = temp;
			return doExchange(newArray);
		} else {
			return strJaggedArray;
		}
	}
	/**  
     * 获取汉字串拼音首字母，英文字符不变  
     * @param chinese 汉字串  
     * @return 汉语拼音首字母  
     */   
    public static String getFirstSpell(String chinese) {   
            StringBuffer pybf = new StringBuffer();   
            char[] arr = chinese.toCharArray();   
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
            for (int i = 0; i < arr.length; i++) {   
                if (arr[i] > 128) {   
                    try {   
                            String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
                            if (temp != null) {   
                                    pybf.append(temp[0].charAt(0));   
                            }   
                    } catch (BadHanyuPinyinOutputFormatCombination e) {   
                            e.printStackTrace();   
                    }   
                } else {   
                        pybf.append(arr[i]);   
                }   
            }   
            return pybf.toString().replaceAll("\\W", "").trim();   
    } 
    /**
    * @方法说明: 第一个字符的首字母  大写  
    * @参数: @param args
    * @作者: XSM
    * @创建时间: 2015年1月6日 下午5:51:04  
     */
    public static String getFirstZM(String chinese) {   
		//得到首字
		String shou = (String) chinese.subSequence(0, 1);
		//判断首字 是不是汉子
		if(!shou.matches("[\\u4E00-\\u9FA5]+")) {//不是汉子
			return shou.toUpperCase();
		}else{//是汉子  转拼音
			return getFirstSpell(shou).toUpperCase();
		}
	}
    /**
     * 是否有中文开头，有则返回中文前缀
     * @param key
     * @return
     */
	public static String getStartChineseString(String key){
		String returnStr = "";
		if(key.matches("^[\u4e00-\u9fa5].*")){
			returnStr = key.replaceAll("([\u4e00-\u9fa5]+).*","$1");
		}
		return returnStr;
	}
	
	public static void main(String[] args) {
		String str="区b$%#$%#a/-万山I-区";
		System.out.println(getFirstZM(str));
		
	}
}
