package com.manji.elastic.web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestSocke {
	public static void main(String[] args) {
		/*try{
			
				String jsonJoin = "{\"CMD\": \"JOIN\", \"CmdSeq\": 1, \"AppEUI\": \"2c26c501e9000003\", \"AppNonce\": 1234, \"Challenge\": \"993b72d04d9171480ad572a4522e173d\"}";
				
				//1.创建客户端Socket，指定服务器地址和端口
	            Socket socket=new Socket("msp02.claaiot.com",30002);
	            //2.获取输出流，向服务器端发送信息
	            OutputStream os=socket.getOutputStream();//字节输出流
	            PrintWriter pw=new PrintWriter(os);//将输出流包装为打印流
	            pw.write("\n" + jsonJoin.length() + "\n" + jsonJoin +"\n");

	            socket.shutdownOutput();//关闭输出流
	            //3.获取输入流，并读取服务器端的响应信息
	            InputStream is=socket.getInputStream();
	            BufferedReader br=new BufferedReader(new InputStreamReader(is));
	            String info=null;
	            while((info=br.readLine())!=null){
	                System.out.println("我是客户端，服务器说："+info);
	            }
	            //4.关闭资源
	            br.close();
	            is.close();
	            pw.close();
	            os.close();
	            socket.close();
			String jsonJoin = "{\"CMD\": \"JOIN\", \"CmdSeq\": 1, \"AppEUI\": \"2c26c501e9000003\", \"AppNonce\": 1234, \"Challenge\": \"993b72d04d9171480ad572a4522e173d\"}";
	        System.out.println("客户端启动...");    
	        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");   
	        while (true) {    
	            Socket socket = null;  
	            try {  
	                //创建一个流套接字并将其连接到指定主机上的指定端口号  
	                socket = new Socket("msp02.claaiot.com",30002);
	                    
	                //读取服务器端数据    
	                DataInputStream input = new DataInputStream(socket.getInputStream());    
	                //向服务器端发送数据    
	                DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
	                String str = "\n" + jsonJoin.length() + "\n" + jsonJoin +"\n";    
	                out.writeUTF(str);    
	                String ret = input.readUTF();     
	                System.out.println("服务器端返回过来的是: " + ret);    
	                // 如接收到 "OK" 则断开连接    
	                if ("OK".equals(ret)) {    
	                    System.out.println("客户端将关闭连接");    
	                    Thread.sleep(500);    
	                    break;    
	                }    
	                  
	                out.close();  
	                input.close();  
	            } catch (Exception e) {  
	                System.out.println("客户端异常:" + e.getMessage());   
	            } finally {  
	                if (socket != null) {  
	                    try {  
	                        socket.close();  
	                    } catch (IOException e) {  
	                        socket = null;   
	                        System.out.println("客户端 finally 异常:" + e.getMessage());   
	                    }  
	                }  
	            }  
	        }    
	        
	        
		}catch(Exception e) {
			System.out.println("Error"+e); //出错，则打印出错信息
		}*/
		
		try {
			String jsonJoin = "{\"CMD\": \"JOIN\", \"CmdSeq\": 1, \"AppEUI\": \"2c26c501e9000003\", \"AppNonce\": 1234, \"Challenge\": \"993b72d04d9171480ad572a4522e173d\"}";
			            // 1、创建客户端Socket，指定服务器地址和端口
			            Socket socket = new Socket("msp02.claaiot.com",30002);
			            System.out.println("客户端启动成功");
			            // 2、获取输出流，向服务器端发送信息
			            // 向本机的52000端口发出客户请求
			            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			            // 由系统标准输入设备构造BufferedReader对象
			            PrintWriter write = new PrintWriter(socket.getOutputStream());
			            // 由Socket对象得到输出流，并构造PrintWriter对象
			            //3、获取输入流，并读取服务器端的响应信息 
			            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			            // 由Socket对象得到输入流，并构造相应的BufferedReader对象
			            String readline;
			            readline = br.readLine(); // 从系统标准输入读入一字符串
			            while (!readline.equals("end")) {
			                // 若从标准输入读入的字符串为 "end"则停止循环
			                write.println(readline);
			                // 将从系统标准输入读入的字符串输出到Server
			                write.flush();
			                // 刷新输出流，使Server马上收到该字符串
			                System.out.println("Client:" + readline);
			                // 在系统标准输出上打印读入的字符串
			                System.out.println("Server:" + in.readLine());
			                // 从Server读入一字符串，并打印到标准输出上
			                readline = br.readLine(); // 从系统标准输入读入一字符串
			            } // 继续循环
			            //4、关闭资源 
			            write.close(); // 关闭Socket输出流
			            in.close(); // 关闭Socket输入流
			            socket.close(); // 关闭Socket
			        } catch (Exception e) {
			            System.out.println("can not listen to:" + e);// 出错，打印出错信息
			        }
			    }
}
