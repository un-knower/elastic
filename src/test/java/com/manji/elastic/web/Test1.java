package com.manji.elastic.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.biz.helper.ElasticsearchClientUtils;
import com.manji.elastic.common.exception.NotFoundException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.util.Pinyin4j;

public class Test1 {
	
	public static void main(String[] args) {
		Test1 ss = new Test1();
		try {
			ss.chineseSearch("母乳保鲜储奶袋", null);
			//ss.chineseWithEnglishOrPinyinSearch("母乳保鲜储奶袋", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * 纯中文搜索
     * @return*/
	public void chineseSearch(String key,Integer cityId) throws Exception{
		//连接服务端
		TransportClient  client = ElasticsearchClientUtils.getTranClinet();
		
		BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
		//商家分类
		//qb1.must(QueryBuilders.matchQuery("class_list","4461"));
		
		
		DisMaxQueryBuilder  disMaxQueryBuilder=QueryBuilders.disMaxQuery();
		//以关键字开头(优先级最高)
		MatchQueryBuilder q1=QueryBuilders.matchQuery("article_category_index",key).boost(5);
		//完整包含经过分析过的关键字
		//boolean  whitespace=key.contains(" ");
		//int slop=whitespace?50:5;
		QueryBuilder q2=QueryBuilders.matchQuery("article_category_index.ik", key).minimumShouldMatch("100%");
		disMaxQueryBuilder.add(q1);
		disMaxQueryBuilder.add(q2);
		
		qb1.must(disMaxQueryBuilder);
		//创建搜索条件
		SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
		requestBuider.setTypes("info");
		requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
		requestBuider.setQuery(qb1);
		requestBuider.setFrom(0).setSize(20);
		System.out.println("参数json:"+requestBuider.toString());
		//执行查询结果
		SearchResponse searchResponse = requestBuider.get();
		SearchHits hits = searchResponse.getHits();
		System.out.println("结果：" + JSON.toJSONString(hits).toString());
    }
	
	/**
	 * 混合搜索
	* @return*/
	public void chineseWithEnglishOrPinyinSearch(String key,Integer cityId) throws Exception{
		//连接服务端
		TransportClient  client = ElasticsearchClientUtils.getTranClinet();
		
		DisMaxQueryBuilder  disMaxQueryBuilder=QueryBuilders.disMaxQuery();
		//是否有中文开头，有则返回中文前缀
		String startChineseString=Pinyin4j.getStartChineseString(key);
		/**
		 * 源值搜索，不做拼音转换    
		 * 权重* 1.5
		 */        
		QueryBuilder normSearchBuilder=QueryBuilders.matchQuery("article_title",key).boost(5f);
		/**
		 * 拼音简写搜索
		 * 1、分析key，转换为简写  case:  南京东路==>njdl，南京dl==>njdl，njdl==>njdl
		 * 2、搜索匹配，必须完整匹配简写词干
		 * 3、如果有中文前缀，则排序优先
		 * 权重*1
		 */
		//String analysisKey=commonSearchService.anaysisKeyAndGetMaxWords(SearchIndex.INDEX_NAME_SEARCHWORDSSTATISTICS,key,"pinyiSimpleSearchAnalyzer");
		String analysisKey = Pinyin4j.getFirstSpell(key);
		QueryBuilder pingYinSampleQueryBuilder=QueryBuilders.termQuery("article_title.pinyin", analysisKey);
		/**
		 * 拼音简写包含匹配，如 njdl可以查出 "城市公牛 南京东路店"，虽然非南京东路开头
		 * 权重*0.8
		 */
		QueryBuilder  pingYinSampleContainQueryBuilder=null;
		if(analysisKey.length()>1){
			pingYinSampleContainQueryBuilder=QueryBuilders.wildcardQuery("article_title.pinyin", "*"+analysisKey+"*").boost(0.8f);
		}

		/**
		 * 拼音全拼搜索
		 * 1、分析key，获取拼音词干   case :  南京东路==>[nan,jing,dong,lu]，南京donglu==>[nan,jing,dong,lu]
		 * 2、搜索查询，必须匹配所有拼音词，如南京东路，则nan,jing,dong,lu四个词干必须完全匹配
		 * 3、如果有中文前缀，则排序优先  
		 * 权重*1
		 */
		QueryBuilder pingYinFullQueryBuilder=null;
		if(key.length()>1){
			pingYinFullQueryBuilder=QueryBuilders.matchPhraseQuery("article_title.pinyin", key);    
		}

		/**
		 * 完整包含关键字查询(优先级最低，只有以上四种方式查询无结果时才考虑）
		 * 权重*0.8
		 */
		QueryBuilder containSearchBuilder=QueryBuilders.matchQuery("article_title.ik", key).minimumShouldMatch("100%");
		disMaxQueryBuilder
		.add(normSearchBuilder)
		.add(pingYinSampleQueryBuilder)    
		.add(containSearchBuilder);
		//以下两个对性能有一定的影响，故作此判定，单个字符不执行此类搜索
		if(pingYinFullQueryBuilder!=null){
			disMaxQueryBuilder.add(pingYinFullQueryBuilder);
		}
		if(pingYinSampleContainQueryBuilder!=null){
			disMaxQueryBuilder.add(pingYinSampleContainQueryBuilder);
		}
		QueryBuilder queryBuilder=disMaxQueryBuilder;
		//关键如果有中文，则必须包含在内容中
		if(StringUtils.isNotBlank(startChineseString)){
			queryBuilder=QueryBuilders.filteredQuery(disMaxQueryBuilder,
					FilterBuilders.queryFilter(QueryBuilders.queryStringQuery("*"+startChineseString+"*").field("article_category_index").analyzer("ngramSearchAnalyzer")));
			
			queryBuilder=QueryBuilders.functionScoreQuery(queryBuilder)
					.add(FilterBuilders.queryFilter(QueryBuilders.matchQuery("article_category_index",startChineseString).analyzer("ngramSearchAnalyzer")), ScoreFunctionBuilders.weightFactorFunction(1.5f));
		}
		//创建搜索条件
		SearchRequestBuilder requestBuider = client.prepareSearch("test");
		requestBuider.setTypes("info");
		requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
		requestBuider.setQuery(queryBuilder);
		requestBuider.setFrom(0).setSize(20);
		System.out.println("参数json:"+requestBuider.toString());
		//执行查询结果
		SearchResponse searchResponse = requestBuider.get();
		SearchHits hits = searchResponse.getHits();
		System.out.println("结果：" + JSON.toJSONString(hits).toString());
	}
}
