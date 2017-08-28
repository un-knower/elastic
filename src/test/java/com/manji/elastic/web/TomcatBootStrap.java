package com.manji.elastic.web;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.api.commom.utils.KeySerchBuider;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.tomcat.TomcatBootstrapHelper;

public class TomcatBootStrap {
	public static Client client132 = null;
	
	public static Client client49 = null;
	public static void main(String[] args) throws Exception {
		new TomcatBootstrapHelper(8097, false, "dev2").start();
		/*try {
		
			Settings settings = Settings.builder().put("cluster.name", "my-application")
                .put("xpack.security.transport.ssl.enabled", false)
                .put("xpack.security.user", "elastic:changeme")
                .put("client.transport.sniff", true).build();
		
			client132 = new PreBuiltXPackTransportClient(settings)
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("113.31.84.132"), 9300));
			
			
			// 设置集群名称
			Settings settings49 = Settings.builder().put("cluster.name", "mj-es").build();
			// 创建client
			client49 = new PreBuiltTransportClient(settings49)
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName("192.168.0.49"), 9333));
						
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			qb1.must(KeySerchBuider.getChniseBulider("article_title", ""));
			
			//创建搜索条件
			SearchRequestBuilder requestBuider = client49.prepareSearch("article");
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			//requestBuider.setQuery(qb1);
			
			requestBuider.setFrom((1 - 1) * 10000).setSize(10000);
			
			System.out.println("参数json:{}" + requestBuider.toString());
			
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//System.out.println("结果:" + JSON.toJSONString(hits).toString());
			
			//录入到 132
			int i = 0;
			for (SearchHit searchHit : hits) {
				i++;
				IndexRequestBuilder requestBuilder = client132.prepareIndex("article_v2", "info", null);//设置索引名称，索引类型，id  
				requestBuilder.setSource(JSON.toJSONString(searchHit.getSource()),XContentType.JSON).execute().actionGet();//创建索引  
				System.out.println(i);
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
	}
}