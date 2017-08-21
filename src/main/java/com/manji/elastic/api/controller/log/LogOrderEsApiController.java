package com.manji.elastic.api.controller.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.manji.elastic.api.controller.log.requestmodel.OrderLogBody;
import com.manji.elastic.biz.helper.ElasticsearchClientUtils;
import com.manji.elastic.common.exception.BusinessDealException;
import com.manji.elastic.common.exception.NotFoundException;
import com.manji.elastic.common.global.Configure;
import com.manji.elastic.common.result.BaseResult;
import com.manji.elastic.common.util.DateUtils;
import com.manji.elastic.dal.enums.CodeEnum;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
/**
 * 日志系统
 * @author Administrator
 *
 */
@Controller
@Api(value = "/api-serch_log", description = "订单日志Es处理")
@RequestMapping("/api/serch/log/order/v1/")
public class LogOrderEsApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 *订单日志
	 * @param req
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "添加定单日志", notes = "添加定单日志"
			+ "<h3 style='color:red'>状态码说明</h3>"
			+ "<br/>		10000---业务执行成功"
			+ "<br/>		10001---添加出现异常，拿出message存入文件日志")
	@RequestMapping(value="/add", method = {RequestMethod.POST}, produces = { MediaType.APPLICATION_JSON_VALUE })
	public BaseResult backorderAdd(HttpServletRequest req, @RequestBody OrderLogBody body){
		BaseResult baseResult=new BaseResult(CodeEnum.SUCCESS.getCode(),"添加成功");
		try{
			if(null == body) {
				throw new BusinessDealException("请传输日志数据");
			}
			if(StringUtils.isBlank(body.getEvent_source())) {
				throw new BusinessDealException("订单源不允许为空");
			}
			if(StringUtils.isBlank(body.getRowAddTime())) {
				body.setRowAddTime(DateUtils.DateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date()));
			}
			//连接服务端
			TransportClient client = ElasticsearchClientUtils.getTranClinet();
			IndexRequestBuilder requestBuilder = client.prepareIndex(Configure.getES_IndexOrder(), body.getEvent_source(), null);//设置索引名称，索引类型，id  
			requestBuilder.setSource(JSON.toJSONString(body),XContentType.JSON).execute().actionGet();//创建索引  
		}catch (BusinessDealException e) {
			logger.error("业务处理异常， 错误信息：{}", e.getMessage());
			baseResult = new BaseResult(CodeEnum.BUSSINESS_HANDLE_ERROR.getCode(), e.getMessage());
		}catch (NotFoundException e) {
			logger.error("未找到， 错误信息：{}", e.getMessage());
			baseResult = new BaseResult(CodeEnum.NOT_FOUND.getCode(), e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("系统异常，{}", e.getMessage());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			baseResult = new BaseResult(CodeEnum.SYSTEM_ERROR.getCode(), "系统异常" , sw.toString());
		}
		return baseResult;
	}
}
