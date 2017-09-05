package com.manji.elastic.api.controller.commonserch;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
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

import com.manji.elastic.api.commom.utils.KeySerchBuider;
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
@Api(value = "/api-serch_commom_v2", description = " 二期搜索 ----下拉框提示")
@RequestMapping("/api/commom/serch/select/v2/")
public class V2CommonSerchApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 下拉框提示查询,APP_PC 通用一个
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "下拉框提示查询,APP_PC 通用一个", notes = "下拉框提示查询,APP_PC 通用一个"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---成功搜索出有结果"
			+ "<br/>		10001---拿后端返回的message提示一下即可"
			+ "<br/>		10004---抱歉，没有找到“关键词”的搜索结果")
	@RequestMapping(value="/query", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseObjectResult<SearchHits> queryCommodity(HttpServletRequest req, @RequestBody SelectSearchModel body){
		BaseObjectResult<SearchHits> baseResult=new BaseObjectResult<SearchHits>(CodeEnum.SUCCESS.getCode(),"查询成功");
		try{
			Integer pageNum = 1;
			Integer size = 10;
			long startTime = System.currentTimeMillis();
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			//关键字处理
			if(StringUtils.isBlank(body.getContent())){
				throw new BusinessDealException("关键字不为空的时候再请求我嘛");
			}
			if(body.getContent().trim().length() < 2){
				throw new BusinessDealException("关键字大于两个字的时候再请求我嘛");
			}
			qb1.must(KeySerchBuider.getChniseBulider("content", body.getContent().trim()));
			if(StringUtils.isBlank(body.getDevice())) {
				throw new BusinessDealException("设备参数必须传");
			}
			if(StringUtils.isNotBlank(body.getIndexType())) {
				qb1.must(QueryBuilders.matchPhraseQuery("indexType", body.getIndexType().trim()));
			}
			qb1.must(QueryBuilders.matchPhraseQuery("device", body.getDevice().trim()));
			//创建搜索条件
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_IndexHotSearchWords());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.addSort(SortBuilders.fieldSort("count").order(SortOrder.DESC));
			requestBuider.setFrom((pageNum - 1) * size).setSize(size);
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
