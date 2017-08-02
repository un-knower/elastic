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
		
		//Query
		Query query = new Query();
		Bool bool = new Bool();
		
		//must
		Map<String,String> mustpartemMap = new HashMap<String,String>();
		mustpartemMap.put("article_category_index", "你好啊");
		mustpartemMap.put("article_index", "我不后啊");
		mustpartemMap.put("342r", "");
		List<Match> musts = setAndMust(mustpartemMap);
		if(musts.size() > 0){
			bool.setMust(musts);
		}
		
		//filter
		
		
		query.setBool(bool);
		mm.setQuery(query);
		
		//排序
		Map<String,String> shortMap = new HashMap<String,String>();
		shortMap.put("article_review_score", "desc");
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
