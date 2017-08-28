package com.manji.elastic.api.controller.base;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;

import javax.servlet.http.HttpServletRequest;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.api.commom.utils.KeySerchBuider;
import com.manji.elastic.biz.helper.ElasticsearchClientUtils;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.result.BaseResult;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 二期搜索
 * @author Administrator
 *
 */
@Controller
@Api(value = "/api-base", description = "base")
@RequestMapping("/api/base/")
public class BaseApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public static Client client132 = null;
	
	public static Client client49 = null;
	
	
	/**
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	@RequestMapping(value="/startClient", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseResult startClient(HttpServletRequest req){
		BaseResult baseResult=new BaseResult(CodeEnum.SUCCESS.getCode(),"成功");
		try{
			ElasticsearchClientUtils.startClient();
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseResult(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseResult(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
	/**
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	@RequestMapping(value="/stopClient", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseResult stopClient(HttpServletRequest req){
		BaseResult baseResult=new BaseResult(CodeEnum.SUCCESS.getCode(),"成功");
		try{
			ElasticsearchClientUtils.stopClient();
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseResult(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseResult(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
	
	
	
	
	
	/**
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "", notes = "")
	@RequestMapping(value="/abc", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseResult abc(HttpServletRequest req,@RequestParam(required = false) Integer pageNum){
		BaseResult baseResult=new BaseResult(CodeEnum.SUCCESS.getCode(),"成功");
		try{
			
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
				
				requestBuider.setFrom((pageNum - 1) * 10000).setSize(10000);
				
				System.out.println("参数json:{}" + requestBuider.toString());
				
				//执行查询结果
				SearchResponse searchResponse = requestBuider.get();
				SearchHits hits = searchResponse.getHits();
				System.out.println("结果:" + JSON.toJSONString(hits).toString());
				
				//录入到 132
				for (SearchHit searchHit : hits) {
				
					IndexRequestBuilder requestBuilder = client132.prepareIndex("article", "info", null);//设置索引名称，索引类型，id  
					requestBuilder.setSource(JSON.toJSONString(searchHit.getSource()),XContentType.JSON).execute().actionGet();//创建索引  
				}
			
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseResult(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseResult(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
}
