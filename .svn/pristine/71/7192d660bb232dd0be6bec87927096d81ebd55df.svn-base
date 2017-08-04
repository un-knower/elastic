package com.manji.elastic.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String formatUpper32(String plainText) {
		String buf = format32(plainText);
		return buf.toUpperCase();
	}
	
	public static String format32(String plainText) {
		String buf = format(plainText);
		return buf;
	}
	
	public static String format16(String plainText) {
		String buf = format(plainText);
		return buf.substring(8, 24);
	}
	
	public static String format(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(format32("12345613628420127"));
	}
}
