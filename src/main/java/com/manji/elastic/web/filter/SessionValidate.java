package com.manji.elastic.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
public class SessionValidate implements Filter {
	Set<String> urlSet = new HashSet<String>();
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String urls = filterConfig.getInitParameter("urls");
		String[] urlArray = urls.replace(" ", "").split(",");
		for (String url : urlArray) {
			if (StringUtils.isNotBlank(url)) {
				urlSet.add(url);
			}
		}
	}
	@Override
	public void destroy() {

	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI().trim().replaceAll("/{2,10}", "/"); 
		//获取项目路径
		String path = req.getContextPath();
		String basePath = "//"+req.getServerName()+path+"/";
		if(isIp(req.getServerName()) || "localhost".equals(req.getServerName())){
			basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path+"/";
		}
		req.setAttribute("basePath", basePath);
		String origin = "*";
		
		String referer = req.getHeader("Referer");
		if(StringUtils.isNotBlank(referer)){
			String[] arr = referer.split("/");
			if(arr.length>2){
				origin = StringUtils.join(arr, "/", 0, 3);
			}
		}
		//处理跨域
		resp.setHeader("Access-Control-Allow-Origin",origin);
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,jsessionid,Content-Type");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		
		//1、如果为含（非root下发布 含有项目名在前面）/login,/doLogin,/loginOut,/loginCodeServlet直接放行
		for(String s : urlSet){
			if(uri.contains(s)){
				chain.doFilter(request, response);
				return;
			}
		}
		//在不是指定的特殊里面情况 做判断
		if(	uri.startsWith(path+"/web/") && req.getSession().getAttribute("adminLoginStats")==null){
			resp.sendRedirect(basePath+"login");
			return;
		}
		
		chain.doFilter(request, response);
	}
	/**
	 * 判断是否是一个IP  
	 * @param IP
	 * @return
	 */
	public boolean isIp(String IP){
		boolean b = false;
		if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){  
			String s[] = IP.split("\\.");  
			if(Integer.parseInt(s[0])<255)  
				if(Integer.parseInt(s[1])<255)  
					if(Integer.parseInt(s[2])<255)  
						if(Integer.parseInt(s[3])<255)  
							b = true;  
		}  
		return b;  
	}
}
