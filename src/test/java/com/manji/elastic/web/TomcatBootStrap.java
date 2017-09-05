package com.manji.elastic.web;

import com.manji.elastic.common.tomcat.TomcatBootstrapHelper;

public class TomcatBootStrap {
	public static void main(String[] args) throws Exception {
		new TomcatBootstrapHelper(8097, false, "test").start();
	}
}