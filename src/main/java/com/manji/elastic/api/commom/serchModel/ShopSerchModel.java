package com.manji.elastic.api.commom.serchModel;

import com.manji.elastic.api.commom.BaseSerchModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class ShopSerchModel extends BaseSerchModel {
	private String location;
	private Integer distance_max = 999999999;//多少米内
	private String cate_id;
	private String busy_id;
	private String main_business_category_channel_id;
	private String area_code;
	private Integer sign_flag;
	private Integer open_flag;
	private Integer sort_flag;//排序规则 0距离排序，1评分排序 。
	
	@ApiModelProperty(value = "区域信息")
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@ApiModelProperty(value = "多少米内")
	public Integer getDistance_max() {
		return distance_max;
	}
	public void setDistance_max(Integer distance_max) {
		this.distance_max = distance_max;
	}
	@ApiModelProperty(value = "商家分类ID")
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	@ApiModelProperty(value = "商家主营分类")
	public String getBusy_id() {
		return busy_id;
	}
	public void setBusy_id(String busy_id) {
		this.busy_id = busy_id;
	}
	@ApiModelProperty(value = "商家主营分类的频道ID")
	public String getMain_business_category_channel_id() {
		return main_business_category_channel_id;
	}
	public void setMain_business_category_channel_id(String main_business_category_channel_id) {
		this.main_business_category_channel_id = main_business_category_channel_id;
	}
	@ApiModelProperty(value = "区域Code")
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	@ApiModelProperty(value = "是否签约")
	public Integer getSign_flag() {
		return sign_flag;
	}
	public void setSign_flag(Integer sign_flag) {
		this.sign_flag = sign_flag;
	}
	@ApiModelProperty(value = "是否开通")
	public Integer getOpen_flag() {
		return open_flag;
	}
	public void setOpen_flag(Integer open_flag) {
		this.open_flag = open_flag;
	}
	@ApiModelProperty(value = "排序规则-->> 0距离排序，1评分排序 。默认搜索不传，按照匹配度排序")
	public Integer getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(Integer sort_flag) {
		this.sort_flag = sort_flag;
	}
}
