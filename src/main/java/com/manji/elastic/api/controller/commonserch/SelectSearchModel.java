package com.manji.elastic.api.controller.commonserch;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SelectSearchModel {
	private String content; //关键词内容
	
	private String indexType; //索引类型 shop   commodity
	
	private String device; //设备类型，PC   APP 

	@ApiModelProperty(value = "关键字")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	@ApiModelProperty(value = "索引类型 shop商家，commodity商品")
	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType == null ? null : indexType.trim();
	}
	
	@ApiModelProperty(value = "设备类型，PC   APP ,必传")
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device == null ? null : device.trim();
	}
}
