package com.manji.elastic.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manji.elastic.common.global.Configure;

@SuppressWarnings("resource")
public class ElasticsearchClientUtils {
	public static TransportClient client = null;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	static{
		if(null == client){
			 // 设置集群名称
			Settings settings = Settings.builder().put("cluster.name", "mj-es").build();
			// 创建client
			try {
				client = new PreBuiltTransportClient(settings)
				        .addTransportAddress(new InetSocketTransportAddress(
				InetAddress.getByName(Configure.getEsLocation()), Integer.valueOf(Configure.getESscoke_PORT())));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("创建连接成功~~~~~~~~~~~~~~~");
		}
	}
	
	
	public static TransportClient getTranClinet(){
		return client;
	}
}
