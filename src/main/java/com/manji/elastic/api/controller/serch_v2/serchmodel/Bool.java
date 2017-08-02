package com.manji.elastic.api.controller.serch_v2.serchmodel;

import java.util.List;

public class Bool {
	private List<Match> must;
	
	private List<Range> filter;

	public List<Match> getMust() {
		return must;
	}

	public void setMust(List<Match> must) {
		this.must = must;
	}

	public List<Range> getFilter() {
		return filter;
	}

	public void setFilter(List<Range> filter) {
		this.filter = filter;
	}
}
