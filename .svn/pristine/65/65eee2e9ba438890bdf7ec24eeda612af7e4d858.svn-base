package com.manji.elastic.common.result;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@SuppressWarnings("serial")
public class BaseObjectResult<T> extends BaseResult {

    private final static String DEFAULT_CODE = "SUCCESS";

    private T result;
    
    private String requestId;
    
    public BaseObjectResult() {
        setCode(DEFAULT_CODE);
        requestId = UUID.randomUUID().toString();
    }

    public BaseObjectResult(T object) {
        this.result = object;
        setCode(DEFAULT_CODE);
        requestId = UUID.randomUUID().toString();
    }

    public BaseObjectResult(String code, String message) {
    	super(StringUtils.defaultString(code), StringUtils.defaultString(message));
    	requestId = UUID.randomUUID().toString();
    }
    
    public BaseObjectResult(String code, String message , String catchStr) {
    	super(StringUtils.defaultString(code), StringUtils.defaultString(message),StringUtils.defaultString(catchStr));
    	requestId = UUID.randomUUID().toString();
    }
   

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
    
    public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public static String getDefaultCode() {
		return DEFAULT_CODE;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
