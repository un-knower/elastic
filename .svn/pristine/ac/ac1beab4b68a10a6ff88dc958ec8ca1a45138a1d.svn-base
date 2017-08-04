package com.manji.elastic.common.tomcat;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;


/** 
* @ClassName: TomcatBootstrapHelper 
* @Description: 嵌入式tomcat helper
*/
public class TomcatBootstrapHelper {
    /** 
    * @Fields ENTER_CHAR: 回车字符
    */
    private static final char ENTER_CHAR = '\n';
    /** 
    * @Fields DEFAULT_PORT: 默认端口
    */
    private static final int DEFAULT_PORT = 8080;
    /** 
    * @Fields DEFULT_ENV: 默认环境
    */
    private static String DEFULT_ENV = "dev";
    /** 
    * @Fields port: 启动端口
    */
    private int port = DEFAULT_PORT;
    /** 
    * @Fields isServlet3Enable: 是否启用servlet 3.0 支持，如果启用的话，就需要扫描jar包中是否有Servlet等annotation，这个会影响启动时间，默认不开启
    */
    private boolean isServlet3Enable = false;

    /** 
    * @Title: TomcatBootstrapHelper 
    * @Description: 构造方法
    * @param @param port				端口
    * @param @param isServlet3Enable	是否启用servlet 3.0 支持
    * @param @param env					环境
    */
    public TomcatBootstrapHelper(int port, boolean isServlet3Enable, String env) {
        System.setProperty("spring.profiles.active", env);
        this.port = port;
        this.isServlet3Enable = isServlet3Enable;
    }

    /** 
    * @Title: TomcatBootstrapHelper 
    * @Description: 构造方法
    * @param @param port				端口
    * @param @param isServlet3Enable	是否启用servlet 3.0 支持
    */
    public TomcatBootstrapHelper(int port, boolean isServlet3Enable) {
        this(port, isServlet3Enable, DEFULT_ENV);
    }

    /** 
    * @Title: TomcatBootstrapHelper 
    * @Description: 构造方法
    * @param @param port				端口
    */
    public TomcatBootstrapHelper(int port) {
        this(port, false);
    }

    /** 
     * @Title: TomcatBootstrapHelper 
     * @Description: 构造方法
     */
    public TomcatBootstrapHelper() {
        this(DEFAULT_PORT);
    }

    /** 
    * @Title: start 
    * @Description:启动tomcat
    * @return void
    */
    public void start() {
        try {
            long begin = System.currentTimeMillis();
            Tomcat tomcat = new Tomcat();
            configTomcat(tomcat);
            tomcat.start();
            long end = System.currentTimeMillis();
            log(end - begin);


            //在控制台回车就可以重启，提高效率
            while (true) {
                char c = (char) System.in.read();
                if (c == ENTER_CHAR) {
                    begin = System.currentTimeMillis();
                    System.out.println("重启tomcat...");
                    tomcat.stop();
                    tomcat.start();
                    end = System.currentTimeMillis();
                    log(end - begin);
                }
            }
        } catch (Exception e) {
            System.err.println("非常抱歉，貌似启动挂了...");
            e.printStackTrace();
        }

    }

    /** 
    * @Title: configTomcat 
    * @Description: 配置tomcat参数
    * @param tomcat
    * @throws ServletException
    * @return void
    */
    private void configTomcat(final Tomcat tomcat) throws ServletException {
        tomcat.setBaseDir("target");
        tomcat.setPort(port);
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(port);
        connector.setURIEncoding("utf-8");
        tomcat.setConnector(connector);
        tomcat.getService().addConnector(connector);
        String webappPath = getWebappsPath();
        System.out.println("webapp目录：" + webappPath);
        Context ctx = tomcat.addWebapp("/", webappPath);
        StandardJarScanner scanner = (StandardJarScanner) ctx.getJarScanner();
        if (!isServlet3Enable) {
            scanner.setScanAllDirectories(false);
            scanner.setScanClassPath(false);
        }
        tomcat.setSilent(true);
    }

    /** 
    * @Title: log 
    * @Description:打印启动日志
    * @param time
    * @return void
    */
    private void log(long time) {
        System.out.println("********************************************************");
        System.out.println("启动成功: http://127.0.0.1:" + port + "   in:" + time + "ms");
        System.out.println("您可以直接在console里敲回车，重启tomcat,just have a try");
        System.out.println("********************************************************");
    }

    /** 
    * @Title: getWebappsPath 
    * @Description: 获取webapp路径
    * @return String
    */
    public String getWebappsPath() {
        //String file = getClass().getClassLoader().getResource(".").getFile();
        //return file.substring(0, file.indexOf("target")) + "src/main/webapp";
    	String xmFile = System.getProperty("user.dir");
        return xmFile.contains("elastic") ? xmFile + "/src/main/webapp" : xmFile + "/elastic/src/main/webapp";
    }
}
