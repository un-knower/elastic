package com.manji.elastic.common.result;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by xsm on 2014/12/15.
 */
@SuppressWarnings("serial")
public class BaseListResult<T> extends BaseResult {

    private final static String DEFAULT_CODE = "SUCCESS";

    private List<T> result;
    
    private String requestId;
    
    public BaseListResult() {
        setCode(DEFAULT_CODE);
        requestId = UUID.randomUUID().toString();
    }

    public BaseListResult(List<T> list) {
        setCode(DEFAULT_CODE);
        this.result = list;
        requestId = UUID.randomUUID().toString();
    }

    public BaseListResult(String code, String message) {
    	super(StringUtils.defaultString(code), StringUtils.defaultString(message));
    	requestId = UUID.randomUUID().toString();
    }
    
    public BaseListResult(String code, String message ,String catchStr) {
    	super(StringUtils.defaultString(code), StringUtils.defaultString(message),StringUtils.defaultString(catchStr));
    	requestId = UUID.randomUUID().toString();
	}

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
    
    public static String getDefaultCode() {
		return DEFAULT_CODE;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
