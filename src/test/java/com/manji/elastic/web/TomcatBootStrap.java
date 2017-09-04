package com.manji.elastic.web;

import org.elasticsearch.client.Client;

import com.manji.elastic.common.tomcat.TomcatBootstrapHelper;

public class TomcatBootStrap {
	public static Client client132 = null;
	
	public static Client client49 = null;
	public static void main(String[] args) throws Exception {
		new TomcatBootstrapHelper(8097, false, "test").start();
	}
}