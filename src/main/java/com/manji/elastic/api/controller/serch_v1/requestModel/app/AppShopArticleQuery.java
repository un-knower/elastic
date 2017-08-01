package com.manji.elastic.api.controller.serch_v1.requestModel.app;

public class AppShopArticleQuery {
	// 基础参数
	private String queryStr = ""; // 查询字段
	private int size = 20; // 每页条数
	private int pageNum = 1;// 当前页数
	private int shop_id =0;//商家ID
	// 筛选条件参数
	private String shop_cate_id = "";// 商家自定義分类ID
	private String act_flag="";//活动类型，1、折扣2、满折3、满减劵4、满额返劵5、指定赠6、买赠7、满赠8、包邮
	// 排序方式
	private int sort_flag = 0;// 0默认综合排序，1，销量排序，2，按照价格降序排序，3，按照价格升序排序，4，上架时间降序
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

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public String getShop_cate_id() {
		return shop_cate_id;
	}

	public void setShop_cate_id(String shop_cate_id) {
		this.shop_cate_id = shop_cate_id;
	}



	public String getAct_flag() {
		return act_flag;
	}

	public void setAct_flag(String act_flag) {
		this.act_flag = act_flag;
	}

	public int getSort_flag() {
		return sort_flag;
	}

	public void setSort_flag(int sort_flag) {
		this.sort_flag = sort_flag;
	}
	
	
	
	
}
