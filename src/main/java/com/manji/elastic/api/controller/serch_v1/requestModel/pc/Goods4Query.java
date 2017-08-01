package com.manji.elastic.api.controller.serch_v1.requestModel.pc;

public class Goods4Query extends GoodsQuery{

	
	
	
	private String sPrice;//开始价格
	private String ePrice;//解说价格

	private int all_f =1;  //是否综合排序，0不综合排序，1综合排序 
	private int price_f =0;//是否价格排序， 0，不排序，1.升序 2.降序
	private int count_f =0;//是否销量排序     0.是否排序。1.升序 2.降序
	
	private int  shipping =0;//是否包邮
//	private String discount;//是否折扣

	public String getsPrice() {
		return sPrice;
	}

	public void setsPrice(String sPrice) {
		this.sPrice = sPrice;
	}

	public String getePrice() {
		return ePrice;
	}

	public void setePrice(String ePrice) {
		this.ePrice = ePrice;
	}

	public int getAll_f() {
		return all_f;
	}

	public void setAll_f(int all_f) {
		this.all_f = all_f;
	}

	public int getPrice_f() {
		return price_f;
	}

	public void setPrice_f(int price_f) {
		this.price_f = price_f;
	}

	public int getCount_f() {
		return count_f;
	}

	public void setCount_f(int count_f) {
		this.count_f = count_f;
	}

	public int getShipping() {
		return shipping;
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	
	
}
