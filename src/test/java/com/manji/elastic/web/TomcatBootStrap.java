package com.manji.elastic.web;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;

import com.manji.elastic.common.tomcat.TomcatBootstrapHelper;

public class TomcatBootStrap {
	public static void main(String[] args) throws Exception {
		//new TomcatBootstrapHelper(8097, false, "dev").start();
		 /* 创建客户端 */
        // client startup
        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.30"), 9200));
        SearchResponse response = client.prepareSearch("article", "shop")
                .setTypes("_search", "_search")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();
        
        System.out.println( response.toString());
       
	}
}