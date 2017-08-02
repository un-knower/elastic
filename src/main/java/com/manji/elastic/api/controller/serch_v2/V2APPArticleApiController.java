package com.manji.elastic.api.controller.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppArticleQuery;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppShopArticleQuery;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseObjectResult;
import com.manji.elastic.common.util.ElasticsearchClientUtils;
import com.manji.elastic.common.util.HttpClientUtil;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * 二期接口
 * APP商品
 * @author Administrator
 *
 */
@Controller
@Api(value = "/app-Article/v2", description = "二期接口，APP商品")
@RequestMapping("/app/article/v2/")
public class V2APPArticleApiController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "综合商品查询", notes = "综合商品查询")
	@RequestMapping(value="/queryArticle", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryArticle(HttpServletRequest req, @RequestParam(required = false) Integer size,
		@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) String queryStr,
		@RequestParam(required = false) String cate_id,@RequestParam(required = false) String shop_cate_id,
		@RequestParam(required = false) String brand_code,@RequestParam(required = false) String goods_id,
		@RequestParam(required = false) String area_code,@RequestParam(required = false) Integer sale_flag,
		@RequestParam(required = false) Integer price_start,@RequestParam(required = false) Integer price_end,
		@RequestParam(required = false) Integer shop_id,@RequestParam(required = false) Integer ship_flag,
		@RequestParam(required = false) String dis_area_code,@RequestParam(required = false) Integer sort_flag){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			
			SearchResponse searchResponse = client.prepareSearch("article_v1")
					.setTypes("info")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(
						StringUtils.isBlank(queryStr) ? null : QueryBuilders.termQuery("article_category_index", queryStr)
					)
					.setQuery(
						StringUtils.isBlank(cate_id) ? null : QueryBuilders.termQuery("class_list", cate_id)
					)
					.setQuery(
						ship_flag == null ? null : QueryBuilders.termQuery("is_free", ship_flag)
					)
					.setQuery(
						sale_flag == null ? null : QueryBuilders.termQuery("case_article_activity_type", sale_flag)
					)
					.setQuery(
						shop_id == null ? null : QueryBuilders.termQuery("shop_id", shop_id)
					)
					.setQuery(
						StringUtils.isBlank(area_code) ? null : QueryBuilders.termQuery("article_distribution_area", "1"+area_code)
					)
					.setQuery(
						sale_flag != null ? QueryBuilders.rangeQuery("article_sell_price").gt(price_start).lt(price_end) : QueryBuilders.rangeQuery("article_sell_price").gt(price_start)
					)
					.addSort(SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC))
					.setFrom((pageNum - 1) * size).setSize(size).get();
			
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();  
			String json = searchSourceBuilder.query(QueryBuilders.termQuery("article_category_index", queryStr)).toString();  
			System.out.println(json);

			SearchHits hits = searchResponse.getHits();
			
			System.out.println("结果:" + JSON.toJSONString(hits).toString());
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new BusinessDealException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
			
			/*int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字+分类ID
			if (!"".equals(query.getQueryStr()) && !"".equals(query.getCate_id())) {

				sb.append("{ \"match\": { \"article_category_index\": \"" + query.getQueryStr()
						+ "\" } },{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
			} else {
				if (!"".equals(query.getQueryStr())) {
					sb.append("{ \"match\": { \"article_category_index\": \"" + query.getQueryStr() + "\" } }");
				}
				if (!"".equals(query.getCate_id())) {

					sb.append("{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
				}
			}
			if (query.getShip_flag() != 0) {

				sb.append(",{ \"match\": { \"is_free\": \"" + query.getShip_flag() + "\" } }");
			}
			if(query.getSale_flag() !=0){
				
				sb.append(",{ \"match\": { \"case_article_activity_type\": \""+query.getSale_flag()+"\" } }");
			}
			if(!"".equals(query.getArea_code())){
				
				sb.append(",{ \"match\": { \"article_distribution_area\": \"" + "1 " + query.getArea_code() + "\" } }");
			}
			sb.append("]");
			// 商品价格过滤
			sb.append(",\"filter\": [{ \"range\": { \"article_sell_price\": { \"gte\": \"" + query.getPrice_start() + "\"");
			if (0 != query.getPrice_end()) {
				sb.append(",\"lte\":\"" + query.getPrice_end() + "\" }}}]");
			} else {
				sb.append("}}}]");
			}
			sb.append("}}");
			// 排序方式
			sb.append(",\"sort\": {");
			switch (query.getSort_flag()) {
			case 1:
				sb.append("\"article_order_times\": \"desc\"");
				break;
			case 2:
				sb.append("\"article_sell_price\":\"asc\"");
				break;
			case 3:
				sb.append("\"article_sell_price\":\"desc\"");
				break;
			default:
				sb.append("\"article_review_score\": \"desc\"");
				break;
			}
			sb.append("}");
			// 分页
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			JSONObject jsonObj = JSON.parseObject(esReturn);  
			JSONObject result = (JSONObject) jsonObj.get("hits");
			
	        return result;*/
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
	
	/**
	 * 好货列表
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "好货列表", notes = "好货列表")
	@RequestMapping(value="/satisfiedList", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> satisfiedList(HttpServletRequest req,
			@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			SearchResponse searchResponse = client.prepareSearch("article_v1")
				.setTypes("info")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchAllQuery())
				.addSort(SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC))
				.addSort(SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC))
				.setFrom((pageNum - 1) * size).setSize(size).get();
			SearchHits hits = searchResponse.getHits();
			System.out.println("结果:" + JSON.toJSONString(hits).toString());
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new BusinessDealException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
	/**
	 * 同类推荐
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "同类推荐", notes = "同类推荐")
	@RequestMapping(value="/similarRecommend", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> similarRecommend(HttpServletRequest req,@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum,
			@RequestParam(required = false) String goods_id,@RequestParam(required = false) Integer shop_id){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//查询当前商品是什么分类的
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			
			SearchResponse searchResponse = client.prepareSearch("article_v1")
				.setTypes("info")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("article_category_index", goods_id)).get();
			SearchHits hits = searchResponse.getHits();
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new BusinessDealException("抱歉，没有找到“关键词”的搜索结果");
			}
			//分类ID
			String cate_id = hits.getHits()[0].getSource().get("article_category_id").toString();
			
			//当前分类下的其他商品
			SearchResponse searchResponse1 = client.prepareSearch("article_v1")
					.setTypes("info")
					.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					.setQuery(QueryBuilders.termQuery("shop_id", shop_id))
					.setQuery(QueryBuilders.termQuery("article_category_id", cate_id))
					.addSort(SortBuilders.fieldSort("article_add_time").order(SortOrder.DESC))
					.setFrom((pageNum - 1) * size).setSize(size).get();
			SearchHits hits1 = searchResponse1.getHits();
			if(null == hits1 || hits1.getHits() == null || hits1.getHits().length == 0){
				throw new BusinessDealException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits1);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
	/**
	 * 商家类查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家类查询", notes = "商家类查询")
	@RequestMapping(value="/articleOfShop", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object articleOfShop(HttpServletRequest req,@RequestBody AppArticleQuery query){
		try{
			int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			boolean query_flag = false;
			// 关键字+分类ID
			if (!"".equals(query.getQueryStr()) && !"".equals(query.getCate_id())) {
				sb.append("{ \"match\": { \"shop_name\": \"" + query.getQueryStr()
						+ "\" } },{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
				query_flag = true;
			} else {
				if (!"".equals(query.getQueryStr())) {
					sb.append("{ \"match\": { \"shop_name\": \"" + query.getQueryStr() + "\" } }");
				}
				if (!"".equals(query.getCate_id())) {
					sb.append("{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
				}
				query_flag = true;
			}
			// 商家分类
			if ("" != query.getShop_cate_id()) {
				if (query_flag) {
					sb.append(",");
				}
				sb.append("{\"match\": { \"scope_values\": \"" + query.getShop_cate_id() + "\" } }");
			}
			// 是否包邮逻辑段
			if (query.getShip_flag() == 1) {
				sb.append(",{ \"match\": { \"is_free\": \"" + query.getShip_flag() + "\" } }");
				if ("".equals(query.getDis_area_code())) {
					sb.append(",{ \"match\": { \"article_freeshipping_area\": \"" + "1" + "\" } }");
				} else {
					sb.append(",{ \"match\": { \"article_freeshipping_area\": \"" + "1 " + query.getDis_area_code()
							+ "\" } }");
				}
				// sb.append(",{ \"match\": { \"article_freeshipping_area\": \"" +
				// query.getDis_area_code() + "\" } }");
			} else if (!"".equals(query.getDis_area_code())) {
				sb.append(",{ \"match\": { \"article_distribution_area\": \"" + "1 " + query.getDis_area_code() + "\" } }");
			}
			// 折扣
			if (query.getSale_flag() != 0) {
				sb.append(",{ \"match\": { \"article_activity_type\": \"" + "1 2 3 4 5 6 7 8" + "\" } }");
			}
			// 商家区域
			if (!"".equals(query.getArea_code())) {
				sb.append(",{ \"match\": { \"left_shop_send_area\": \"" + query.getArea_code() + "\" } }");
			}
			// 品牌ID
			if ("" != query.getBrand_code()) {
				sb.append(",{ \"match\": { \"article_brand_id\": \"" + query.getBrand_code() + "\" } }");
			}
			sb.append("]");
			// 商品价格过滤
			sb.append(",\"filter\": [{ \"range\": { \"article_sell_price\": { \"gte\": \"" + query.getPrice_start() + "\"");
			if (0 != query.getPrice_end()) {
				sb.append(",\"lte\":\"" + query.getPrice_end() + "\" }}}]");
			} else {
				sb.append("}}}]");
			}
			sb.append("}}");
			sb.append(
					",\"collapse\":{\"field\":\"shop_id\",\"inner_hits\":{\"name\":\"top_rated\",\"size\":2,\"sort\":[{\"article_review_score\":\"desc\"}]}}");
			if (0!=query.getSort_flag()) {
			// 排序方式
			sb.append(",\"sort\": {");
			switch (query.getSort_flag()) {
			case 1:
				sb.append("\"article_order_times\": \"desc\"");
				break;
			case 2:
				sb.append("\"article_sell_price\":\"desc\"");
				break;
			case 3:
				sb.append("\"article_sell_price\":\"asc\"");
				break;
			case 4:
				sb.append("\"shop_review_score\":\"desc\"");
				break;
			case 5:
				sb.append("\"shop_order_times\":\"desc\"");
				break;
			default:
				sb.append("\"article_review_score\": \"desc\"");
				break;
			}
			sb.append("}");
			}
			// 分页
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			JSONObject jsonObj = JSON.parseObject(esReturn);  
			JSONObject result = (JSONObject) jsonObj.get("hits");
	        return result;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
		}
		return null;
	}
	
	/**
	 * 商家商品綜合查詢
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家商品綜合查詢", notes = "商家商品綜合查詢")
	@RequestMapping(value="/shopArticle", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object shopArticle(HttpServletRequest req,@RequestBody AppShopArticleQuery query){
		try{
			int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字+分类ID
			if (!"".equals(query.getQueryStr()) && !"".equals(query.getShop_cate_id())) {
				sb.append("{ \"match\": { \"article_category_index\": \"" + query.getQueryStr()
						+ "\" } },{ \"match\": { \"article_user_category_id\": \"" + query.getShop_cate_id() + "\" } }");
			} else {
				if (!"".equals(query.getQueryStr())) {
					sb.append("{ \"match\": { \"article_category_index\": \"" + query.getQueryStr() + "\" } }");
				}
				if (!"".equals(query.getShop_cate_id())) {
					sb.append("{ \"match\": { \"article_user_category_id\": \"" + query.getShop_cate_id() + "\" } }");
				}
			}
			// 折扣
			if (!"".equals(query.getAct_flag())) {
				sb.append(",{ \"match\": { \"article_activity_type\": \"" + query.getAct_flag() + "\" } }");
			}
			// 制定商家
			if (query.getShop_id() != 0) {
				sb.append(",{ \"match\": { \"shop_id\": \"" + query.getShop_id() + "\" } }");
			}
			sb.append("]");
			sb.append("}}");
//			if (0!=query.getSort_flag()) {
			// 排序方式
			sb.append(",\"sort\": {");
			switch (query.getSort_flag()) {
			case 1:
				sb.append("\"article_order_times\": \"desc\"");
				break;
			case 2:
				sb.append("\"article_sell_price\":\"desc\"");
				break;
			case 3:
				sb.append("\"article_sell_price\":\"asc\"");
				break;
			case 4:
				sb.append("\"article_add_time\":\"desc\"");
				break;
			default:
				sb.append("\"article_review_score\": \"desc\"");
				break;
			}
			sb.append("}");
//			}
			// 分页
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb.toString().replace("must\": [,", "must\": ["),"application/json", null);
			JSONObject jsonObj = JSON.parseObject(esReturn);  
			JSONObject result = (JSONObject) jsonObj.get("hits");
	        return result;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
		}
		return null;
	}
}
