package com.manji.elastic.api.commom;

import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 搜索基类
 * @author Administrator
 *
 */
public class BaseSerchModel {
	private String queryStr=""; //查询字段
	private Integer size =20; //每页条数
	private Integer pageNum =1;//当前页数
	
	@ApiModelProperty(value = "关键字内容")
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	@ApiModelProperty(value = "每页查询条数，默认20")
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	@ApiModelProperty(value = "当前页，默认1")
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
}
