package com.manji.elastic.common.global;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件工具类
 * 
 * 类名称：PropertiesUtil
 * 类描述：
 * 修改时间：2014年7月16日 上午10:53:38
 * 修改备注：
 * 
 * @version 1.0.0
 * 
 */
public class PropertiesUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String filetype = ".properties";
	private static Map<String, Properties> map = new HashMap<String, Properties>();

	/**
	 * 读取配置文件
	 * 
	 * @param fileName 文件名
	 * @return Map<String, String> 返回Map集合
	 * @throws IOException
	 */
	public static Map<String, String> getPropMap(String fileName) {
		try {
			Map<String, String> mapList = new HashMap<String, String>();
			Properties prop = new Properties();
			// 加载文件
			prop.load(new InputStreamReader(PropertiesUtil.class
					.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
			String realFileName = getRealFileName(fileName);
			map.put(realFileName, prop);

			Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				
				if (!StringUtils.isBlank(value)) {
					logger.debug("{}={}",key,value);
					mapList.put(String.valueOf(key), value);
				}
			}
			return mapList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	// ===========================================
	/**
	 * 截取文件路径中的文件名
	 * 
	 * @param fileName
	 * @return
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	private static String getRealFileName(String fileName) {
		if (null == fileName || "".equals(fileName)) {
			return null;
		}
		String[] tmp = fileName.split("\\\\");
		tmp = tmp[tmp.length-1].split("/");
		return tmp[tmp.length-1];
	}
	/**
	 * 读取配置文件
	 * 
	 * @param fileName 文件名
	 * @return Map<String, String> 返回Map集合
	 * @throws IOException
	 */
	public static Properties loadProp(String fileName) {
		InputStreamReader isr = null;
		Properties tempProp = new Properties();
		try {
			isr = new InputStreamReader(PropertiesUtil.class.getClassLoader()
					.getResourceAsStream(fileName), "UTF-8");
			String realFileName = getRealFileName(fileName);
			// 加载文件
			tempProp.load(isr);
			map.put(realFileName, tempProp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempProp;
	}

	/**
	 * 加载项目 classes 目录下 指定目录下的所有配置文件中的配置
	 * 
	 * @param folder
	 * @param filterStr
	 * @return
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public static void loadPropByFolder(String folder, String filterStr) {

		final String filter = filterStr;
		try {
			File file = new File(PropertiesUtil.class.getClassLoader()
					.getResource(folder).toURI());
			File[] files = file.listFiles(new FileFilter() {
				// 只取符合过滤规则的配置文件
				public boolean accept(File pathname) {
					String filename = pathname.getName();

					if (filename.endsWith(filetype)
							&& filename.indexOf(filter) > -1) {
						return true;
					} else {
						return false;
					}
				}
			});

			// 加载配置文件中的配置
			for (File f : files) {
				Properties tempProp = new Properties();
				tempProp.load(new InputStreamReader(new FileInputStream(f),
						"UTF-8"));
				map.put(f.getName(), tempProp);

			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取多个key对应的value
	 * 
	 * @param keys
	 * @return 返回key对应的value值的map对象，如果key在配置文件中没有配置对应的value会返回空字符串，
	 *         如果配置文件中没有指定的key则会反会null对象
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public static Map<String, String> getValueMap(String... keys) {
		if (map.size() == 0) {
			return null;
		}
		Map<String, String> valueMap = new HashMap<String, String>();
		for (String key : keys) {
			String value = getValue(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}
	
	/**
	 * 从指定的配置文件中获取多个key对应的value
	 * 
	 * @param propName
	 * @param keys
	 * @return 
	 * 
	 * @exception 
	 * @since  1.0.0
	 */
	public static Map<String,String> getValueMapFromProp(String propName,String... keys){
		if (map.size() == 0) {
			return null;
		}
		Map<String, String> valueMap = new HashMap<String, String>();
		for (String key : keys) {
			String value = getValue(key,propName);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 通过Key获取value
	 * <p>
	 * 遍历所有的配置文件，返回第一个匹配key的值
	 * </p>
	 * 
	 * @param key
	 * @return 返回key对应的value值的map对象，
	 * 	<li>如果key在配置文件中没有配置对应的value会返回空字符串</li>
	 * <li>如果配置文件中没有指定的key则会反会null对象</li>
	 * 
	 * @exception
	 * @since 1.0.0
	 */
	public static String getValue(String key) {
		String value = null;
		Set<String> propKeyset = map.keySet();
		for (String propKey : propKeyset) {
			value = getValue(key,propKey);
			if (value != null) {
				return value;
			}
		}
		return value;
	}

	/**
	 * 通过key获取指定propName的配置文件中的值
	 * 
	 * @param key
	 * @param propName
	 * @return 
	 * 
	 * @exception 
	 * @since  1.0.0
	 */
	public static String getValue(String key,String propName){
		Properties properties = map.get(propName);
		if(null==properties){
			return null;
		}
		String value = properties.getProperty(key);
		return value;
	}
	
	public static void main(String[] args) {

	}

}
