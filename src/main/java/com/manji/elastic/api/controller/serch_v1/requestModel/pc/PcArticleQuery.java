package com.manji.elastic.api.controller.serch_v1.requestModel.pc;

public class PcArticleQuery {
	
	//基础参数
	private String queryStr=""; //查询字段
	private int size =20; //每页条数
	private int pageNum =1;//当前页数
	
	//筛选条件
	private String cate_id="";//类型
	private int ship_flag =0;//是否包邮，默认为0，1为包邮
	private int price_start =0; //筛选初始金额
	private int price_end  ;//筛选结束金额 ，默认不传。
	private int sale_flag =0;//折扣
	private String area_code=""; //区域码。。
	//排序方式
	private int sort_flag=0;//排序方式 0默认综合排序，1销量排序，2价格升序，3价格降序。
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	public int getShip_flag() {
		return ship_flag;
	}
	public void setShip_flag(int ship_flag) {
		this.ship_flag = ship_flag;
	}
	public int getPrice_start() {
		return price_start;
	}
	public void setPrice_start(int price_start) {
		this.price_start = price_start;
	}
	public int getPrice_end() {
		return price_end;
	}
	public void setPrice_end(int price_end) {
		this.price_end = price_end;
	}
	public int getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(int sort_flag) {
		this.sort_flag = sort_flag;
	}
	public int getSale_flag() {
		return sale_flag;
	}
	public void setSale_flag(int sale_flag) {
		this.sale_flag = sale_flag;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	
	
	

}
