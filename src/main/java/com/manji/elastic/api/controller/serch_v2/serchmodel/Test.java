package com.manji.elastic.api.controller.serch_v2.serchmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public class Test {
	public static void main(String[] args) {
		SerchModel mm  =new SerchModel();
		mm.setSize(20);
		mm.setFrom(2);
		
		//Query Bool
		Query query = new Query();
		Bool bool = new Bool();
		
		//must 参数不为空时相应字段and
		Map<String,String> mustpartemMap = new HashMap<String,String>();
		mustpartemMap.put("article_category_index", "");
		mustpartemMap.put("article_index", "");
		mustpartemMap.put("342r", "");
		List<Match> musts = setAndMust(mustpartemMap);
		bool.setMust(musts);
		
		//filter
		List<Range> filter = new ArrayList<Range>();
		Range range = new Range();
		Map<String,GtLt> rangeMap = new HashMap<String,GtLt>();
		GtLt gtlt = new GtLt();
		gtlt.setGte("0");
		gtlt.setLte("999");
		rangeMap.put("article_sell_price", gtlt);
		range.setRange(rangeMap);
		filter.add(range);
		bool.setFilter(filter);
		
		query.setBool(bool);
		mm.setQuery(query);
		
		//排序
		int a = 1;
		Map<String,String> shortMap = new HashMap<String,String>();
		switch (a) {
		case 1:
			shortMap.put("article_order_times", "desc");
			break;
		case 2:
			shortMap.put("article_sell_price", "asc");
			break;
		case 3:
			shortMap.put("article_sell_price", "desc");
			break;
		default:
			shortMap.put("article_review_score", "desc");
			break;
		}
		mm.setSort(shortMap);
		
		System.out.println(JSON.toJSONString(mm));
	}
	
	public static List<Match> setAndMust(Map<String,String> partemMap){
		List<Match> must = new ArrayList<Match>();
		for (Map.Entry<String,String> entry : partemMap.entrySet()) {  
		  
			if(StringUtils.isNotBlank(entry.getValue())){
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				Match m = new Match();
				Map<String,String> abc = new HashMap<String,String>();
				abc.put(entry.getKey(), entry.getValue());
				m.setMatch(abc);
				must.add(m);
			}
		}
		if(must.size() == 0){
			return null;
		}
		return must;
	}
}
