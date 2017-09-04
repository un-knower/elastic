package com.manji.elastic.api.controller.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
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
	
	public static void main(String[] args) {
		TransportClient client = null;
		// 设置集群名称
					Settings settings = Settings.builder().put("cluster.name", "mj-es").build();
					
					// 创建client
					try {
						/*client = new PreBuiltTransportClient(settings)*/
						 client = new PreBuiltXPackTransportClient(settings)
								.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.49"), 9333));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					System.out.println("创建连接成功~~~~~~~~~~~~~~~");
		
			IndexRequestBuilder requestBuilder = client.prepareIndex("article", "info", null);//设置索引名称，索引类型，id  
			requestBuilder.setSource("{\r\n" + 
					"          \"article_activity_type\": \"\",\r\n" + 
					"          \"article_brand_name\": \"\",\r\n" + 
					"          \"fav_times\": 0,\r\n" + 
					"          \"article_user_category_id\": 0,\r\n" + 
					"          \"article_img_url\": \"http://file.manjiwang.com/?129a98d63676878a0e1f63a63fb2fde2\",\r\n" + 
					"          \"shop_review_score\": 43,\r\n" + 
					"          \"article_order_times\": 9,\r\n" + 
					"          \"article_add_time\": \"2017-04-07T18:20:31\",\r\n" + 
					"          \"article_id\": 295804,\r\n" + 
					"          \"wap_logo\": \"http://file.manjiwang.com/?86d2e68909894a8aa09315f53b2d546f\",\r\n" + 
					"          \"score\": 99,\r\n" + 
					"          \"update_time\": \"2017-08-14T18:09:20.21\",\r\n" + 
					"          \"article_user_category_name\": \"\",\r\n" + 
					"          \"article_sell_price\": 65,\r\n" + 
					"          \"shop_send_area\": \"500106\",\r\n" + 
					"          \"article_review_score\": 14616,\r\n" + 
					"          \"id\": 295804,\r\n" + 
					"          \"pics\": \"http://file.manjiwang.com/?b114d12ae0cfdbb3f1694954f227e8a2,\",\r\n" + 
					"          \"scope_values\": \"839 843 1928 889 890 891 892 893 895 826 824 1913 1923 3900\",\r\n" + 
					"          \"pc_logo\": \"http://file.manjiwang.com/?86d2e68909894a8aa09315f53b2d546f\",\r\n" + 
					"          \"article_freeshipping_area\": \"\",\r\n" + 
					"          \"article_category_index\": \"香甜柠檬水果\",\r\n" + 
					"          \"shop_brand\": 0,\r\n" + 
					"          \"article_distribution_area\": \" 500103 500104 500105 500106 500107 500108 500109 500112 5101 5103 5104 5105 5106 5107 5108 5109 5110 5111 5113 5114 5115 5116 5117 5118 5119 5120 5132 5133 5134 5201 5202 5203 5204 5205 5206 5223 5226 5227 4201 4202 4203 4205 4206 4207 4208 4209 4210 4211 4212 4213 4228 4290 4301 4302 4303 4304 4305 4306 4307 4308 4309 4310 4311 4312 4313 4331 5301 5303 5304 5305 5306 5307 5308 5309 5323 5325 5326 5328 5329 5331 5333 5334 6101 6102 6103 6104 6105 6106 6107 6108 6109 6110 6111 3401 3402 3403 3404 3405 3406 3407 3408 3410 3411 3412 3413 3415 3416 3417 3418 4101 4102 4103 4104 4105 4106 4107 4108 4109 4110 4111 4112 4113 4114 4115 4116 4117 4190 4501 4502 4503 4504 4505 4506 4507 4508 4509 4510 4511 4512 4513 4514 110101 110102 110105 110106 110107 110108 110109 110111 110112 110113 110114 110115 110116 110117 110118 110119 110120 120101 120102 120103 120104 120105 120106 120110 120111 120112 120113 120114 120115 120116 120117 120118 120225 120226 6301 6302 6322 6323 6325 6326 6327 6328 1301 1302 1303 1304 1305 1306 1307 1308 1309 1310 1311 1501 1502 1503 1504 1505 1506 1507 1508 1509 1522 1525 1529 2101 2102 2103 2104 2105 2106 2107 2108 2109 2110 2111 2112 2113 2114 3701 3702 3703 3704 3705 3706 3707 3708 3709 3710 3711 3712 3713 3714 3715 3716 3717 4601 4602 4603 4604 4690 1401 1402 1403 1404 1405 1406 1407 1408 1409 1410 1411 2301 2302 2303 2304 2305 2306 2307 2308 2309 2310 2311 2312 2327 6201 6202 6203 6204 6205 6206 6207 6208 6209 6210 6211 6212 6229 6230 6401 6402 6403 6404 6405 3601 3602 3603 3604 3605 3606 3607 3608 3609 3610 3611 310101 310104 310105 310106 310107 310109 310110 310112 310113 310114 310115 310116 310117 310118 310120 310230 310231 310232 310233 3201 3202 3203 3204 3205 3206 3207 3208 3209 3210 3211 3212 3213 3301 3302 3303 3304 3305 3306 3307 3308 3309 3310 3311 3312 3501 3502 3503 3504 3505 3506 3507 3508 3509 4401 4402 4403 4404 4405 4406 4407 4408 4409 4412 4413 4414 4415 4416 4417 4418 4419 4420 4451 4452 4453 2201 2202 2203 2204 2205 2206 2207 2208 2224 5401 5402 5403 5404 5405 5424 5425 6501 6502 6504 6522 6523 6527 6528 6529 6530 6531 6532 6540 6542 6543 6590 500101 500102 500110 500111 500113 500114 500115 500116 500117 500118 500119 500120 500151 500152 500153 500154 500155 500156 500157 500158 500159 500160 500162 500163 500164 500165 500167 500168 500169 500170 \",\r\n" + 
					"          \"vote_times\": 5,\r\n" + 
					"          \"class_list_name\": \"水果\",\r\n" + 
					"          \"article_activity\": \"\",\r\n" + 
					"          \"class_list\": \" 837 1923 4243 \",\r\n" + 
					"          \"shop_name\": \"生鲜店\",\r\n" + 
					"          \"shop_id\": 2144435,\r\n" + 
					"          \"article_title\": \"香甜柠檬水果\",\r\n" + 
					"          \"thumb_path_arr\": \"http://file.manjiwang.com/?129a98d63676878a0e1f63a63fb2fde2|http://file.manjiwang.com/?0e0ff0990955989d21b0d24c9997249b|http://file.manjiwang.com/?a7fb7ba1d835aca5b79c54ddded3e081\",\r\n" + 
					"          \"case_article_activity_type\": 0,\r\n" + 
					"          \"shop_order_times\": 262,\r\n" + 
					"          \"article_category_id\": 4243,\r\n" + 
					"          \"is_free\": 0,\r\n" + 
					"          \"left_shop_send_area\": \"50 5001 500106\",\r\n" + 
					"          \"is_activity\": 0,\r\n" + 
					"          \"article_category_name\": \"水果\",\r\n" + 
					"          \"article_brand_id\": 0,\r\n" + 
					"          \"article_tags\": \"\",\r\n" + 
					"          \"article_market_price\": 65\r\n" + 
					" }",XContentType.JSON).execute().actionGet();//创建索引  
		
	}
}
