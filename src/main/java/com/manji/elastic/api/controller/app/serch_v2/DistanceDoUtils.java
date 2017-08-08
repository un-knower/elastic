package com.manji.elastic.api.controller.app.serch_v2;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchSortValues;

import com.manji.elastic.common.util.PointToDistance;

public class DistanceDoUtils {
	
	/**
	 *计算距离
	 * @param hits
	 * @return
	 */
	public static SearchHits computerJl(String location,SearchHits hits){
		SearchHit[] arrays = hits.getHits();
		for (SearchHit arry : arrays) {
			//拿出当前这条数据的latlng信息
			String thisLatLng = arry.getSource().get("latlng").toString();
			//计算距离
			double juli = PointToDistance.distanceOfTwoPoints(location, thisLatLng);
			//设置进去
			String[] sortValues = {String.valueOf(juli)}; 
			SearchSortValues aa = new SearchSortValues(sortValues, null);
			arry.sortValues(aa);
		}
		return hits;
	}
}
