/**
 * 包名：com.yqsapp.common
 * 文件名：RegexUtil.java
 * 版本信息：
 * 日期：2014年7月14日-下午2:53:04
 * 
 */

package com.manji.elastic.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 类名称：RegexUtil
 * 类描述：
 * 修改时间：2014年7月14日 下午2:53:04
 * 修改备注：
 * 
 * @version 1.0.0
 * 
 */

public class RegexUtil {
	//================================基本正则==================================
	/**
	 * 数字
	 */
	public static final String DIGITAL = "^\\d*$";
	/**
	 * 非负整数（正整数 + 0）
	 */
	public static final String NON_NEGATIVE_INTEGER = "^\\d+$";
	/**
	 * //非正整数（负整数 + 0）
	 */
	public static final String NON_POSITIVE_INTEGERS = "((-\\d+)|(0+))$";
	/**
	 * 正整数
	 */
	public static final String POSITIVE_INTEGER = "^[0-9]*[1-9][0-9]*$";
	/**
	 * 负整数
	 */
	public static final String NEGATIVE_INTEGER = "-[0-9]*[1-9][0-9]*$";
	
	
	
	//================================日期格式==================================
	/**
	 * 时间验证：格式HH:mm:ss
	 */
	public static final String TIME_FORMAT_PATTERN = "^(?:0?[0-9]|1[0-9]|2[0-3])(:?)(?:0?[0-9]|[1-5][0-9])(:?)(?:0?[0-9]|[1-5][0-9])$";
	/**
	 * 日期验证：
	 * 年份0001-9999，格式yyyy-MM-dd或yyyy-M-d，连字符可以没有或是“-”、“/”、“.”之一
	 */
	public static final String DATE_FORMAT_PATTERN = "^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])([-/.]?)(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])([-/.]?)(?:29|30)|(?:0?[13578]|1[02])([-/.]?)31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2([-/.]?)29)$";

	/**
	 * 日期时间验证：
	 * 格式yyyy-MM-dd HH:mm:ss 或yyyy-M-d HH:mm:ss，连字符可以没有或是“-”、“/”、“.”之一
	 */
	public static final String DTAETIME_FORMAT_PATTERN = "^(?:(?!0000)[0-9]{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])([-/.]?)(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])([-/.]?)(?:29|30)|(?:0?[13578]|1[02])([-/.]?)31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2([-/.]?)29)(\\s*)(?:0?[0-9]|1[0-9]|2[0-3])(:?)(?:0?[0-9]|[1-5][0-9])(:?)(?:0?[0-9]|[1-5][0-9])$";

	/**
	 * 日期模式验证：格式yyyy-MM-dd 连字符可以没有或是“-”、“/”、“.”之一
	 */
	public static final String DATE_FORMAT_MODE = "^(?:y{4}([-/.]?)M{2}([-/.]?)d{2})$";

	/**
	 * 时间模式验证：格式HH:mm:ss连字符可以没有或是“-”、“/”、“.”之一
	 */
	public static final String TIME_FORMAT_MODE = "^(?:(h{2}|H{2})(:?)m{2}(:?)s{2})$";

	/**
	 * 日期时间模式验证：格式yyyy-MM-dd HH:mm:ss连字符可以没有或是“-”、“/”、“.”之一
	 */
	public static final String DATETIME_FORMAT_MODE = "^(?:y{4}([-/.]?)M{2}([-/.]?)d{2})(\\s*)(?:(h{2}|H{2})(:?)m{2}(:?)s{2})$";
	
	//================================手机号段==================================
	/**
	 * 移动:
	 * 上网卡 147
	 * 2G号段（GSM网络）有139，138，137，136，135，134（0-8）,159，158，152，151，150
	 * 3G号段（TD-SCDMA网络）有157，188，187，182，183，184
	 * 4G号段 178
	 */
	public static final String PHONE_MOBILE = "^(86)?1((34[0-8]\\d{7})|(((3[5-9])|(5[012789])|(8[23478])|(47)|(78))\\d{8}))$";
	/**
	 * 联通:
	 * 上网卡 145
	 * 2G号段（GSM网络）有130，131，132，155，156
	 * 3G号段（WCDMA网络）有186，185
	 * 4G号段 176
	 */
	public static final String PHONE_UNICOM = "^(86)?1((3[0-2]{1})|(45)|(5[56])|(8[56]|76))\\d{8}$";
	/**
	 * 电信:
	 * 2G号段(CDMA网络)有133，153
	 * 3G号段(CDMA网络)有189，180，181
	 * 4G号段 177
	 */
	public static final String PHONE_TELECOM = "^(86)?1((33)|(53)|(8[019])|(7[0-9]))\\d{8}$";
	/**
	 * 卫通有 1349
	 */
	public static final String PHONE_SATCOM = "^(86)?1349\\d{7}$";
	/**
	 * 补充
	 * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
	 * 170号段为虚拟运营商专属号段，
	 * 170号段的 11 位手机号前四位来区分基础运营商，
	 * 其中 “1700” 为中国电信的转售号码标识，
	 * “1705” 为中国移动，
	 * “1709” 为中国联通。
	 */
	public static final String PHONE_VIRTUAL = "^(86)?170\\d{8}$";
	/**
	 * 台湾手机号 大概校验 09开头10位数字
	 */
	public static final String TAI_WAN_PHONE = "^09\\d{8}$";
	//==================================================================
	/**
	 * 
	 * checkPhoneNum(这里用一句话描述这个方法的作用)
	 * @param phone 手机号码
	 * @return 
	 * -1 无效的手机号<br/>
	 * 1  移动号段<br/>
	 * 2  联通号段<br/>
	 * 3  电信号段<br/>
	 * 4  卫通号段<br/>
	 * 5 虚拟运营商手机号<br/>
	 * @since  1.0.0
	 */
	public static int checkPhoneNum(String phone){
		if(StringUtils.isBlank(phone)){
			return -1;
		}
		int i = -1;
		if(isMathch(PHONE_MOBILE,phone)){
			i = 1;
		}else if(isMathch(PHONE_UNICOM,phone)){
			i = 2;
		}else if(isMathch(PHONE_TELECOM,phone)){
			i = 3;
		}else if(isMathch(PHONE_SATCOM,phone)){
			i = 4;
		}else if(isMathch(PHONE_VIRTUAL,phone)){
			i = 5;
		}
		return i;
	}
	/**
	 * 校验是否台湾电话
	 * 
	 */
	public static int checktaiwanPhoneNum(String phone){
		int i= -1;
		if(isMathch(TAI_WAN_PHONE,phone)){
			i =1;
		}
		return i;
	}
	/**
	 * 
	 * 正则匹配
	 * @param patternStr 正则表达式
	 * @param testMatcheStr 验证的字符串
	 * @return 
	 * <ul>
	 * <li>true：表示字符串与表达式匹配</li>
	 * <li>false：表示字符串与表达式不匹配</li>
	 * </ul>
	 * @since  1.0.0
	 */
	public static boolean isMathch(String patternStr,String testMatcheStr){
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(testMatcheStr);
		boolean flag = matcher.matches();
		return flag;
	}
	
	/**
	 * 
	 * 字符串中是否含有中文
	 * <ul>
	 * <li>true：表示含有能</li>
	 * <li>false：表示没有</li>
	 * </ul>
	 * @since  1.0.0
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}
//======================================测试区================================================================
	
	public static final String TEST = TIME_FORMAT_PATTERN;

	public static void main(String[] args) {
		System.out.println(RegexUtil.checkPhoneNum("17883479195"));
//		String testMatcheStr = "112453";
//		String patternStr = TEST;
//		Pattern pattern = Pattern.compile(patternStr);
//		Matcher matcher = pattern.matcher(testMatcheStr);
//		System.out.println(matcher.matches());
		// System.out.println(testMatcheStr.matches(DATE_FORMAT_PATTERN));
		
		
		//System.out.print(isMathch(DIGITAL,"123"));
	}
}
