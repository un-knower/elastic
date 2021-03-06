package com.manji.elastic.api.controller.pc.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manji.elastic.api.commom.serchModel.BrandSerchModel;
import com.manji.elastic.api.commom.serchModel.CategorySerchModel;
import com.manji.elastic.api.commom.serchModel.PcCommoditySerchModel;
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
@Api(value = "/api-serch_pc_commodity_v2", description = " 二期搜索 ---- 商品PC部分")
@RequestMapping("/api/pc/serch/commodity/v2/")
public class V2CommodityPcSerchApiController {
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
	public BaseObjectResult<SearchHits> queryCommodity(HttpServletRequest req, @RequestBody PcCommoditySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
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
			//分类ID
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("class_list", cate_id));
				}
				qb1.must(cateIdORBuilder);
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
			//区域Code
			if(StringUtils.isNotBlank(body.getArea_code())){
				qb1.must(QueryBuilders.matchQuery("article_distribution_area",body.getArea_code()));
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
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			baseResult.setResult(hits);
			
			//录入热搜词
			if(body.getQueryStr().length() >= 2) {
				RecordModel wordsModel = new RecordModel();
				wordsModel.setContent(body.getQueryStr());
				wordsModel.setDevice("PC");
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
	 *查询分类list(商品查询)
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "查询分类list(商品查询)", notes = "查询分类list(商品查询)"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/queryCategoryIdList", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryCategoryIdList(HttpServletRequest req, @RequestBody CategorySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//collapse构建
			CollapseBuilder collapse = new CollapseBuilder("article_category_id");
			collapse.setInnerHits(new InnerHitBuilder().setSize(0).setName("top_rated"));
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("article_title", body.getQueryStr()));
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
			requestBuider.setCollapse(collapse);
			if(null != sortBuilder){
				requestBuider.addSort(sortBuilder);
			}
			requestBuider.setFrom((1 - 1) * 50).setSize(50);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
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
	 *查询分类list(商家类查询)
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "查询分类list(商家类查询)", notes = "查询分类list(商家类查询)"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/queryCategoryIdListGroupShop", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryCategoryIdListGroupShop(HttpServletRequest req, @RequestBody CategorySerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//collapse构建
			CollapseBuilder collapse = new CollapseBuilder("article_category_id");
			collapse.setInnerHits(new InnerHitBuilder().setSize(0).setName("top_rated"));
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("shop_name", body.getQueryStr()));
			}
			//分类ID
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("scope_values", cate_id));
				}
				qb1.must(cateIdORBuilder);
			}
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
			requestBuider.setCollapse(collapse);
			if(null != sortBuilder){
				requestBuider.addSort(sortBuilder);
			}
			requestBuider.setFrom((1 - 1) * 50).setSize(50);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
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
	 *查询品牌list
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "查询品牌list", notes = "查询品牌list"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/queryBrandList", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryBrandList(HttpServletRequest req, @RequestBody BrandSerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//collapse构建
			CollapseBuilder collapse = new CollapseBuilder("article_brand_id");
			collapse.setInnerHits(new InnerHitBuilder().setSize(0).setName("top_rated"));
			//关键字
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
			//不为0的
			qb1.mustNot(QueryBuilders.termQuery("article_brand_id","0"));
			//首字母
			if(StringUtils.isNotBlank(body.getFirstZm())) {
				qb1.must(QueryBuilders.termQuery("article_brand_first_char",body.getFirstZm().toUpperCase()));
			}
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
			requestBuider.setCollapse(collapse);
			if(null != sortBuilder){
				requestBuider.addSort(sortBuilder);
			}
			requestBuider.setFrom((1 - 1) * 50).setSize(50);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
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
}
