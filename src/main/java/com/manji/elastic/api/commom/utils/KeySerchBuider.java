package com.manji.elastic.api.commom.utils;

import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
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
		/*QueryBuilder q2=QueryBuilders.matchQuery(key+"", value).prefixLength(1).fuzziness(0);
		return q2;*/
		
		 DisMaxQueryBuilder  disMaxQueryBuilder=QueryBuilders.disMaxQuery();
	        //以关键字开头(优先级最高)
	        MatchQueryBuilder q1=QueryBuilders.matchQuery(key+"",key).analyzer("ngramSearchAnalyzer").boost(5);        
	        //完整包含经过分析过的关键字
//	         boolean  whitespace=key.contains(" ");
//	         int slop=whitespace?50:5;
	        QueryBuilder q2=QueryBuilders.matchQuery(key, key).analyzer("ik").minimumShouldMatch("100%");
	        disMaxQueryBuilder.add(q1);
	        disMaxQueryBuilder.add(q2);
	        
	        return disMaxQueryBuilder;
	        
	}
	/**
	 * 混合搜索
	* @return*/
	/*public static QueryBuilder chineseWithEnglishOrPinyinSearch(String key,String value) throws Exception{
		DisMaxQueryBuilder  disMaxQueryBuilder=QueryBuilders.disMaxQuery();
		//是否有中文开头，有则返回中文前缀
		String startChineseString=Pinyin4j.getStartChineseString(value);
		*//**
		 * 源值搜索，不做拼音转换    
		 * 权重* 1.5
		 *//*        
		QueryBuilder normSearchBuilder=QueryBuilders.matchQuery(key,value).boost(5f);
		*//**
		 * 拼音简写搜索
		 * 1、分析key，转换为简写  case:  南京东路==>njdl，南京dl==>njdl，njdl==>njdl
		 * 2、搜索匹配，必须完整匹配简写词干
		 * 3、如果有中文前缀，则排序优先
		 * 权重*1
		 *//*
		String analysisKey = Pinyin4j.getFirstSpell(value);
		QueryBuilder pingYinSampleQueryBuilder=QueryBuilders.termQuery(key+".ik_smart", analysisKey);
		*//**
		 * 拼音简写包含匹配，如 njdl可以查出 "城市公牛 南京东路店"，虽然非南京东路开头
		 * 权重*0.8
		 *//*
		QueryBuilder  pingYinSampleContainQueryBuilder=null;
		if(analysisKey.length()>1){
			pingYinSampleContainQueryBuilder=QueryBuilders.wildcardQuery(key+".ik_smart", "*"+analysisKey+"*").boost(0.8f);
		}
		*//**
		 * 拼音全拼搜索
		 * 1、分析key，获取拼音词干   case :  南京东路==>[nan,jing,dong,lu]，南京donglu==>[nan,jing,dong,lu]
		 * 2、搜索查询，必须匹配所有拼音词，如南京东路，则nan,jing,dong,lu四个词干必须完全匹配
		 * 3、如果有中文前缀，则排序优先  
		 * 权重*1
		 *//*
		QueryBuilder pingYinFullQueryBuilder=null;
		if(value.length()>1){
			pingYinFullQueryBuilder=QueryBuilders.matchPhraseQuery(key+".ik_smart", value);
		}
		*//**
		 * 完整包含关键字查询(优先级最低，只有以上四种方式查询无结果时才考虑）
		 * 权重*0.8
		 *//*
		QueryBuilder containSearchBuilder=QueryBuilders.matchQuery(key+".ik_smart", value).minimumShouldMatch("100%");
		
		disMaxQueryBuilder.add(normSearchBuilder).add(pingYinSampleQueryBuilder).add(containSearchBuilder);
		
		//以下两个对性能有一定的影响，故作此判定，单个字符不执行此类搜索
		if(pingYinFullQueryBuilder!=null){
			disMaxQueryBuilder.add(pingYinFullQueryBuilder);
		}
		if(pingYinSampleContainQueryBuilder!=null){
			disMaxQueryBuilder.add(pingYinSampleContainQueryBuilder);
		}
		//关键如果有中文，则必须包含在内容中
		if(StringUtils.isNotBlank(startChineseString)){
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			queryBuilder.must(disMaxQueryBuilder).filter(QueryBuilders.queryStringQuery("*"+startChineseString+"*").field(key));
			return queryBuilder;
		}
		return  disMaxQueryBuilder;
	}*/
}
