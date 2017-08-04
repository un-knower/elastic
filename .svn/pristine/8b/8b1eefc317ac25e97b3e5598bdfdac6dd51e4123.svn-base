package com.manji.elastic.api.controller.serch_v1.requestModel.app;

public class AppShopQuery {

	//基础属性参数
	private String queryStr ="";			//商家查询关键字
	private int size =20; 					//每页条数
	private int pageNum =1;					//当前页数
	
	//附近接口需要传递参数
	private String location ="";			//经纬度  格式   "lat,lon"
	private int distance_max =0 ;			//附近距离 单位为米,默认1000m
	
	//筛选类参数
	private String cate_id ="";				//类型ID，
	private String busy_id ="";             //主营分类ID
	private String area_code ="";			//区域码
//	private int shop_id =0;					//商家ID
	private int sign_flag =0;               //是否签约 0 不限制，1签约商家
//	private String rec_area_code ="";       //推荐地区参数
	private int open_flag =0;				//商家开店状态 默认未0 ，不检查该值，传值为1，则只搜索开启的商家；
	//排序方式
	private int sort_flag =0;				//排序方式  默认 0距离，1 评分 review_score
	
	
	
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	public int getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(int sort_flag) {
		this.sort_flag = sort_flag;
	}
	public int getDistance_max() {
		return distance_max;
	}
	public void setDistance_max(int distance_max) {
		this.distance_max = distance_max;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
//	public int getShop_id() {
//		return shop_id;
//	}
//	public void setShop_id(int shop_id) {
//		this.shop_id = shop_id;
//	}
	public String getBusy_id() {
		return busy_id;
	}
	public void setBusy_id(String busy_id) {
		this.busy_id = busy_id;
	}
	public int getSign_flag() {
		return sign_flag;
	}
	public void setSign_flag(int sign_flag) {
		this.sign_flag = sign_flag;
	}
//	public String getRec_area_code() {
//		return rec_area_code;
//	}
//	public void setRec_area_code(String rec_area_code) {
//		this.rec_area_code = rec_area_code;
//	}
	public int getOpen_flag() {
		return open_flag;
	}
	public void setOpen_flag(int open_flag) {
		this.open_flag = open_flag;
	}
	
	
	
	
}
