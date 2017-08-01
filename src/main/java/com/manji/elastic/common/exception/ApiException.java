package com.manji.elastic.common.exception;






/**
 * Api相关异常
 */
public class ApiException extends Exception {
    /** 
	* @Fields serialVersionUID: 
	*/
	private static final long serialVersionUID = 2464073982627867256L;
	
	private final int errorCode;
    private final String description;
    private final int httpStatusCode;

    public static final ApiException system_error = new ApiException("System error", 10001, "系统错误", 500);
    public static final ApiException service_unavailable = new ApiException("Service unavailable", 10002, "服务暂停", 503);
    public static final ApiException service_not_found = new ApiException("Service not found", 10003, "服务不存在", 404);
    public static final ApiException illegal_request = new ApiException("Illegal request", 10004, "非法请求", 400);
    public static final ApiException no_permission = new ApiException("No permission", 10005, "没有权限", 403);
    public static final ApiException system_is_busy = new ApiException("System is busy", 10006, "系统繁忙", 500);
    public static final ApiException unsupport_mediatype = new ApiException("Unsupport mediatype", 10007, "不支持的媒体类型", 415);
    public static final ApiException miss_required_parameters = new ApiException("Miss required parameters, see doc for more info", 10008, "缺少必填字段，请参照API文档查询更多内容", 400);
    public static final ApiException illegal_parameter_type  = new ApiException("Illegal parameter type， see doc for more info", 10009, "非法的字段类型，请参照API文档查询更多内容", 415);
    public static final ApiException unsupport_http_method = new ApiException("Unsupport http method", 10010, "不支持的http方法，请参照API文档查询服务的HTTP方法是哪一种（GET/POST/PUT/DELETE）", 400);
    public static final ApiException illegal_request_source = new ApiException("Illegal request source", 10011, "非法的调用来源，调用来源不在白名单内", 400);
    public static final ApiException miss_name = new ApiException("miss_name", 10012, "姓名不能为空", 400);
    public static final ApiException param_null = new ApiException("param_null", 10013, "参数不能为空", 400);
    public static final ApiException param_Decrypt_null = new ApiException("param_Decrypt_null", 10014, "参数解密出错", 400);
    public static final ApiException return_value_is_not_correct = new ApiException("return_value_is_not_correct", 10015, "通知状态码不正确", 400);
    public static final ApiException Obtain_DecryptString_fail = new ApiException("Obtain_DecryptString_fail", 10016, "获取异步通知解密的字符串data失败", 400);
    public static final ApiException Obtain_OrderStart_fail = new ApiException("Obtain_OrderStart_fail", 10017, "获取订单状态码失败", 400);
    public static final ApiException Obtain_OrderSerialnumber_fail = new ApiException("Obtain_OrderSerialnumber_fail", 10018, "获取订单交易流水号失败", 400);
    public static final ApiException OrderSerialnumber_null = new ApiException("OrderSerialnumber_null", 10019, "未查询到此订单", 400);
    public static final ApiException OrderStart_error = new ApiException("OrderStart_error", 10020, "订单状态码不在系统设置范围内", 400);
    public static final ApiException ChannelId_error = new ApiException("ChannelId_error", 10021, "渠道ID不能为空", 400);
    public static final ApiException ChannelId_overlength = new ApiException("ChannelId_overlength", 10022, "渠道ID长度不能超过20位", 400);
    public static final ApiException StateDate_or_EndDate_null = new ApiException("StateDate_or_EndDate_null", 10023, "开始时间或结束时间不能为空", 400);
    public static final ApiException FAIL = new ApiException("FAIL", 10024, "操作失败", 400);
    
    public static final ApiException property_admin_not_found = new ApiException("property_admin_not_found", 11034, "没有该物业管理员", 400);
    public static final ApiException excel_data_duplicate = new ApiException("excel_data_duplicate", 20204, "excel中有重复项", 400);
    public static final ApiException download_file_error = new ApiException("download_file_error", 20205, "读取excel错误", 400);
    public static final ApiException file_name_is_null = new ApiException("file_name_is_null", 11030, "文件名为空", 400);
    public static final ApiException property_company_id_is_null= new ApiException("property_company_id_is_null", 20206, "物业公司id为空", 400);
    public static final ApiException community_id_is_null = new ApiException("community_id_is_null", 20207, "小区id为空", 400);
    public static final ApiException community_not_found = new ApiException("community_not_found", 11031, "没有该小区", 400);
    public static final ApiException read_excel_error = new ApiException("read_excel_error", 20208, "读取excel失败", 400);
    public static final ApiException upload_file_error = new ApiException("upload_file_error", 20209, "读取excel错误", 400);
    public static final ApiException appkey_is_exist = new ApiException("appkey_is_exist", 20210, "appkey已经存在", 400);
    public static final ApiException appkey_is_error = new ApiException("appkey_is_exist", 20211, "只能导入一条appkey数据", 400);
    public static final ApiException points_not_greater_than_credit_deductible = new ApiException("points_not_greater_than_credit_deductible", 20212, "不能大于商品抵扣积分", 400);
    public static final ApiException excel_file_is_null = new ApiException("excel_file_is_null", 20213, "excel内容为空", 400);
    public static final ApiException member_community_exists = new ApiException("member_community_exists", 20214, "该小区已存在", 400);
    public static final ApiException payment_channelId_null = new ApiException("payment_channelId_null", 20215, "支付渠道不能为空", 400);
    public static final ApiException payment_not_arrearage = new ApiException("payment_not_arrearage", 20216, "未欠费", 400);
    public static final ApiException excel_file_error = new ApiException("excel_file_error", 20216, "excel中数据为空，或者excel模板有误，请检查excel", 400);
    public static final ApiException property_company_not_found = new ApiException("property_company_not_found", 11032, "没有该物业公司", 400);
    public static final ApiException FileName_error = new ApiException("FileName_error", 20217, "文件名称不能为空", 400);
    public static final ApiException FileName_overlength = new ApiException("FileName_overlength", 20218, "文件名称不能超过20个字符", 400);
    public static final ApiException illegal_parameter_is_null  = new ApiException("Illegal parameter type， see doc for more info", 10029, "参数为空", 415);
    
    public ApiException(String error, int errorCode, String description, int httpStatusCode) {
        super(error);
        this.errorCode = errorCode;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiException(String error, int errorCode, String description, int httpStatusCode, Throwable cause) {
        super(error, cause);
        this.errorCode = errorCode;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return getMessage();
    }
}
