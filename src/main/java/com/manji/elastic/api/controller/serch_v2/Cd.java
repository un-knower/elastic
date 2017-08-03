	
			/*int from = (query.getPageNum() - 1) * query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字
			if (!"".equals(query.getQueryStr())) {
				sb.append(",{ \"match\": { \"shopinfo_index\": \"" + query.getQueryStr() + "\" } }");
			}
			// 分类ID
			if (!"".equals(query.getCate_id())) {
				sb.append(",{ \"match\": { \"scope_values\": \"" + query.getCate_id() + "\" } }");
			}
			// 商家店铺开启状态
			if(1 ==query.getOpen_flag()){
				sb.append(",{ \"match\": { \"dpkg\": \"" + 1 + "\" } }");
			}
			// 区域
			sb.append(",{\"match\": { \"area_code\": \"" + areaCode + "\" } }");
			// if (!"".equals(query.getArea_code())) {
			//
			// sb.append(",{\"match\": { \"area\": \"" + query.getArea_code() + "\"
			// } }");
			// }
			sb.append("]");
			sb.append("}}");
			sb.append(",\"sort\":{\"hot\":\"asc\"}");
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}");
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"shop_hot"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			JSONObject jsonObj = JSON.parseObject(esReturn);  
			JSONObject result = (JSONObject) jsonObj.get("hits");
	        return result;*/