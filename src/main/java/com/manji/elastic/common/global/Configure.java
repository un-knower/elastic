package com.manji.elastic.common.global;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Configure {
	private static final Logger logger = LoggerFactory.getLogger(Configure.class);
	/**
	 * 配置参数集合
	 */
	private static Map<String, String> allConfigMap = new HashMap<String, String>();
	
	//ES连接参数配置
	private static final String EsUrl = "EsUrl";
	private static final String EsLocation = "EsLocation";
	private static final String BASIC_PORT = "BASIC_PORT";
	private static final String EStrans_PORT = "EStrans_PORT";
	private static final String ES_NAME = "ES_NAME";
	private static final String ES_PASSWORD = "ES_PASSWORD";
	private static final String ES_sp_IndexAlias  = "ES_sp_IndexAlias";
	private static final String ES_shop_IndexAlias  = "ES_shop_IndexAlias";
	private static final String ES_shop_hot_IndexAlias  = "ES_shop_hot_IndexAlias";
	private static final String ES_shop_extra_IndexAlias  = "ES_shop_extra_IndexAlias";
	
	private static final String ES_IndexOrder = "ES_IndexOrder";
	
	private static final String ES_IndexHotSearchWords = "ES_IndexHotSearchWords";
	
	static {
		String profiles = System.getProperty("spring.profiles.active", "dev");
		logger.info("=========================>>>>在什么环境启动？===>"+profiles);
		String fileName = "config/config-" + profiles +  ".properties";
		allConfigMap = PropertiesUtil.getPropMap(fileName);
	}
	/**
	 * @return the configMap
	 */
	public static Map<String, String> getConfigMap() {
		return allConfigMap;
	}
	
	public static String getValue(String key){
		return allConfigMap.get(key);
	}
	
	/**
	 * 获取所有配置文件参数集合
	 */
	public static void setAllConfigMap(Map<String, String> paramsMap) {
		allConfigMap = paramsMap;
	}

	/**
	 * 获取所有配置Map集合
	 * @return Map<String, String>
	 */
	public static Map<String, String> getAllConfigMap() {
		return allConfigMap;
	}
	//ES参数配置
	public static String getEsUrl() {
		return PropertiesUtil.getValue(EsUrl);
	}
	public static String getEsLocation() {
		return PropertiesUtil.getValue(EsLocation);
	}
	public static String getBASIC_PORT() {
		return PropertiesUtil.getValue(BASIC_PORT);
	}
	public static String getEStrans_PORT() {
		return PropertiesUtil.getValue(EStrans_PORT);
	}
	public static String getES_NAME() {
		return PropertiesUtil.getValue(ES_NAME);
	}
	public static String getES_PASSWORD() {
		return PropertiesUtil.getValue(ES_PASSWORD);
	}
	public static String getES_sp_IndexAlias() {
		return PropertiesUtil.getValue(ES_sp_IndexAlias);
	}
	public static String getES_shop_IndexAlias() {
		return PropertiesUtil.getValue(ES_shop_IndexAlias);
	}
	public static String getES_shop_hot_IndexAlias() {
		return PropertiesUtil.getValue(ES_shop_hot_IndexAlias);
	}
	public static String getES_shop_extra_IndexAlias() {
		return PropertiesUtil.getValue(ES_shop_extra_IndexAlias);
	}
	public static String getES_IndexOrder() {
		return PropertiesUtil.getValue(ES_IndexOrder);
	}
	public static String getES_IndexHotSearchWords() {
		return PropertiesUtil.getValue(ES_IndexHotSearchWords);
	}
}
