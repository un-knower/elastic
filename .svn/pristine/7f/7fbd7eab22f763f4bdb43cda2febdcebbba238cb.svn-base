package com.manji.elastic.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @ClassName: IDUtil
 * @Description:
 */
public final class IDUtils {
	private static long lastTime = System.currentTimeMillis();
	public static IDUtils instance = null;

	public synchronized static IDUtils getInstanse() {
		if (instance == null) {
			instance = new IDUtils();
		}
		return instance;
	}

	private IDUtils() {
	}

	public synchronized String getUID() {
		try {
			boolean flag = true;
			while(flag) {
				long nowTime = System.currentTimeMillis();
				if(nowTime == lastTime) {
					Thread.sleep(1);
					continue;
				} else {
					flag = false;
					lastTime = nowTime;
				}
			}
		} catch(Exception e) {
			return null;
		}
		String result = getTimeSeq() + String.valueOf(lastTime).substring(10);
		return result;
	}
	
	private String getTimeSeq() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
		return simpleDateFormat.format(new Date());
	}
	
	public static void main(String[] args) {
		System.out.println(IDUtils.getInstanse().getUID());
	}
}
