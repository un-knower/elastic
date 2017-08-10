package com.manji.elastic.common.util;

public class PointToDistance {
	// 地球半径  
	private static final double EARTH_RADIUS = 6370996.81;  
	// 弧度  
	private static double radian(double d) {  
		return d * Math.PI / 180.0;  
	}  
	/** 
	* @Description: 第二种方法 
	* @param lat1 
	* @param lng1 
	* @param lat2 
	* @param lng2    
	* @return void 
	*/  
	public static double distanceOfTwoPoints(String latlng1,String latlng2) {  
		double lat1 = Double.valueOf(latlng1.split(",")[0]);
		double lng1 = Double.valueOf(latlng1.split(",")[1]);
		double lat2 = Double.valueOf(latlng2.split(",")[0]);;
		double lng2 = Double.valueOf(latlng2.split(",")[1]);;
		double radLat1 = radian(lat1);  
		double radLat2 = radian(lat2);  
		double a = radLat1 - radLat2;  
		double b = radian(lng1) - radian(lng2);  
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
				+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));  
		s = s * EARTH_RADIUS;  
		return s;
    }  
    
    
    public static void main(String[] args) {
    	System.out.println(distanceOfTwoPoints("29.531779000000000,106.575711000000000", "29.540679999999998,106.566109000000000"));
	}
}
