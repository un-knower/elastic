package com.manji.elastic.api.commom;

import com.wordnik.swagger.annotations.ApiModelProperty;
/**
 * 搜索基类
 * @author Administrator
 *
 */
public class BaseSerchModel {
	private String queryStr=""; //关键字查询字段
	private String articleName="";//商品名查询字段
	private String shopName="";//商家名查询字段
	private Integer size =20; //每页条数
	private Integer pageNum =1;//当前页数
	
	@ApiModelProperty(value = "关键字内容")
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	@ApiModelProperty(value = "商品名查询字段")
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	@ApiModelProperty(value = "商家名查询字段")
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
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
