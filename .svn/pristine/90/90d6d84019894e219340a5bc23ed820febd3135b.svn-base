package com.manji.elastic.common.util;

/**
 * 
 * @author ShyMe
 * 判断密码的安全等级
 *
 */
public final class SecurityLevel {
	public static SecurityLevelEnum calculateSecurityLevel(String password) {
		if (password.length() <= 6) {
			return SecurityLevelEnum.D;
		} else {
			if (isOnlyContainNumbers(password) || isOnlyContainLetter(password)
					|| isOnlyContainSpecialCharacters(password)) {
				return SecurityLevelEnum.D;
			} else if (isContainNumbersAndLetter(password)
					|| isContainNumbersAndSpecialCharacters(password)
					|| isContainLetterAndSpecialCharacters(password)) {
				return SecurityLevelEnum.Z;
			} else if (isAllContain(password)) {
				return SecurityLevelEnum.G;
			}
		}
		return SecurityLevelEnum.D;
	}
	
	private static boolean isOnlyContainNumbers(String password) {
		return password.matches("^[0-9]+$");
	}
	
	private static boolean isOnlyContainLetter(String password) {
		return password.matches("^[a-zA-Z]+$");
	}
	
	private static boolean isOnlyContainSpecialCharacters(String password) {
		return password.matches("^[^0-9a-zA-Z]+$");
	}
	
	private static boolean isContainNumbersAndLetter(String password) {
		return password.matches("^[0-9a-zA-Z]+$");
	}
	
	private static boolean isContainNumbersAndSpecialCharacters(String password) {
		return password.matches("^[0-9[^0-9a-zA-Z]]+$");
	}
	
	private static boolean isContainLetterAndSpecialCharacters(String password) {
		return password.matches("^[a-zA-Z[^0-9a-zA-Z]]+$");
	}
	
	private static boolean isAllContain(String password) {
		return password.matches("^[0-9a-zA-Z[^0-9a-zA-Z]]+$");
	}
}
