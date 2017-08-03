
			// 获取传入的地理位置
			String location = query.getLocation();
			String lat = "";
			String lon = "";
			if ("" != location) {
				lat = location.split(",")[0];
				lon = location.split(",")[1];
			} else {
				return "{\"data\":\"地理位置未传\"}";
			}
			int from = (query.getPageNum() - 1) * query.getSize();
			int end =query.getPageNum()* query.getSize();
			StringBuffer sb = new StringBuffer("{\"query\": {\"bool\": {\"must\": [");
			// 关键字
			if (!"".equals(query.getQueryStr())) {
				sb.append("{ \"match\": { \"shopinfo_index\": \"" + query.getQueryStr() + "\" } }");
			}
			// 商家主营分类
			if (!"".equals(query.getBusy_id())) {
				sb.append(",{ \"match\": { \"main_business\": \"" + query.getBusy_id() + "\" } }");
			}
			// 商家分类
			if (!"".equals(query.getCate_id())) {
				sb.append(",{ \"match\": { \"scope_values\": \"" + query.getCate_id() + "\" } }");
			}
			// 是否签约
			if (0 != query.getSign_flag()) {
				sb.append(",{ \"match\": { \"is_sign_up\": \"" + query.getSign_flag() + "\" } }");
			}
			if(1 ==query.getOpen_flag()){
				sb.append(",{ \"match\": { \"dpkg\": \"" + 1 + "\" } }");
			}
			// 区域
			if (!"".equals(query.getArea_code())&&query.getArea_code() !=null) {
				sb.append(",{\"match\": { \"area_code\": \"" + query.getArea_code() + "\" } }");
			}
			sb.append("]");
			// 搜索附近最大区域
			if (0 == query.getDistance_max()) {
				sb.append(",\"filter\":{\"geo_distance\":{\"distance\":\"1000m\",\"latlng\":{\"lat\":" + lat + ",\"lon\":"
						+ lon + "}}}");
			} else {
				sb.append(",\"filter\":{\"geo_distance\":{\"distance\":\"" + query.getDistance_max()
						+ "m\",\"latlng\":{\"lat\":" + lat + ",\"lon\":" + lon + "}}}");
			}
			sb.append("}}");
			// 排序方式
			if (0 == query.getSort_flag()) {
				sb.append(",\"sort\":{\"_geo_distance\":{\"latlng\":{\"lat\":\"" + lat + "\",\"lon\":\"" + lon
						+ "\"},\"order\": \"asc\",\"unit\":\"m\"}}");
			} else {
				// service_review_score
				sb.append(",\"sort\":{\"review_score\":\"desc\",\"_geo_distance\":{\"latlng\":{\"lat\":\"" + lat + "\",\"lon\":\"" + lon
						+ "\"},\"order\": \"asc\",\"unit\":\"m\"}}");
			}
			StringBuffer tempSb =sb;
			sb.append(",\"size\": " + query.getSize() + ",\"from\": " + from + "}");
			
			String esReturn = HttpClientUtil.post(Configure.getEsUrl()+"article"+"/_search", sb.toString().replace("must\": [,", "must\": ["), "application/json", null);
			
			JSONObject obj = JSONObject.parseObject(esReturn);
			JSONObject returnObj = obj.getJSONObject("hits");
			//签约商家查询完毕
			int signCount =returnObj.getIntValue("total");
			//签约商家数量不足
			if(end >signCount){
				if(end -signCount<query.getSize()){
					JSONArray hits =returnObj.getJSONArray("hits");
					int hitsCount =0;
					if(null !=hits){
						hitsCount =hits.size();
					}
					int deCount =query.getSize() -hitsCount;
					tempSb.append(",\"size\": " + deCount + ",\"from\": " + 0 + "}");
					String tempEsReturn = loaderExtra(tempSb);
					JSONObject tempObj = JSONObject.parseObject(tempEsReturn);
					JSONObject tempReturnObj = tempObj.getJSONObject("hits");
					JSONArray extraHits =tempReturnObj.getJSONArray("hits");
					for(int i =0;i<extraHits.size();i++){
						JSONObject extraObj =extraHits.getJSONObject(i);
						returnObj.getJSONArray("hits").add(i+hitsCount, extraObj);
					}
				}else{
					int extraStart =end -signCount-query.getSize();
					tempSb.append(",\"size\": " + query.getSize() + ",\"from\": " + extraStart + "}");
					String tempEsReturn = loaderExtra(tempSb);
					JSONObject tempObj = JSONObject.parseObject(tempEsReturn);
					JSONObject tempReturnObj = tempObj.getJSONObject("hits");
					returnObj =tempReturnObj;
				}
			}
			//返回信息
	        return returnObj;