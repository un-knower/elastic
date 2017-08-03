package com.manji.elastic.api.controller.serch_v2;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppShopQuery;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseObjectResult;
import com.manji.elastic.common.util.ElasticsearchClientUtils;
import com.manji.elastic.common.util.HttpClientUtil;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 二期接口，这里保留
 * APP商家
 * @author Administrator
 *
 */
@Controller
@Api(value = "/app-Shopv2", description = "二期接口，APP商家")
@RequestMapping("/app/shop/v2")
public class V2APPShopApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 商家综合查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家综合查询", notes = "商家综合查询")
	@RequestMapping(value="/queryShop", method = {RequestMethod.GET}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryShop(HttpServletRequest req,@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) String queryStr,
			@RequestParam(required = false) String location,@RequestParam(required = false) Integer distance_max,
			@RequestParam(required = false) String cate_id,@RequestParam(required = false) String busy_id,
			@RequestParam(required = false) String area_code,@RequestParam(required = false) Integer sign_flag,
			@RequestParam(required = false) Integer open_flag,@RequestParam(required = false) Integer sort_flag
			){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			if(null == pageNum){
				pageNum = 1;
			}
			if(null == size){
				size = 20;
			}
			if(null == distance_max){
				distance_max = 1000;
			}
			if(null == sort_flag){
				sort_flag = 0;
			}
			String lat = "";
			String lon = "";
			if(StringUtils.isBlank(location)){
				throw new BusinessDealException("获取位置信息失败~~~");
			}
			lat = location.split(",")[0];
			lon = location.split(",")[1];
			int end = pageNum * size;
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字
			if(StringUtils.isNotBlank(queryStr)){
				qb1.must(QueryBuilders.matchQuery("shopinfo_index",queryStr));
			}
			// 商家主营分类
			if(StringUtils.isNotBlank(busy_id)){
				qb1.must(QueryBuilders.matchQuery("main_business",busy_id));
			}
			//商家分类
			if(StringUtils.isNotBlank(cate_id)){
				qb1.must(QueryBuilders.matchQuery("scope_values",cate_id));
			}
			//是否签约
			if(null != sign_flag){
				qb1.must(QueryBuilders.matchQuery("is_sign_up",sign_flag));
			}
			//是否开通
			if(null != open_flag){
				qb1.must(QueryBuilders.matchQuery("dpkg",open_flag));
			}
			//区域
			if(StringUtils.isNotBlank(area_code)){
				qb1.must(QueryBuilders.matchQuery("area_code",area_code));
			}
			//搜索附近区域
			QueryBuilder builder = QueryBuilders.geoDistanceRangeQuery("latlng", Double.valueOf(lat), Double.valueOf(lon))
				.from("0m")
				.to(distance_max + "m")
				.includeLower(true)
				.includeUpper(false)
				.geoDistance(GeoDistance.ARC);
			qb1.filter(builder);
			
			//排序方式
			GeoDistanceSortBuilder sort = new GeoDistanceSortBuilder("latlng", Double.valueOf(lat), Double.valueOf(lon));
			sort.unit(DistanceUnit.METERS);//距离单位米  
			sort.order(SortOrder.ASC); 
			
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_shop_IndexAlias());
			requestBuider.setTypes("info");
			
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			
			//距离排序
			if(sort_flag == 0){
				requestBuider.addSort(sort);
			}
			//评分和距离综合排序
			if(sort_flag == 1){
				requestBuider.addSort(SortBuilders.fieldSort("review_score").order(SortOrder.DESC));
				requestBuider.addSort(sort);
			}
			requestBuider.setFrom((pageNum - 1) * size);
			requestBuider.setSize(size);
			logger.info("参数json:{}",requestBuider.toString());
			//执行查询结果
			SearchResponse searchResponse = requestBuider.get();
			SearchHits hits = searchResponse.getHits();
			logger.info("结果:" + JSON.toJSONString(hits).toString());
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
	 * 推荐商家
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "推荐商家", notes = "推荐商家")
	@RequestMapping(value="/queryHotShop", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryHotShop(HttpServletRequest req,@RequestBody AppShopQuery query){
		try{
			String area_code = query.getArea_code();
			int len = area_code.length();
			String areaCode = "";
			if (len == 0) {
				return "{\"data\":\"推荐区域码未传\"}";
			}
			switch (len) {
			case 6:
				String temp61 = area_code.substring(0, 1);
				String temp62 = area_code.substring(0, 3);
				areaCode = temp61 + " " + temp62 + " " + area_code;
				break;
			case 4:
				String temp41 = area_code.substring(0, 1);
				areaCode = temp41 + " " + area_code;
				break;
			case 2:
				if (!area_code.equals("00")) {
					areaCode = "00 " + area_code;
				} else {
					areaCode = area_code;
				}
				break;
			default:
				areaCode = "00";
			}
			int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字
			if (!"".equals(query.getQueryStr())) {
				sb.append(",{ \"match\": { \"shopinfo_index\": \"" + query.getQueryStr() + "\" } }");
			}
			// 分类ID
			if (!"".equals(query.getCate_id())) {
				sb.append(",{ \"match\": { \"scope_values\": \"" + query.getCate_id() + "\" } }");
			}
			// 商家店铺开启状态
			if(1 ==query.getOpen_flag()){
				sb.append(",{ \"match\": { \"dpkg\": \"" + 1 + "\" } }");
			}
			// 区域
			sb.append(",{\"match\": { \"area_code\": \"" + areaCode + "\" } }");
			// if (!"".equals(query.getArea_code())) {
			//
			// sb.append(",{\"match\": { \"area\": \"" + query.getArea_code() + "\"
			// } }");
			// }
			sb.append("]");
			sb.append("}}");
			sb.append(",\"sort\":{\"hot\":\"asc\"}");
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"shop_hot"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
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
	
	public static String loaderExtra(StringBuffer sb) {
		String queryBody = sb.toString();
		queryBody = queryBody.replace("must\": [,", "must\": [");
		String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"shop_extra"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
		return esReturn;
	}
}
