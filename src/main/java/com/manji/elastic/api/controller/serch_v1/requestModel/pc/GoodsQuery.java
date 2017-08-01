package com.manji.elastic.api.controller.serch_v1.requestModel.pc;

public class GoodsQuery {

	
	
	
	
	
	private String q=""; //查询字段
	private String c="";//类型
	private int s =20; //每页条数
	private int p;//当前页数
	
	
	
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}
	
	
	
}
