package com.manji.elastic.api.controller.serch_v2.serchmodel;

import java.util.Map;

public class Range {
	private Map<String,GtLt> range;

	public Map<String, GtLt> getRange() {
		return range;
	}

	public void setRange(Map<String, GtLt> range) {
		this.range = range;
	}
}
