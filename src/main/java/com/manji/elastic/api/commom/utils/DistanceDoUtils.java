package com.manji.elastic.api.commom.utils;

import org.elasticsearch.search.DocValueFormat;
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
			Object[] sortValues = {juli};
			try{
				//FormatDateTimeFormatter formatter = Joda.forPattern("dateOptionalTime");
				
				DocValueFormat format = new DocValueFormat.Decimal("666.6666");//.DateTime(formatter, DateTimeZone.UTC);
				
				DocValueFormat[] sortValueFormats ={format};
				
				arry.sortValues(new SearchSortValues(sortValues, sortValueFormats));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return hits;
	}
}
