package com.manji.elastic.api.commom.utils;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class KeySerchBuider {
	
	/**
	 * 纯中文搜索
	 * @param key
	 * @param value
	 * @return
	 */
	public static QueryBuilder getChniseBulider(String key,String value){
		DisMaxQueryBuilder  disMaxQueryBuilder=QueryBuilders.disMaxQuery();
		//以关键字开头(优先级最高)
		MatchQueryBuilder q1=QueryBuilders.matchQuery(key,value).boost(5);
		//完整包含经过分析过的关键字
		QueryBuilder q2=QueryBuilders.matchQuery(key+".IKS", value).minimumShouldMatch("100%");
		disMaxQueryBuilder.add(q1);
		disMaxQueryBuilder.add(q2);
		
		return disMaxQueryBuilder;
	}
}
