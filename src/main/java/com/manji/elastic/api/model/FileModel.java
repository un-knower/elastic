package com.manji.elastic.api.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class FileModel {
	private String path;
	private String fileFormat;
	private Long fileSize;
	@ApiModelProperty(value = "文件地址")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@ApiModelProperty(value = "文件格式：img图片。video视频")
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
}
