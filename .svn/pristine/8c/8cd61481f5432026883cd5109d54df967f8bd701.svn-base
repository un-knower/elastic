package com.manji.elastic.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ShyMe
 *
 */
public enum SecurityLevelEnum {
	/**
	 * 账户安全等级--->高
	 */
	G("G", "高"),

	/**
	 * 账户安全等级--->中
	 */
	Z("Z", "中"),
	
	/**
	 * 账户安全等级--->低
	 */
	D("D", "低"),

	;

	/**
	 * 枚举值
	 */
	private final String code;

	/**
	 * 枚举描述
	 */
	private final String message;

	/**
	 * 构造一个<code>SecurityLevelEnum</code>枚举对象
	 * 
	 * @param code
	 * @param message
	 */
	private SecurityLevelEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return Returns the code.
	 */
	public String code() {
		return code;
	}

	/**
	 * @return Returns the message.
	 */
	public String message() {
		return message;
	}

	/**
	 * 通过枚举<code>code</code>获得枚举
	 * 
	 * @param code
	 * @return SecurityLevelEnum
	 */
	public static SecurityLevelEnum getByCode(String code) {
		for (SecurityLevelEnum _enum : values()) {
			if (_enum.getCode().equals(code)) {
				return _enum;
			}
		}
		return null;
	}

	/**
	 * 获取全部枚举
	 * 
	 * @return List<SecurityLevelEnum>
	 */
	public List<SecurityLevelEnum> getAllEnum() {
		List<SecurityLevelEnum> list = new ArrayList<SecurityLevelEnum>();
		for (SecurityLevelEnum _enum : values()) {
			list.add(_enum);
		}
		return list;
	}

	/**
	 * 获取全部枚举值
	 * 
	 * @return List<String>
	 */
	public List<String> getAllEnumCode() {
		List<String> list = new ArrayList<String>();
		for (SecurityLevelEnum _enum : values()) {
			list.add(_enum.code());
		}
		return list;
	}
}
