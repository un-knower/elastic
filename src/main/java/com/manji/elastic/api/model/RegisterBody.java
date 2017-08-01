package com.manji.elastic.api.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 注册参数
 */
public class RegisterBody {

    private String sendCode;
    private String userName;
    private String password;

    @ApiModelProperty(value = "注册验证码")
	public String getSendCode() {
		return sendCode;
	}

	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}

	@ApiModelProperty(value = "注册手机号，用户名")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "注册用户设置的密码")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
    
}
