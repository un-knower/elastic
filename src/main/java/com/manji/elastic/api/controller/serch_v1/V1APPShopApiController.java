package com.manji.elastic.api.controller.serch_v1;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppShopQuery;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.util.HttpClientUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 一期接口，这里保留
 * APP商家
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@Api(value = "/app-Shop", description = "一期接口（保留兼容线上已运行产品），APP商家")
@RequestMapping("/app/shop")
public class V1APPShopApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 商家综合查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家综合查询", notes = "商家综合查询")
	@RequestMapping(value="/queryShop", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryShop(HttpServletRequest req, AppShopQuery query){
		try{
			// 获取传入的地理位置
			String location = query.getLocation();
			String lat = "";
			String lon = "";
			if ("" != location) {
				lat = location.split(",")[0];
				lon = location.split(",")[1];
			} else {
				return "{\"data\":\"地理位置未传\"}";
			}
			int from = (query.getPageNum() - 1) * query.getSize();
			int end =query.getPageNum()* query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字
			if (!"".equals(query.getQueryStr())) {
				sb.append("{ \"match\": { \"shopinfo_index\": {\"query\":\"" + query.getQueryStr() + "\",\"operator\": \"and\"}} }");
			}
			// 商家主营分类
			if (!"".equals(query.getBusy_id())) {
				sb.append(",{ \"match\": { \"main_business\": \"" + query.getBusy_id() + "\" } }");
			}
			// 商家分类
			if (!"".equals(query.getCate_id())) {
				sb.append(",{ \"match\": { \"scope_values\": \"" + query.getCate_id() + "\" } }");
			}
			// 是否签约
			if (0 != query.getSign_flag()) {
				sb.append(",{ \"match\": { \"is_sign_up\": \"" + query.getSign_flag() + "\" } }");
			}
			if(1 ==query.getOpen_flag()){
				sb.append(",{ \"match\": { \"dpkg\": \"" + 1 + "\" } }");
			}
			// 区域
			if (!"".equals(query.getArea_code())&&query.getArea_code() !=null) {
				sb.append(",{\"match\": { \"area_code\": \"" + query.getArea_code() + "\" } }");
			}
			sb.append("]");
			// 搜索附近最大区域
			if (0 == query.getDistance_max()) {
				sb.append(",\"filter\":{\"geo_distance\":{\"distance\":\"10000000m\",\"latlng\":{\"lat\":" + lat + ",\"lon\":"
						+ lon + "}}}");
			} else {
				sb.append(",\"filter\":{\"geo_distance\":{\"distance\":\"" + query.getDistance_max()
						+ "m\",\"latlng\":{\"lat\":" + lat + ",\"lon\":" + lon + "}}}");
			}
			sb.append("}}");
			// 排序方式
			if (0 == query.getSort_flag()) {
				sb.append(",\"sort\":{\"_geo_distance\":{\"latlng\":{\"lat\":\"" + lat + "\",\"lon\":\"" + lon
						+ "\"},\"order\": \"asc\",\"unit\":\"m\"}}");
			} else {
				// service_review_score
				sb.append(",\"sort\":{\"review_score\":\"desc\",\"_geo_distance\":{\"latlng\":{\"lat\":\"" + lat + "\",\"lon\":\"" + lon
						+ "\"},\"order\": \"asc\",\"unit\":\"m\"}}");
			}
			StringBuffer tempSb =sb;
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"shop"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			JSONObject obj = JSONObject.parseObject(esReturn);
			JSONObject returnObj = obj.getJSONObject("hits");
			//签约商家查询完毕
			int signCount =returnObj.getIntValue("total");
			//签约商家数量不足
			if(end >signCount){
				if(end -signCount<query.getSize()){
					JSONArray hits =returnObj.getJSONArray("hits");
					int hitsCount =0;
					if(null !=hits){
						hitsCount =hits.size();
					}
					int deCount =query.getSize() -hitsCount;
					tempSb.append(",\"size\": " + deCount + ",\"from\": " + 0 + "}");
					String tempEsReturn = loaderExtra(tempSb);
					JSONObject tempObj = JSONObject.parseObject(tempEsReturn);
					JSONObject tempReturnObj = tempObj.getJSONObject("hits");
					JSONArray extraHits =tempReturnObj.getJSONArray("hits");
					for(int i =0;i<extraHits.size();i++){
						JSONObject extraObj =extraHits.getJSONObject(i);
						returnObj.getJSONArray("hits").add(i+hitsCount, extraObj);
					}
				}else{
					int extraStart =end -signCount-query.getSize();
					tempSb.append(",\"size\": " + query.getSize() + ",\"from\": " + extraStart + "}");
					String tempEsReturn = loaderExtra(tempSb);
					JSONObject tempObj = JSONObject.parseObject(tempEsReturn);
					JSONObject tempReturnObj = tempObj.getJSONObject("hits");
					returnObj =tempReturnObj;
				}
			}
			//返回信息
	        return returnObj;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
		}
		return null;
	}
	
	
	/**
	 * 推荐商家
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "推荐商家", notes = "推荐商家")
	@RequestMapping(value="/queryHotShop", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryHotShop(HttpServletRequest req, AppShopQuery query){
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
