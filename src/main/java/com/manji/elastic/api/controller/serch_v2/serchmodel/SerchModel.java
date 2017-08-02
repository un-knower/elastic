package com.manji.elastic.api.controller.serch_v2.serchmodel;

import java.util.Map;

public class SerchModel {
	private int size = 20;
	
	private int from = 0;
	
	private Map<String,String> sort;
	
	private Query query;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public Map<String, String> getSort() {
		return sort;
	}

	public void setSort(Map<String, String> sort) {
		this.sort = sort;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
	
}
