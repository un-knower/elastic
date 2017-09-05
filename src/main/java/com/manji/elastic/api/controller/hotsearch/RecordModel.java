package com.manji.elastic.api.controller.hotsearch;

public class RecordModel {
	private String content; //关键词内容
	
	private String indexType; //索引类型 shop   commodity
	
	private String device; //设备类型，PC   APP 
	
	private Integer count = 1; //收录次数

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType == null ? null : indexType.trim();
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device == null ? null : device.trim();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
