package com.manji.elastic.dal.enums;
/**
 * 短信模版code枚举
 * @author Mr.ShyMe
 *
 */
public enum SmsTemplateCode {
	USER_REGIST("SMS_75890065"),//验证码${code},您正在注册成为${product}用户,感谢您的支持!
	USER_BIND_PHONE("SMS_7589qq0065"),//验证码${code},您正在使用${product},绑定手机号!
	CHANGE_BINDING_PHONE("SMS_7589qqw0065"),//验证码${code},您正在使用${product},修改绑定手机号!
	USER_CHANGE_PWD("SMS_75850064");//验证码${code},您正在尝试修改${product}登录密码,请妥善保管账户信息
	String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private SmsTemplateCode(String value) {
		this.value = value;
	}
}
