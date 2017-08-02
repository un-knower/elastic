package com.manji.elastic.api.controller.serch_v2.serchmodel;

import java.util.Map;

public class Range {
	private Map<String,GtLt> s;

	public Map<String, GtLt> getS() {
		return s;
	}

	public void setS(Map<String, GtLt> s) {
		this.s = s;
	}
	
}
