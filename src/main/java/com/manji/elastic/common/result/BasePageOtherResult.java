package com.manji.elastic.common.result;


import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.domain.Page;


/**
 * Created by xsm on 2014/12/15.
 */
@SuppressWarnings("serial")
public class BasePageOtherResult<T,T1> extends BaseResult {

    private final static String DEFAULT_CODE = "SUCCESS";

    private Page<T> result;
    
    private T1 otherResult;
    
    private String requestId;

    public BasePageOtherResult() {
    	requestId = UUID.randomUUID().toString();
        setCode(DEFAULT_CODE);
    }

    public BasePageOtherResult(Page<T> page) throws Exception {
    	requestId = UUID.randomUUID().toString();
        setCode(DEFAULT_CODE);
        result = page;
    }

    public BasePageOtherResult(String Code, String message) {
        super(StringUtils.defaultString(Code), StringUtils.defaultString(message));
        requestId = UUID.randomUUID().toString();
    }

    public BasePageOtherResult(String code, String message , String catchStr) {
    	super(StringUtils.defaultString(code), StringUtils.defaultString(message),StringUtils.defaultString(catchStr));
    	requestId = UUID.randomUUID().toString();
    }
    
    public Page<T> getResult() {
        return result;
    }

    public void setResult(Page<T> result) {
        this.result = result;
    }

	public T1 getOtherResult() {
		return otherResult;
	}

	public void setOtherResult(T1 otherResult) {
		this.otherResult = otherResult;
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
