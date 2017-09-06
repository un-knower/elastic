package com.manji.elastic.api.commom.serchModel;

import java.util.List;

import com.manji.elastic.api.commom.BaseSerchModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class PcCommoditySerchModel extends BaseSerchModel {
	//筛选条件参数
	private String cate_id;//分类ID
	private List<String> brand_code;//多选品牌ID
	private String area_code;//地区码。
	private Integer sale_flag;///是否折扣
	private Integer price_start = 0; //筛选初始金额
	private Integer price_end;//筛选结束金额 ，默认不传。
	private String shop_cate_id ;//商家分类ID
	private String article_user_category_id;//商家分类ID
	private Integer shop_id;//商家ID
	private String act_flag;///折扣类型1、折扣2、满折3、满减劵4、满额返劵5、指定赠6、买赠7、满赠8、包邮
	//包邮特殊逻辑
	private Integer ship_flag=0;//是否包邮，默认为0，1为包邮
	private String dis_area_code;//配送地区
	//排序方式
	private Integer sort_flag;//默认不传匹配排序>>>0综合排序，1，销量排序，2，按照价格降序排序，3，按照价格升序排序,4按照商家综合排序，5按照商家销量排序
	
	@ApiModelProperty(value = "分类ID")
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	@ApiModelProperty(value = "多选品牌ID")
	public List<String> getBrand_code() {
		return brand_code;
	}
	public void setBrand_code(List<String> brand_code) {
		this.brand_code = brand_code;
	}
	@ApiModelProperty(value = "地区码")
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	@ApiModelProperty(value = "是否折扣")
	public Integer getSale_flag() {
		return sale_flag;
	}
	public void setSale_flag(Integer sale_flag) {
		this.sale_flag = sale_flag;
	}
	@ApiModelProperty(value = "筛选初始金额默认0")
	public Integer getPrice_start() {
		return price_start;
	}
	public void setPrice_start(Integer price_start) {
		this.price_start = price_start;
	}
	@ApiModelProperty(value = "筛选结束金额 ，默认不传")
	public Integer getPrice_end() {
		return price_end;
	}
	public void setPrice_end(Integer price_end) {
		this.price_end = price_end;
	}
	@ApiModelProperty(value = "商家ID")
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	@ApiModelProperty(value = "折扣类型1、折扣2、满折3、满减劵4、满额返劵5、指定赠6、买赠7、满赠8、包邮")
	public String getAct_flag() {
		return act_flag;
	}
	public void setAct_flag(String act_flag) {
		this.act_flag = act_flag;
	}
	@ApiModelProperty(value = "商家分类ID")
	public String getShop_cate_id() {
		return shop_cate_id;
	}
	public void setShop_cate_id(String shop_cate_id) {
		this.shop_cate_id = shop_cate_id;
	}
	@ApiModelProperty(value = "商家分类ID")
	public String getArticle_user_category_id() {
		return article_user_category_id;
	}
	public void setArticle_user_category_id(String article_user_category_id) {
		this.article_user_category_id = article_user_category_id;
	}
	@ApiModelProperty(value = "是否包邮，0不包邮，1为包邮")
	public Integer getShip_flag() {
		return ship_flag;
	}
	public void setShip_flag(Integer ship_flag) {
		this.ship_flag = ship_flag;
	}
	@ApiModelProperty(value = "配送地区")
	public String getDis_area_code() {
		return dis_area_code;
	}
	public void setDis_area_code(String dis_area_code) {
		this.dis_area_code = dis_area_code;
	}
	@ApiModelProperty(value = "默认不传匹配排序>>>0综合排序，1，销量排序，2，按照价格降序排序，3，按照价格升序排序,4按照商家综合排序，5按照商家销量排序")
	public Integer getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(Integer sort_flag) {
		this.sort_flag = sort_flag;
	}
}
