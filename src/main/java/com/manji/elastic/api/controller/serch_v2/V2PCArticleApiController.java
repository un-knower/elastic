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
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseObjectResult;
import com.manji.elastic.common.util.ElasticsearchClientUtils;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 二期接口，这里保留
 * PC 商品
 * @author Administrator
 *
 */
@Controller
@Api(value = "/pc-Articlev2", description = "二期接口，PC 商品查询")
@RequestMapping("/pc/article/v2")
public class V2PCArticleApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 商品查询
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "商品查询", notes = "商品查询")
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
			if(null == price_start){
				price_start = 0;
			}
			if(null == pageNum){
				pageNum = 1;
			}
			if(null == size){
				size = 20;
			}
			//连接服务端
			TransportClient  client = ElasticsearchClientUtils.getTranClinet();
			List<MatchQueryBuilder> mpqs = new ArrayList<MatchQueryBuilder>();
			//关键字
			mpqs.add(StringUtils.isBlank(queryStr) ? null :QueryBuilders.matchQuery("article_category_index",queryStr));
			//分类ID
			mpqs.add(StringUtils.isBlank(cate_id) ? null :QueryBuilders.matchQuery("class_list",cate_id));
			//是否包邮
			mpqs.add(ship_flag == null ? null :QueryBuilders.matchQuery("is_free",ship_flag));
			//折扣类型
			mpqs.add(ship_flag == null ? null :QueryBuilders.matchQuery("case_article_activity_type", sale_flag));
			//商家ID
			mpqs.add(shop_id == null  ? null :QueryBuilders.matchQuery("shop_id", shop_id));
			//区域Code
			mpqs.add(StringUtils.isBlank(area_code) ? null :QueryBuilders.matchQuery("article_distribution_area", area_code));
			
			BoolQueryBuilder qb1 = QueryBuilders.boolQuery();
			for(MatchQueryBuilder mpq : mpqs){
				if(mpq != null){
					qb1.must(mpq);
				}
			}
			SearchRequestBuilder requestBuider = client.prepareSearch(Configure.getES_sp_IndexAlias());
			requestBuider.setTypes("info");
			requestBuider.setSearchType(SearchType.QUERY_THEN_FETCH);
			requestBuider.setQuery(qb1);
			requestBuider.setPostFilter(price_end != null ? QueryBuilders.rangeQuery("article_sell_price").gt(price_start).lt(price_end) : QueryBuilders.rangeQuery("article_sell_price").gt(price_start));
			requestBuider.addSort(
						sort_flag == null ? SortBuilders.fieldSort("article_review_score").order(SortOrder.DESC) 
						: (sort_flag == 1 ? SortBuilders.fieldSort("article_order_times").order(SortOrder.DESC) 
						: (sort_flag == 2 ? SortBuilders.fieldSort("article_sell_price").order(SortOrder.ASC)
						: (sort_flag == 3 ? SortBuilders.fieldSort("article_sell_price").order(SortOrder.DESC) : null)))
					);
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
}
