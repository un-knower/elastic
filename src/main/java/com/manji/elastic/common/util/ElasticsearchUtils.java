package com.manji.elastic.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.manji.elastic.common.global.Configure;

public class ElasticsearchUtils {
	public static TransportClient  client = null;
	
	@SuppressWarnings("resource")
	public ElasticsearchUtils() throws UnknownHostException{
		
		if(null == client){
			 // 设置集群名称
	        Settings settings = Settings.builder()
	                .put("cluster.name", "mj-es").build();
	        // 创建client
	        client = new PreBuiltTransportClient(settings)
	                .addTransportAddress(new InetSocketTransportAddress(
	        InetAddress.getByName(Configure.getEsLocation()), Integer.valueOf(Configure.getEStrans_PORT())));
	        System.out.println("连接成功。。。。。。。。。。");
		}
	}
	
	/** 
     * 创建索引 
     * @param indexName 索引名称，相当于数据库名称 
     * @param typeName 索引类型，相当于数据库中的表名 
     * @param id id名称，相当于每个表中某一行记录的标识 
     * @param jsonData json数据 
     */  
    public void createIndex(String indexName, String typeName, String id,  
            String jsonData) {  
        IndexRequestBuilder requestBuilder = client.prepareIndex(indexName,  
            typeName, id);//设置索引名称，索引类型，id  
        requestBuilder.setSource(jsonData).execute().actionGet();//创建索引  
    }  
    
    /** 
     * 执行搜索 
     * @param indexname 索引名称 
     * @param type 索引类型 
     * @param queryBuilder 查询条件 
     * @return 
     */  
    public SearchResponse searcher(String indexName, String typeName,  
            QueryBuilder queryBuilder) {  
        SearchResponse searchResponse = client.prepareSearch(indexName)  
                .setTypes(typeName).setQuery(queryBuilder).execute()  
                .actionGet();//执行查询  
        return searchResponse;  
    }  
    
    /** 
     * 更新索引 
     * @param indexName 索引名称 
     * @param typeName 索引类型 
     * @param id id名称 
     * @param jsonData json数据 
     */  
    public void updateIndex(String indexName, String typeName, String id,  
            String jsonData) {  
        UpdateRequest updateRequest = new UpdateRequest();  
        updateRequest.index(indexName);//设置索引名称  
        updateRequest.id(id);//设置id  
        updateRequest.type(typeName);//设置索引类型  
        updateRequest.doc(jsonData);//更新数据  
        client.update(updateRequest).actionGet();//执行更新  
    }  

    /** 
     * 删除索引 
     * @param indexName 
     * @param typeName 
     * @param id 
     */  
    public void deleteIndex(String indexName, String typeName, String id) {  
        client.prepareDelete(indexName, typeName, id).get();  
    }  
    
    
    
}
