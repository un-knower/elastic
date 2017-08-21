package com.manji.elastic.biz.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.manji.elastic.common.global.Configure;

@SuppressWarnings("resource")
public class ElasticsearchClientUtilsSS {
	
	public static TransportClient client = null;
	
	static{
		if(null == client){
			// 设置集群名称
			Settings settings = Settings.builder().put("cluster.name", "mj-es").build();
			// 创建client
			try {
				client = new PreBuiltTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(
								InetAddress.getByName(Configure.getEsLocation()), Integer.valueOf(Configure.getEStrans_PORT())));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("创建连接成功~~~~~~~~~~~~~~~");
		}
	}
	/**
	 * 获得链接
	 * @return
	 */
	public static TransportClient getTranClinet(){
		return client;
	}
}
