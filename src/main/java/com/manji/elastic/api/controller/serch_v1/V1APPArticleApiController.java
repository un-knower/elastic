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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppArticleQuery;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppShopArticleQuery;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.util.HttpClientUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * 一期接口，这里保留
 * APP商品
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@Api(value = "/app-Article", description = "一期接口（保留兼容线上已运行产品），APP商品")
@RequestMapping("/app/article")
public class V1APPArticleApiController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 综合商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "综合商品查询", notes = "综合商品查询")
	@RequestMapping(value="/queryArticle", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryArticle(HttpServletRequest req, AppArticleQuery query){
		try{
			int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字+分类ID
			if (!"".equals(query.getQueryStr())) {
				//sb.append("{ \"match\": { \"shop_name\": \"" + query.getQueryStr() + "\" } }");
				sb.append("{ \"match\": { \"article_category_index\": {\"query\":\"" + query.getQueryStr() + "\",\"operator\": \"and\"}} }");
			}
			if (!"".equals(query.getCate_id())) {
				
				sb.append(",{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
			}
			// 是否包邮逻辑段
			if (query.getShip_flag() == 1) {
				sb.append(",{ \"match\": { \"is_free\": \"" + query.getShip_flag() + "\" } }");
				if (!"".equals(query.getDis_area_code())) {
					sb.append(",{ \"match\": { \"article_freeshipping_area\": \"" + "1 " + query.getDis_area_code()
							+ "\" } }");
				} else {
					sb.append(",{ \"match\": { \"article_freeshipping_area\": \"" + "1 " + "\" } }");
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
			System.out.println(sb.toString().replace("must\": [,", "must\": ["));
			
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
	 * 好货列表
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "好货列表", notes = "好货列表")
	@RequestMapping(value="/satisfiedList", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object satisfiedList(HttpServletRequest req,@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum){
		try{
			if(size == null ){
				size = 20;
			}
			if(pageNum == null){
				pageNum = 1;
			}
			int from = (pageNum - 1) * size;
			StringBuffer sb = new StringBuffer("{\"query\":  { \"match_all\": {} }");
			sb.append(
					",\"sort\": [{\"article_order_times\":{\"order\":\"desc\"}},{\"article_review_score\":{\"order\":\"desc\"}}]");
			sb.append(",\"size\": " + size + ",\"from\": " + from + "}");
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
	 * 同类推荐
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "同类推荐", notes = "同类推荐")
	@RequestMapping(value="/similarRecommend", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object similarRecommend(HttpServletRequest req,@RequestParam(required = false) Integer size,@RequestParam(required = false) Integer pageNum,
			@RequestParam(required = false) String goods_id,@RequestParam(required = false) Integer shop_id){
		try{
			if(size == null ){
				size = 20;
			}
			if(pageNum == null){
				pageNum = 1;
			}
			int from = (pageNum - 1) * size;
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [{\"match\": {\"article_id\":\"" + goods_id + "\"}}]}}}");
			
			String esReturn1 = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			
			JSONObject jsonObj = JSON.parseObject(esReturn1);  
			JSONObject result = (JSONObject) jsonObj.get("hits");
			JSONArray array = result.getJSONArray("hits");
			if(array.size() == 0){
				logger.info("=====没找到相关数据。");
				return null;
			}
			JSONObject hit = array.getJSONObject(0);
			String cate_id = hit.getJSONObject("_source").getString("article_category_id");
			StringBuffer sb2 = new StringBuffer(
					"{\"query\": {\"bool\": {\"must\": [{\"match\": {\"article_category_id\":\"" + cate_id + "\"}}");
			if(0!=shop_id){
				sb.append(",{ \"match\": { \"shop_id\": \"" + shop_id + "\" } }");
			}
			sb.append("]}}");
			sb2.append(",\"sort\":[{\"article_add_time\":{\"order\":\"desc\"}}]");
			sb2.append(",\"size\": " + size + ",\"from\": " + from + "}");
			
			String esReturn2 = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb2.toString().replace("must\": [,", "must\": ["),"application/json", null);
			
			JSONObject jsonObj2 = JSON.parseObject(esReturn2);  
			JSONObject result2 = (JSONObject) jsonObj2.get("hits");
			return result2;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
		}
		return null;
	}
	/**
	 * 商家类查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商家类查询", notes = "商家类查询")
	@RequestMapping(value="/articleOfShop", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object articleOfShop(HttpServletRequest req, AppArticleQuery query){
		try{
			int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			if (!"".equals(query.getQueryStr())) {
				//sb.append("{ \"match\": { \"shop_name\": \"" + query.getQueryStr() + "\" } }");
				sb.append("{ \"match\": { \"shop_name\": {\"query\":\"" + query.getQueryStr() + "\",\"operator\": \"and\"}} }");
			}
			//分类ID
			if (!"".equals(query.getCate_id())) {
				sb.append(",{ \"match\": { \"class_list\": \"" + query.getCate_id() + "\" } }");
			}
			// 商家分类
			if ("" != query.getShop_cate_id()) {
				sb.append(",{\"match\": { \"scope_values\": \"" + query.getShop_cate_id() + "\" } }");
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
			System.out.println("-------------条件"+sb.toString().replace("must\": [,", "must\": ["));
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
	public Object shopArticle(HttpServletRequest req, AppShopArticleQuery query){
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
