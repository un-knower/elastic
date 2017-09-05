package com.manji.elastic.api.controller.hotsearch;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.biz.helper.ElasticsearchClientUtils;
import com.manji.elastic.common.global.Configure;

/**
 * 新增热搜词BIZ
 * @author Mr.ShyMe
 *
 */
public class HotSearchAddBiz {
	
	public static void addHotSearchWords(RecordModel model) {
		try {
			//完全匹配，判断是否存在，如果不存在，才存入
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字处理
			qb1.must(QueryBuilders.matchPhraseQuery("content", model.getContent().trim()));
			qb1.must(QueryBuilders.matchPhraseQuery("device", model.getDevice().trim()));
			qb1.must(QueryBuilders.matchPhraseQuery("indexType", model.getIndexType().trim()));
			//创建连接
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_IndexHotSearchWords());
			requestBuider.setTypes("info");
			requestBuider.setQuery(qb1);
			SearchResponse res = requestBuider.get();
			SearchHits hits = res.getHits();
			long total =hits.getTotalHits();
			if(total > 0) {
				//获取count的值
				Integer count = (Integer) hits.getHits()[0].getSource().get("count");
				model.setCount(count + 1);
				//获取id
				String _id = hits.getHits()[0].getId();
				//连接服务端录入
				IndexRequestBuilder requestBuilder = client.prepareIndex(Configure.getES_IndexHotSearchWords(), "info", _id);//设置索引名称，索引类型，id  
				requestBuilder.setSource(JSON.toJSONString(model),XContentType.JSON).execute().actionGet();//创建索引  
			}
			if(total == 0) {
				//连接服务端录入
				IndexRequestBuilder requestBuilder = client.prepareIndex(Configure.getES_IndexHotSearchWords(), "info", null);//设置索引名称，索引类型，id  
				requestBuilder.setSource(JSON.toJSONString(model),XContentType.JSON).execute().actionGet();//创建索引  
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
