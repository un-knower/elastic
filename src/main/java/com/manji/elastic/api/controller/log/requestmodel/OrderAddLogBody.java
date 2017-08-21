package com.manji.elastic.api.controller.log.requestmodel;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OrderAddLogBody {
	private Long order_id;
	
	private String order_no;
	
	private String event_source;
	
	private String event_arg;
	
	private String event_context;
	
	private String remark;
	
	private String rowAddTime;

	@ApiModelProperty(value = "订单ID")
	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	@ApiModelProperty(value = "订单号")
	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	@ApiModelProperty(value = "事件源")
	public String getEvent_source() {
		return event_source;
	}

	public void setEvent_source(String event_source) {
		this.event_source = event_source;
	}
	@ApiModelProperty(value = "事件参数")
	public String getEvent_arg() {
		return event_arg;
	}

	public void setEvent_arg(String event_arg) {
		this.event_arg = event_arg;
	}
	@ApiModelProperty(value = "关联数据")
	public String getEvent_context() {
		return event_context;
	}

	public void setEvent_context(String event_context) {
		this.event_context = event_context;
	}

	@ApiModelProperty(value = "备注")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@ApiModelProperty(value = "数据添加时间，格式yyyy-MM-dd hh:mm:ss")
	public String getRowAddTime() {
		return rowAddTime;
	}

	public void setRowAddTime(String rowAddTime) {
		this.rowAddTime = rowAddTime;
	}
	
}
