package com.manji.elastic.web;

import java.net.InetAddress;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.alibaba.fastjson.JSON;

public class Testeee {
	
    public static void main(String[] args) {
        try {
            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", "mj-es").build();
            //创建client
            TransportClient client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.30"), 9300));
            
            
            String jsonData = "{" + "\"name\":\"第一个\","  
                    + "\"birth\":\"1995-01-30\"," + "\"email\":\"fdfds@163.com\""  
                    + "}";//json数据  
            //录入
            IndexRequestBuilder requestBuilder = client.prepareIndex("blog",  
                    "article", "111");//设置索引名称，索引类型，id  
                requestBuilder.setSource(jsonData).execute().actionGet();//创建索引  
            
            
           
             
            
            
            
            SearchResponse searchResponse = client.prepareSearch("blog")
                    .setTypes("article")
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.termQuery("name", "kimchy"))    
                    .setQuery(QueryBuilders.termQuery("email", "kimrwechy"))  // Query
                    .setPostFilter(QueryBuilders.rangeQuery("birth").from("1995-01-03").to("1998-01-30")) // Filter
                    .setFrom(0).setSize(60).setExplain(true)
                    .get();
            
            SearchHits hits = searchResponse.getHits();  
            SearchHit[] searchHits = hits.getHits();  
            
            
            System.out.println("查询结果：" + JSON.toJSONString(searchHits));
            
            
            
            //搜索数据
            GetResponse response1 = client.prepareGet("blog", "article", "111").execute().actionGet();
            
            //
            //输出结果
            System.out.println(response1.getSourceAsString());
            //关闭client
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
