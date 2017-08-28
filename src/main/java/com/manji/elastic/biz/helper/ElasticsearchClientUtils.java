package com.manji.elastic.biz.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;

import com.manji.elastic.common.global.Configure;

@SuppressWarnings("resource")
public class ElasticsearchClientUtils {
	
	public static TransportClient client = null;
	
	/**
	 * 获得链接
	 * @return
	 */
	public static TransportClient getTranClinet(){
		if(null == client){
			// 设置集群名称
			/*Settings settings = Settings.builder().put("cluster.name", "mj-es").build();*/
			
			Settings settings = Settings.builder().put("cluster.name", "my-application")
	                .put("xpack.security.transport.ssl.enabled", false)
	                .put("xpack.security.user", "elastic:changeme")
	                .put("client.transport.sniff", true).build();
			
			// 创建client
			try {
				/*client = new PreBuiltTransportClient(settings)*/
				client = new PreBuiltXPackTransportClient(settings)
						.addTransportAddress(new InetSocketTransportAddress(
								InetAddress.getByName(Configure.getEsLocation()), Integer.valueOf(Configure.getEStrans_PORT())));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("创建连接成功~~~~~~~~~~~~~~~");
		}
		return client;
	}
	/**
	 * 关闭连接
	 */
	public static void stopClient(){
		if(null != client){
			try{
				client.close();
			}catch(Exception e){
				
			}finally {
				client = null;
			}
		}
	}
	/**
	 * 重新开启连接
	 */
	public static void startClient(){
		try{
			stopClient();
		}catch(Exception e){
			
		}finally {
			getTranClinet();
		}
	}
}
