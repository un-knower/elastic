/**
 * 包名：com.yqsapp.common.annotation
 * 文件名：ValidateUtil.java
 * 版本信息：
 * 日期：2014年7月3日-上午10:21:39
 * 
 */

package com.manji.elastic.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 
 * 类名称：ValidateUtil
 * 类描述：根据字段注解验证对象的值是否满足注解定义，使用本类需要加入oval框架的依赖包
 * 修改时间：2014年7月3日 上午10:21:39
 * 修改备注：
 * 
 * @version 1.0.0
 * 
 */
public class ValidateUtil {

	private static Validator validator = null;
	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	
	private static int errMsgCount = 8;

	private ValidateUtil() {
	}

	/**
	 * 
	 * 验证对象的字段值是否符注解定义
	 * 
	 * @param <T>
	 * @param obj 需要验证的对象
	 * @return 返回空字符串表示验证通过，否则返回错误信息 (所有字段一起提示)
	 * @exception
	 * @since 1.0.0
	 */
	public static String baseCheck(Object obj) {
		String validateMsg = "";
		if (null == obj) {
			return "[validatedObject] must not be null";
		}

		int index = 1;
		Set<ConstraintViolation<Object>> validateSet = validator.validate(obj);
		
		for (ConstraintViolation<Object> constraintViolation : validateSet) {
			validateMsg += constraintViolation.getPropertyPath() + ":"
					+ constraintViolation.getMessage() + ";";
			if(index==errMsgCount){
				validateMsg += "error msg more than "+errMsgCount+"...";
				break;
			}
			index++;
		}

		return validateMsg;
	}
	
	/**
	 * 
	 * 验证对象的字段值是否符注解定义
	 * 
	 * @param <T>
	 * @param obj 需要验证的对象
	 * @return 返回空字符串表示验证通过，否则返回错误信息 (单个字段一个一个提示)
	 * @exception
	 * @since 1.0.0
	 */
	public static String baseCheckOneT(Object obj) {
		String validateMsg = "";
		if (null == obj) {
			return "[validatedObject] must not be null";
		}
		Set<ConstraintViolation<Object>> validateSet = validator.validate(obj);
		
		for (ConstraintViolation<Object> constraintViolation : validateSet) {
			validateMsg = constraintViolation.getMessage();
			break;
		}

		return validateMsg;
	}

	/**
	 * 
	 * 验证对象的字段值是否符注解定义
	 * 
	 * @param obj 需要验证的对象
	 * @param exceptFieldName 不需要验证的字段
	 * @return 返回空字符串表示验证通过，否则返回错误信息，多个字段的错误信息用英文分号(";")分隔(所有字段一起提示)
	 * @exception
	 * @since 1.0.0
	 */
	public static String checkExcept(Object obj, String... exceptFieldName) {

		if (null == obj) {
			return "[validatedObject] must not be null";
		}

		String validateMsg = "";
		
		int index = 1;
		Set<ConstraintViolation<Object>> validateSet = validator.validate(obj);

		for (ConstraintViolation<Object> constraintViolation : validateSet) {
			String field = constraintViolation.getPropertyPath().toString();
			if (!isExcept(field, exceptFieldName)) {
				validateMsg += constraintViolation.getPropertyPath() + ":"
						+ constraintViolation.getMessage() + ";";
				if(index==errMsgCount){
					validateMsg += "error msg more than "+errMsgCount+"...";
					break;
				}
				index++;
			}
		}
		return validateMsg;
	}
	/**
	 * 
	 * 验证对象的字段值是否符注解定义
	 * 
	 * @param obj 需要验证的对象
	 * @param exceptFieldName 不需要验证的字段
	 * @return 返回空字符串表示验证通过，否则返回错误信息，多个字段的错误信息用英文分号(";")分隔(单个字段一个一个提示)
	 * @exception
	 * @since 1.0.0
	 */
	public static String checkExceptOneT(Object obj, String... exceptFieldName) {

		if (null == obj) {
			return "[validatedObject] must not be null";
		}

		String validateMsg = "";
		Set<ConstraintViolation<Object>> validateSet = validator.validate(obj);

		for (ConstraintViolation<Object> constraintViolation : validateSet) {
			String field = constraintViolation.getPropertyPath().toString();
			if (!isExcept(field, exceptFieldName)) {
				validateMsg=constraintViolation.getMessage();
				break;
			}
		}
		return validateMsg;
	}

	/**
	 * 
	 * 判断字段是否属于例外字段列表
	 * 
	 * @param field
	 * @param exceptFieldName
	 * @return true:属于例外字段 false:不是例外字段
	 * @exception
	 * @since 1.0.0
	 */
	private static boolean isExcept(String field, String... exceptFieldName) {
		for (String ef : exceptFieldName) {
			if (ef != null && !"".equals(ef) && field.indexOf(ef) >= 0) {
				return true;
			}
		}
		return false;
	}

}
