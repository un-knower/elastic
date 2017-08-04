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
import com.alibaba.fastjson.JSONObject;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.manji.elastic.api.controller.serch_v1.requestModel.app.AppArticleQuery;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.util.HttpClientUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 一期接口，这里保留
 * PC 商品
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@Api(value = "/pc-Article", description = "一期接口（保留兼容线上已运行产品），PC 商品")
@RequestMapping("/pc/article")
public class V1PCArticleApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商品查询", notes = "商品查询")
	@RequestMapping(value="/queryArticle", method = {RequestMethod.GET,RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Object queryArticle(HttpServletRequest req, AppArticleQuery query){
		try{
			int from = (query.getPageNum() - 1) * query.getSize();
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
