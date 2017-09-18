package com.manji.elastic.api.controller.app.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
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

import com.manji.elastic.api.commom.serchModel.AppCommoditySerchModel;
import com.manji.elastic.api.commom.serchModel.ShopCommoditySerchModel;
import com.manji.elastic.api.commom.utils.KeySerchBuider;
import com.manji.elastic.api.controller.hotsearch.HotSearchAddBiz;
import com.manji.elastic.api.controller.hotsearch.RecordModel;
import com.manji.elastic.biz.helper.ElasticsearchClientUtils;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.exception.NotFoundException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseObjectResult;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 二期搜索
 * @author Administrator
 *
 */
@Controller
@Api(value = "/api-serch_app_commodity_v2", description = " 二期搜索 ---- 商品APP部分")
@RequestMapping("/api/app/serch/commodity/v2/")
public class V2CommodityAppSerchApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
	public BaseObjectResult<SearchHits> queryCommodity(HttpServletRequest req, @RequestBody AppCommoditySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			long startTime = System.currentTimeMillis();
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字处理
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("article_category_index", body.getQueryStr()));
			}
			//商品名处理
			if(StringUtils.isNotBlank(body.getArticleName())){
				qb1.must(KeySerchBuider.getChniseBulider("article_title", body.getArticleName()));
			}
			//商家名处理
			if(StringUtils.isNotBlank(body.getShopName())){
				qb1.must(KeySerchBuider.getChniseBulider("shop_name", body.getShopName()));
			}
			//分类ID
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("class_list", cate_id));
				}
				qb1.must(cateIdORBuilder);
			}
			//是否包邮逻辑处理
			if(null != body.getShip_flag()) {
				if (body.getShip_flag() == 1) {
					qb1.must(QueryBuilders.matchQuery("is_free",1));
					if (StringUtils.isBlank(body.getDis_area_code())) {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area",1));
					} else {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area","1"+body.getDis_area_code()));
					}
				}else{
					if (StringUtils.isNotBlank(body.getDis_area_code())) {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area","1"+body.getDis_area_code()));
					}
				}
			}
			//商家ID
			if(null != body.getShop_id()) {
				qb1.must(QueryBuilders.matchQuery("shop_id",body.getShop_id()));
			}
			//商家分类
			if(StringUtils.isNotBlank(body.getArticle_user_category_id())){
				List<String> article_user_category_ids = Arrays.asList(body.getArticle_user_category_id().split(" "));
				BoolQueryBuilder article_user_categoryBuilder = QueryBuilders.boolQuery();
				for (String category_id : article_user_category_ids) {
					article_user_categoryBuilder.should(QueryBuilders.matchQuery("article_user_category_id", category_id));
				}
				qb1.must(article_user_categoryBuilder);
			}
			//是否折扣
			if(null != body.getSale_flag()){
				qb1.must(QueryBuilders.matchQuery("case_article_activity_type",body.getSale_flag()));
			}
			//折扣
			if(StringUtils.isNotBlank(body.getAct_flag())){
				List<String> act_flags = Arrays.asList(body.getAct_flag().split(" "));
				BoolQueryBuilder actFlagORBuilder = QueryBuilders.boolQuery();
				for (String act_flag : act_flags) {
					actFlagORBuilder.should(QueryBuilders.matchQuery("article_activity_type", act_flag));
				}
				qb1.must(actFlagORBuilder);
			}
			//商家区域
			if(StringUtils.isNotBlank(body.getArea_code())){
				qb1.must(QueryBuilders.matchQuery("left_shop_send_area",body.getArea_code()));
			}
			//处理品牌list(or逻辑)
			if(null != body.getBrand_code() && body.getBrand_code().size() > 0){
				BoolQueryBuilder brandORBuilder = QueryBuilders.boolQuery();
				for (String brandCode : body.getBrand_code()) {
					brandORBuilder.should(QueryBuilders.matchQuery("article_brand_id", brandCode));
				}
				qb1.must(brandORBuilder);
			}
			//价格区间处理
			qb1.filter(body.getPrice_end() != null ? 
					QueryBuilders.rangeQuery("article_sell_price").gte(body.getPrice_start()).lte(body.getPrice_end()) 
					: QueryBuilders.rangeQuery("article_sell_price").gte(body.getPrice_start()));
			//排序处理
			FieldSortBuilder sortBuilder = null ;
			if(null != body.getSort_flag()){
				/*if(0 == body.getSort_flag()){
					sortBuilder = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
				}*/
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
					sortBuilder = SortBuilders.fieldSort("shop_review_score").order(SortOrder.DESC);
				}
				if(5 == body.getSort_flag()){
					sortBuilder = SortBuilders.fieldSort("shop_order_times").order(SortOrder.DESC);
				}
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
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("综合商品查询--搜索耗时：" + (endTime - startTime) + "ms");
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
			
			//录入热搜词
			if(body.getQueryStr().length() >= 2) {
				RecordModel wordsModel = new RecordModel();
				wordsModel.setContent(body.getQueryStr());
				wordsModel.setDevice("APP");
				wordsModel.setIndexType("commodity");
				HotSearchAddBiz.addHotSearchWords(wordsModel);
			}
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
	 * 商家类查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家类查询", notes = "商家类查询"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/commodityGroupShop", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> commodityGroupShop(HttpServletRequest req, @RequestBody AppCommoditySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			long startTime = System.currentTimeMillis();
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字处理
			if (StringUtils.isNotBlank(body.getQueryStr())) {
				qb1.must(KeySerchBuider.getChniseBulider("shop_name", body.getQueryStr()));
			}
			//分类ID
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("class_list", cate_id));
				}
				qb1.must(cateIdORBuilder);
			}
			// 商家分类
			if (StringUtils.isNotBlank(body.getShop_cate_id())) {
				qb1.must(QueryBuilders.matchQuery("scope_values",body.getShop_cate_id()));
			}
			//是否包邮逻辑处理
			if(null != body.getShip_flag()) {
				if (body.getShip_flag() == 1) {
					qb1.must(QueryBuilders.matchQuery("is_free",1));
					if (StringUtils.isBlank(body.getDis_area_code())) {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area",1));
					} else {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area","1"+body.getDis_area_code()));
					}
				}else{
					if (StringUtils.isNotBlank(body.getDis_area_code())) {
						qb1.must(QueryBuilders.matchQuery("article_freeshipping_area","1"+body.getDis_area_code()));
					}
				}
			}
			//折扣类型
			if(null != body.getSale_flag()){
				qb1.must(QueryBuilders.matchQuery("case_article_activity_type",body.getSale_flag()));
			}
			//商家区域
			if(StringUtils.isNotBlank(body.getArea_code())){
				qb1.must(QueryBuilders.matchQuery("left_shop_send_area",body.getArea_code()));
			}
			//处理品牌list(or逻辑)
			if(null != body.getBrand_code() && body.getBrand_code().size() > 0){
				BoolQueryBuilder brandORBuilder = QueryBuilders.boolQuery();
				for (String brandCode : body.getBrand_code()) {
					brandORBuilder.should(QueryBuilders.matchQuery("article_brand_id", brandCode));
				}
				qb1.must(brandORBuilder);
			}
			//价格区间处理
			qb1.filter(body.getPrice_end() != null ? 
					QueryBuilders.rangeQuery("article_sell_price").gte(body.getPrice_start()).lte(body.getPrice_end()) 
					: QueryBuilders.rangeQuery("article_sell_price").gte(body.getPrice_start()));
			//排序处理
			FieldSortBuilder sortBuilder = null ;
			if(null != body.getSort_flag()){
				/*if(0 == body.getSort_flag()){
					sortBuilder = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
				}*/
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
					sortBuilder = SortBuilders.fieldSort("shop_review_score").order(SortOrder.DESC);
				}
				if(5 == body.getSort_flag()){
					sortBuilder = SortBuilders.fieldSort("shop_order_times").order(SortOrder.DESC);
				}
			}
			//collapse构建
			List<SortBuilder<?>> sorts = new ArrayList<SortBuilder<?>>();
			SortBuilder<?> sort = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
			sorts.add(sort);
			CollapseBuilder collapse = new CollapseBuilder("shop_id");
			collapse.setInnerHits(new InnerHitBuilder().setSize(3).setName("top_rated").setSorts(sorts));
			//创建搜索条件
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.setCollapse(collapse);
			if(null != sortBuilder){
				requestBuider.addSort(sortBuilder);
			}
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("商家类查询--搜索耗时：" + (endTime - startTime) + "ms");
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
			long startTime = System.currentTimeMillis();
			//参数校验
			if(null == body.getShop_id()){
				throw new BusinessDealException("商家ID参数必传");
			}
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("article_category_index", body.getQueryStr()));
			}
			//商家分类
			if(StringUtils.isNotBlank(body.getShop_cate_id())){
				List<String> shop_cate_ids = Arrays.asList(body.getShop_cate_id().split(" "));
				BoolQueryBuilder shopCateORBuilder = QueryBuilders.boolQuery();
				for (String shop_cate_id : shop_cate_ids) {
					shopCateORBuilder.should(QueryBuilders.matchQuery("article_user_category_id", shop_cate_id));
				}
				qb1.must(shopCateORBuilder);
			}
			//折扣
			if(StringUtils.isNotBlank(body.getAct_flag())){
				List<String> act_flags = Arrays.asList(body.getAct_flag().split(" "));
				BoolQueryBuilder actFlagORBuilder = QueryBuilders.boolQuery();
				for (String act_flag : act_flags) {
					actFlagORBuilder.should(QueryBuilders.matchQuery("article_activity_type", act_flag));
				}
				qb1.must(actFlagORBuilder);
			}
			//商家ID
			qb1.must(QueryBuilders.matchQuery("shop_id",body.getShop_id()));
			//排序处理
			FieldSortBuilder sortBuilder = null;
			if(null != body.getSort_flag()){
				/*if(0 == body.getSort_flag()){
					sortBuilder = SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC);
				}*/
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
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("商家店铺中搜索商品--搜索耗时：" + (endTime - startTime) + "ms");
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
			
			//录入热搜词
			if(body.getQueryStr().length() >= 2) {
				RecordModel wordsModel = new RecordModel();
				wordsModel.setContent(body.getQueryStr());
				wordsModel.setDevice("APP");
				wordsModel.setIndexType("commodity");
				HotSearchAddBiz.addHotSearchWords(wordsModel);
			}
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
			long startTime = System.currentTimeMillis();
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
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("好货列表--搜索耗时：" + (endTime - startTime) + "ms");
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
	public BaseObjectResult<SearchHits> similarRecommend(HttpServletRequest req,
			@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum,
			@RequestParam(required = false) String goods_id,@RequestParam(required = false) Integer shop_id){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			if(null == pageNum){
				pageNum = 1;
			}if(null == size){
				size = 20;
			}
			long startTime = System.currentTimeMillis();
			//查询当前商品是什么分类的
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			if(StringUtils.isBlank(goods_id)){
				throw new BusinessDealException("我不晓得你要查询什么类的了~~~");
			}
			requestBuider.setQuery(QueryBuilders.matchQuery("article_id", goods_id));
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			//分类ID
			String cate_id = hits.getHits()[0].getSource().get("article_category_id").toString();
			
			//当前分类下的其他商品
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			qb1.must(QueryBuilders.matchQuery("article_category_id",cate_id));
			//商家ID
			if(null != shop_id){
				qb1.must(QueryBuilders.matchQuery("shop_id", shop_id));
			}
			SearchRequestBuilder requestBuider1 = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider1.setTypes("info");
			requestBuider1.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider1.setQuery(qb1);
			requestBuider1.addSort(SortBuilders.fieldSort("article_add_time").order(SortOrder.DESC));
			requestBuider1.setFrom((pageNum - 1) * size).setSize(size);
			logger.info("参数json:{}",requestBuider1.toString());
			//执行查询结果
			SearchResponse searchResponse1 = requestBuider1.get();
			SearchHits hits1 = searchResponse1.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits1).toString());
			long endTime = System.currentTimeMillis();
			logger.info("同类推荐--搜索耗时：" + (endTime - startTime) + "ms");
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
	
	public static void main(String[] args) {
		String abc = "798";
		List<String> ss = Arrays.asList(abc.split(" "));
		for (String string : ss) {
			System.out.println(string);
		}
	}
}
