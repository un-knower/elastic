package com.manji.elastic.api.controller.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
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
import com.manji.elastic.api.commom.BaseSerchModel;
import com.manji.elastic.api.commom.serchModel.CommoditySerchModel;
import com.manji.elastic.api.commom.serchModel.ShopCommoditySerchModel;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.exception.NotFoundException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseObjectResult;
import com.manji.elastic.common.util.ElasticsearchClientUtils;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 二期搜索
 * @author Administrator
 *
 */
@Controller
@Api(value = "/api-serch/v2", description = " 二期搜索")
@RequestMapping("/api/serch/commodity/v2/")
public class V2SerchApiController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 输入关键字综合搜索商品,商家
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "输入关键字综合搜索商品,商家", notes = "输入关键字综合搜索商品,商家"
			+ "<br/>先默认搜索“商品”，无结果情况下转“商家”"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10003---商品无结果，成功搜索商家有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/complex", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryArticle(HttpServletRequest req,@RequestBody BaseSerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			long startTime = System.currentTimeMillis();
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(QueryBuilders.matchQuery("article_category_index",body.getQueryStr()));
			}
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias()).setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			logger.info("结果:" + JSON.toJSONString(hits).toString());
			//如果搜索商品无结果。。。转向搜索商家
			if(null == hits || hits.getTotalHits() == 0){
				BoolQueryBuilder qbShop = QueryBuilders.boolQuery();
				//关键字
				if(StringUtils.isNotBlank(body.getQueryStr())){
					qbShop.must(QueryBuilders.matchQuery("shopinfo_index",body.getQueryStr()));
				}
				SearchRequestBuilder requestBuiderShop = client.prepareSearch(Configure.getES_shop_IndexAlias()).setTypes("info");
				requestBuiderShop.setSearchType(SearchType.QUERY_THEN_FETCH);
				requestBuiderShop.setQuery(qbShop);
				requestBuiderShop.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
				logger.info("参数json:{}",requestBuiderShop.toString());
				//执行查询结果
				SearchResponse searchResponseShop = requestBuiderShop.get();
				SearchHits hitsShop = searchResponseShop.getHits();
				logger.info("结果:" + JSON.toJSONString(hitsShop).toString());
				if(null == hitsShop || hitsShop.getTotalHits() == 0){
					throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
				}
				baseResult.setCode(CodeEnum.MDZZ.getCode());
				baseResult.setResult(hitsShop);
			}else{
				baseResult.setResult(hits);
			}
			long endTime = System.currentTimeMillis();
			logger.info("搜索耗时：{} ms" , endTime - startTime);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
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
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "综合商品查询", notes = "综合商品查询"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/queryCommodity", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryCommodity(HttpServletRequest req, @RequestBody CommoditySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(QueryBuilders.matchQuery("article_category_index",body.getQueryStr()));
			}
			//分类ID
			if(StringUtils.isNotBlank(body.getCate_id())){
				qb1.must(QueryBuilders.matchQuery("class_list",body.getCate_id()));
			}
			//是否包邮
			if(null != body.getShip_flag()){
				qb1.must(QueryBuilders.matchQuery("is_free",body.getShip_flag()));
			}
			//折扣类型
			if(null != body.getSale_flag()){
				qb1.must(QueryBuilders.matchQuery("case_article_activity_type",body.getSale_flag()));
			}
			//区域Code
			if(StringUtils.isNotBlank(body.getArea_code())){
				qb1.must(QueryBuilders.matchQuery("article_distribution_area",body.getArea_code()));
			}
			//价格区间处理
			qb1.filter(body.getPrice_end() != null ? 
					QueryBuilders.rangeQuery("article_sell_price").gt(body.getPrice_start()).lt(body.getPrice_end()) 
					: QueryBuilders.rangeQuery("article_sell_price").gt(body.getPrice_start()));
			//排序处理
			FieldSortBuilder sortBuilder = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
			if(1 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC);
			}
			if(2 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_sell_price").order(SortOrder.DESC);
			}
			if(3 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_sell_price").order(SortOrder.ASC);
			}
			//创建搜索条件
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			if(null != sortBuilder){
				requestBuider.addSort(sortBuilder);
			}
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			logger.info("结果:" + JSON.toJSONString(hits).toString());
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
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
	 * 商家店铺中搜索商品
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家店铺中搜索商品", notes = "商家店铺中搜索商品"
				+ "<h3 style='color:red'>状态码说明</h3>"
				+ "<br/>		10000---成功搜索出商品有结果"
				+ "<br/>		10001---拿后端返回的message提示一下即可"
				+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/commodityOfShop", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> commodityOfShop(HttpServletRequest req,@RequestBody ShopCommoditySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(QueryBuilders.matchQuery("article_category_index",body.getQueryStr()));
			}
			//商家分类
			if(StringUtils.isNotBlank(body.getShop_cate_id())){
				qb1.must(QueryBuilders.matchQuery("article_user_category_id",body.getShop_cate_id()));
			}
			//折扣
			if(StringUtils.isNotBlank(body.getAct_flag())){
				qb1.must(QueryBuilders.matchQuery("article_activity_type",body.getAct_flag()));
			}
			if(null == body.getShop_Id()){
				throw new BusinessDealException("商家ID参数必传");
			}
			//商家ID
			qb1.must(QueryBuilders.matchQuery("shop_id",body.getShop_Id()));
			//排序处理
			FieldSortBuilder sortBuilder = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
			if(1 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC);
			}
			if(2 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_sell_price").order(SortOrder.DESC);
			}
			if(3 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_sell_price").order(SortOrder.ASC);
			}
			if(4 == body.getSort_flag()){
				sortBuilder = SortBuilders.fieldSort("article_add_time").order(SortOrder.DESC);
			}
			//创建搜索条件
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.addSort(sortBuilder);
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			logger.info("结果:" + JSON.toJSONString(hits).toString());
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
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
	@ApiOperation(value = "好货列表", notes = "好货列表"
				+ "<h3 style='color:red'>状态码说明</h3>"
				+ "<br/>		10000---成功搜索出商品有结果"
				+ "<br/>		10001---拿后端返回的message提示一下即可"
				+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/satisfiedList", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> satisfiedList(HttpServletRequest req,
			@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			if(null == pageNum){
				pageNum = 1;
			}
			if(null == size){
				size = 20;
			}
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(QueryBuilders.matchAllQuery());
			requestBuider.addSort(SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC));
			requestBuider.addSort(SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC));
			requestBuider.setFrom((pageNum - 1) * size).setSize(size);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			logger.info("结果:" + JSON.toJSONString(hits).toString());
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
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
	@ApiOperation(value = "同类推荐", notes = "同类推荐"
				+ "<h3 style='color:red'>状态码说明</h3>"
				+ "<br/>		10000---成功搜索出商品有结果"
				+ "<br/>		10001---拿后端返回的message提示一下即可"
				+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/similarRecommend", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> similarRecommend(HttpServletRequest req,@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum,
			@RequestParam(required = false) String goods_id,@RequestParam(required = false) Integer shop_id){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			if(null == pageNum){
				pageNum = 1;
			}
			if(null == size){
				size = 20;
			}
			//查询当前商品是什么分类的
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			SearchRequestBuilder requestBuder = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuder.setTypes("info");
			requestBuder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			if(StringUtils.isBlank(goods_id)){
				throw new BusinessDealException("我不晓得你要查询什么类的了~~~");
			}
			requestBuder.setQuery(QueryBuilders.termQuery("article_category_index", goods_id));
			SearchResponse searchResponse = requestBuder.get();
			SearchHits hits = searchResponse.getHits();
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			//分类ID
			String cate_id = hits.getHits()[0].getSource().get("article_category_id").toString();
			
			//当前分类下的其他商品
			List<MatchPhraseQueryBuilder> mpqs = new ArrayList<MatchPhraseQueryBuilder>();
			//分类ID
			mpqs.add(StringUtils.isBlank(cate_id) ? null :QueryBuilders.matchPhraseQuery("class_list",cate_id));
			//商家ID
			mpqs.add(shop_id == null  ? null :QueryBuilders.matchPhraseQuery("shop_id", shop_id));
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			for(MatchPhraseQueryBuilder mpq : mpqs){
				if(mpq != null){
					qb1.must(mpq);
				}
			}
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.addSort(SortBuilders.fieldSort("article_add_time").order(SortOrder.DESC));
			requestBuider.setFrom((pageNum - 1) * size);
			requestBuider.setSize(size);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse1 = requestBuider.get();
			SearchHits hits1 = searchResponse1.getHits();
			logger.info("结果:" + JSON.toJSONString(hits1).toString());
			if(null == hits1 || hits1.getHits() == null || hits1.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits1);
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseObjectResult<SearchHits>(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
}
