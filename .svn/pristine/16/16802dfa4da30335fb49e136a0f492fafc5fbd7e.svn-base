package com.manji.elastic.common.util;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

public abstract class ServletRequestUtil {

	public static final String parameterMapToString(
			ServletRequest servletRequest) {
		return parameterMapToString(servletRequest.getParameterMap());
	}

	public static final String parameterMapToString(Map<String, String[]> parameterMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		int i = 1;
		int size = parameterMap.size();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(Arrays.toString(entry.getValue()));
			if (i < size) {
				sb.append(", ");
			}
			i++;
		}
		sb.append("}");
		return sb.toString();
	}

	public static final Integer getInteger(HttpServletRequest request,
			String name) {
		String value = request.getParameter(name);
		return NumberUtils.createInteger(value);
	}

	/**
	 * 取得IP
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String getIpAddr(HttpServletRequest request) {
		try{
			String ip = request.getHeader("x-forwarded-for");
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
				ip = request.getRemoteAddr();
			}
			if(ip != null && ip.length() >0&&ip.contains(",")){
				ip = ip.split(",")[0];
			}
			return ip;
		}catch(Exception e){
			return "";
		}
	}
}
