package com.manji.elastic.api.commom.serchModel;

import com.manji.elastic.api.commom.BaseSerchModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class ShopCommoditySerchModel extends BaseSerchModel {
	//筛选条件参数
	private String shop_cate_id;//商家分类ID
	private String act_flag;///折扣类型1、折扣2、满折3、满减劵4、满额返劵5、指定赠6、买赠7、满赠8、包邮
	private Integer shop_id;//商家ID
	//排序方式
	private Integer sort_flag;//不传按照匹配排序，0综合排序，1，销量排序，2，按照价格降序排序，3，按照价格升序排序,4按照商家综合排序，5按照商家销量排序
	
	@ApiModelProperty(value = "商家分类ID")
	public String getShop_cate_id() {
		return shop_cate_id;
	}
	public void setShop_cate_id(String shop_cate_id) {
		this.shop_cate_id = shop_cate_id;
	}
	@ApiModelProperty(value = "折扣类型1、折扣2、满折3、满减劵4、满额返劵5、指定赠6、买赠7、满赠8、包邮")
	public String getAct_flag() {
		return act_flag;
	}
	public void setAct_flag(String act_flag) {
		this.act_flag = act_flag;
	}
	@ApiModelProperty(value = "商家ID")
	public Integer getShop_id() {
		return shop_id;
	}
	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	@ApiModelProperty(value = "不传按照匹配排序，0综合排序，1，销量排序，2，按照价格降序排序，3，按照价格升序排序,4按照商家综合排序，5按照商家销量排序")
	public Integer getSort_flag() {
		return sort_flag;
	}
	public void setSort_flag(Integer sort_flag) {
		this.sort_flag = sort_flag;
	}
}
