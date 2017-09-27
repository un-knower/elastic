package com.manji.elastic.api.controller.app.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
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

import com.manji.elastic.api.commom.serchModel.ShopSerchModel;
import com.manji.elastic.api.commom.utils.AreaCodeUtil;
import com.manji.elastic.api.commom.utils.DistanceDoUtils;
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
@Api(value = "/api-serch_app_shop_v2", description = " 二期搜索 ---商家APP部分")
@RequestMapping("/api/app/serch/shop/v2/")
public class V2ShopAppSerchApiController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 商家综合查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家综合查询", notes = "商家综合查询"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出商品有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/queryShop", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryShop(HttpServletRequest req, @RequestBody ShopSerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			String lat = "";
			String lon = "";
			if(StringUtils.isNotBlank(body.getLocation())){
				lat = body.getLocation().split(",")[0];
				lon = body.getLocation().split(",")[1];
				if(StringUtils.isBlank(lat) || StringUtils.isBlank(lon)){
					throw new BusinessDealException("获取位置信息失败~~~");
				}
			}
			long startTime = System.currentTimeMillis();
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("shopinfo_index", body.getQueryStr()));
			}
			//商家名处理
			if(StringUtils.isNotBlank(body.getShopName())){
				qb1.must(KeySerchBuider.getChniseBulider("name", body.getShopName()));
			}
			// 商家主营分类
			if(StringUtils.isNotBlank(body.getBusy_id())){
				List<String> busy_ids = Arrays.asList(body.getBusy_id().split(" "));
				BoolQueryBuilder busyIdORBuilder = QueryBuilders.boolQuery();
				for (String busy_id : busy_ids) {
					busyIdORBuilder.should(QueryBuilders.matchQuery("main_business", busy_id));
				}
				qb1.must(busyIdORBuilder);
			}
			// 商家主营分类的频道ID
			if(StringUtils.isNotBlank(body.getMain_business_category_channel_id())){
				List<String> busy_channel_ids = Arrays.asList(body.getMain_business_category_channel_id().split(" "));
				BoolQueryBuilder busyChannelIdORBuilder = QueryBuilders.boolQuery();
				for (String busy_channel_id : busy_channel_ids) {
					busyChannelIdORBuilder.should(QueryBuilders.matchQuery("main_business_category_channel_id", busy_channel_id));
				}
				qb1.must(busyChannelIdORBuilder);
			}
			//商家分类
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("scope_values", cate_id));
				}
				qb1.must(cateIdORBuilder);
			}
			//是否签约
			if(null != body.getSign_flag()){
				qb1.must(QueryBuilders.matchQuery("is_sign_up",body.getSign_flag()));
			}
			//是否开通
			if(null != body.getOpen_flag()){
				qb1.must(QueryBuilders.matchQuery("dpkg",body.getOpen_flag()));
			}
			//区域
			if(StringUtils.isNotBlank(body.getArea_code())){
				qb1.must(QueryBuilders.matchQuery("area_code",body.getArea_code()));
			}
			GeoDistanceSortBuilder sort = null;
			if(StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)){
				//搜索附近区域
				QueryBuilder builder = QueryBuilders.geoDistanceQuery("latlng")
						.distance(body.getDistance_max() + "m")
						.point(Double.valueOf(lat), Double.valueOf(lon))
						.geoDistance(GeoDistance.ARC);
				qb1.filter(builder);
				//排序方式
				sort = new GeoDistanceSortBuilder("latlng", Double.valueOf(lat), Double.valueOf(lon));
				sort.unit(DistanceUnit.METERS);//距离单位米
				sort.order(SortOrder.ASC);
			}
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_shop_IndexAlias()).setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			if(null != body.getSort_flag()){
				//距离排序
				if(body.getSort_flag() == 0 && StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)){
					requestBuider.addSort(sort);
				}
				//评分和距离综合排序
				if(body.getSort_flag() == 1){
					requestBuider.addSort(SortBuilders.fieldSort("review_score").order(SortOrder.DESC));
					if(StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)){
						sort.order(SortOrder.ASC);
						requestBuider.addSort(sort);
					}
				}
			}
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());

			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			
			//搜索签约商家完毕。处理数据不够，抓取数据来凑
			int end =body.getPageNum()* body.getSize();
			int signCount = (int) hits.getTotalHits();
			if(end > signCount){
				if(StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)) {
					//搜索附近区域
					QueryBuilder builder = QueryBuilders.geoDistanceQuery("latlng")
							.distance("1000000000m")
							.point(Double.valueOf(lat), Double.valueOf(lon))
							.geoDistance(GeoDistance.ARC);
					qb1.filter(builder);
				}
				//构造查询抓取商家的查询条件
				SearchRequestBuilder requestBuiderExtra = client.prepareSearch(Configure.getES_shop_extra_IndexAlias()).setTypes("info");
				requestBuiderExtra.setSearchType(SearchType.QUERY_THEN_FETCH);
				requestBuiderExtra.setQuery(qb1);
				if(null != body.getSort_flag()){
					//距离排序
					if(body.getSort_flag() == 0 && StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)){
						requestBuiderExtra.addSort(sort);
					}
					//评分和距离综合排序
					if(body.getSort_flag() == 1){
						requestBuiderExtra.addSort(SortBuilders.fieldSort("review_score").order(SortOrder.DESC));
						if(StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)){
							requestBuiderExtra.addSort(sort);
						}
					}
				}
				if(end -signCount < body.getSize()){
					int hitsCount =hits.getHits().length;
					int deCount =body.getSize() -hitsCount;
					requestBuiderExtra.setFrom(0).setSize(deCount);
					SearchResponse searchResponseExtra = requestBuiderExtra.get();
					SearchHits hits1 = searchResponseExtra.getHits();
					//hits1 和 hits合并
					SearchHit[] both = (SearchHit[]) ArrayUtils.addAll(hits.getHits(), hits1.getHits());
					hits = new SearchHits(both, hits1.getTotalHits() + hits.getTotalHits() , hits1.getMaxScore());
				}else{
					int extraStart =end -signCount-body.getSize();
					requestBuiderExtra.setFrom(extraStart).setSize(body.getSize());
					SearchResponse searchResponseExtra = requestBuiderExtra.get();
					hits = searchResponseExtra.getHits();
				}
			}
			if(null == hits || hits.getHits() == null || hits.getHits().length == 0){
				throw new NotFoundException("抱歉，没有找到“关键词”的搜索结果");
			}
			//默认匹配度排序无距离，单独处理距离信息
			if(null == body.getSort_flag() && StringUtils.isNotBlank(body.getLocation())){
				hits = DistanceDoUtils.computerJl(body.getLocation(), hits);
			}
			//logger.info("商家查询结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("商家综合查询--搜索耗时：" + (endTime - startTime) + "ms");
			baseResult.setResult(hits);
			
			
			//录入热搜词
			if(body.getQueryStr().length() >= 2) {
				RecordModel wordsModel = new RecordModel();
				wordsModel.setContent(body.getQueryStr());
				wordsModel.setDevice("APP");
				wordsModel.setIndexType("shop");
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
	 * 推荐商家
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "推荐商家", notes = "推荐商家")
	@RequestMapping(value="/queryHotShop", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryHotShop(HttpServletRequest req,@RequestBody ShopSerchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			String area_code = body.getArea_code();
			if(StringUtils.isBlank(area_code)){
				throw new BusinessDealException("推荐区域码未传");
			}
			int len = area_code.length();
			String areaCode = AreaCodeUtil.doACode(area_code, len);
			long startTime = System.currentTimeMillis();
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(body.getQueryStr())){
				qb1.must(KeySerchBuider.getChniseBulider("shopinfo_index", body.getQueryStr()));
			}
			//商家分类
			if(StringUtils.isNotBlank(body.getCate_id())){
				List<String> cate_ids = Arrays.asList(body.getCate_id().split(" "));
				BoolQueryBuilder cateIdORBuilder = QueryBuilders.boolQuery();
				for (String cate_id : cate_ids) {
					cateIdORBuilder.should(QueryBuilders.matchQuery("scope_values", cate_id));
				}
				qb1.must(cateIdORBuilder);
			}
			//商家店铺开启状态
			if(null != body.getOpen_flag()){
				qb1.must(QueryBuilders.matchQuery("dpkg",body.getOpen_flag()));
			}
			//区域
			qb1.must(QueryBuilders.matchQuery("area_code",areaCode));
			
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_shop_hot_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.addSort(SortBuilders.fieldSort("hot").order(SortOrder.ASC));
			requestBuider.setFrom((body.getPageNum() - 1) * body.getSize()).setSize(body.getSize());
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			//logger.info("结果:" + JSON.toJSONString(hits).toString());
			long endTime = System.currentTimeMillis();
			logger.info("推荐商家--搜索耗时：" + (endTime - startTime) + "ms");
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
