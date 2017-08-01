package com.manji.elastic.api.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 *  登录传递参数实体
 */
public class LoginBody {
	private String registrationId;//极光绑定用户用的
    private String userName;
    private String password;
    private String loginVersion;
    private String uid;
    private String openId;
    private String sType;
    private String sHeadImg;
    private String sNickName;
    
    @ApiModelProperty(value = "绑定极光需要的设备id")
    public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	@ApiModelProperty(value = "账号密码登录 用户名")
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ApiModelProperty(value = "用账号密码登录时传的密码")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @ApiModelProperty(value = "当前登录的APP版本信息")
    public String getLoginVersion() {
        return loginVersion;
    }

    public void setLoginVersion(String loginVersion) {
        this.loginVersion = loginVersion;
    }
    @ApiModelProperty(value = "如果uid不为空表示使用第三方登录 ,获取到的三方uID")
	public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @ApiModelProperty(value = "三方登录如果为微信登录需传这个参数")
    public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@ApiModelProperty(value = "三方登录类型 QQ,WEIXIN")
	public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }
    
    @ApiModelProperty(value = "三方登录获取到的三方头像")
	public String getsHeadImg() {
		return sHeadImg;
	}

	public void setsHeadImg(String sHeadImg) {
		this.sHeadImg = sHeadImg;
	}
	@ApiModelProperty(value = "三方登录获取到的三方昵称")
	public String getsNickName() {
		return sNickName;
	}

	public void setsNickName(String sNickName) {
		this.sNickName = sNickName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
