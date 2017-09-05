package com.manji.elastic.api.commom.utils;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class KeySerchBuider {
	
	/**
	 * 关键字搜索
	 * @param key
	 * @param value
	 * @return
	 */
	public static QueryBuilder getChniseBulider(String key,String value){
		//QueryBuilder q2=QueryBuilders.matchQuery(key+"", value).prefixLength(1).fuzziness(0);
		QueryBuilder q2=QueryBuilders.multiMatchQuery(value, key).type("most_fields");
		return q2;
	}
}
