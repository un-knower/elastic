package com.manji.elastic.api.commom.utils;
/**
 * 区域code处理
 * @author Mr.ShyMe
 *
 */
public class AreaCodeUtil {

	public static String doACode(String area_code,int len){
		String areaCode = "";
		switch (len) {
		case 6:
			String temp61 = area_code.substring(0, 1);
			String temp62 = area_code.substring(0, 3);
			areaCode = temp61 + " " + temp62 + " " + area_code;
			break;
		case 4:
			String temp41 = area_code.substring(0, 1);
			areaCode = temp41 + " " + area_code;
			break;
		case 2:
			if (!area_code.equals("00")) {
				areaCode = "00 " + area_code;
			} else {
				areaCode = area_code;
			}
			break;
		default:
			areaCode = "00";
		}
		return areaCode;
	}
}
