/**   
 * @Title: HttpClientUtil.java 
 * @Package com.yqsapp.sso.utils 
 * @Description: 
 * @date 2014-6-23 下午3:18:38   
 */
package com.manji.elastic.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>
 * HttpClient工具类
 * <p>
 * <p>
 * 实现基本的表单提交操作
 * </p>
 * 
 * @version 2014年12月8日 下午3:55:58
 * 
 */
public class HttpClientUtil {

	// private static String contentTypeJson = "application/json";
	/**
	 * 使用Get方式调用HTTP请求
	 *   GBK 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String getGBk(String url, Map<String, String> paramMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);

			// 封装请求参数
			String queryString = "";
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					queryString += "&" + key + "=" + value;
				}
			}
			if (StringUtils.isNotBlank(queryString)) {
				queryString = queryString.substring(1);
				getMethod.setQueryString(URIUtil.encodeQuery(queryString, "GBK"));
			}
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] resp = getMethod.getResponseBody();
			ret = new String(resp, "GBK");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 使用Get方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String get(String url, Map<String, String> paramMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);

			// 封装请求参数
			String queryString = "";
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					queryString += "&" + key + "=" + value;
				}
			}
			if (StringUtils.isNotBlank(queryString)) {
				queryString = queryString.substring(1);
				getMethod.setQueryString(URIUtil.encodeQuery(queryString, "UTF-8"));
			}
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] resp = getMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 使用Get方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String get(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					getMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			String queryString = "";
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					queryString += "&" + key + "=" + value;
				}
			}
			if (StringUtils.isNotBlank(queryString)) {
				queryString = queryString.substring(1);
				getMethod.setQueryString(URIUtil.encodeQuery(queryString, "UTF-8"));
			}
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] resp = getMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 使用POST方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String post(String url, Map<String, String> paramMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String post(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					postMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}
			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param requestBody
	 *            请求体内容
	 * @param contentType
	 *            请求体内容类型
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String post(String url, String requestBody, String contentType, Map<String, String> headerMap) {
		String ret=null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					postMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			RequestEntity requestEntity = new StringRequestEntity(requestBody, contentType, "utf-8");
			postMethod.setRequestEntity(requestEntity);

			//postMethod.setRequestBody(requestBody);

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 
	 * 方法描述： 创建人：slx-pc 创建时间：2014年10月31日 下午4:18:05
	 * 
	 * @param url
	 *            请求地址
	 * @param paramMap
	 *            请求参数
	 * @param encoding
	 *            字符编码
	 * @version
	 */
	public static String post(String url, Map<String, String> paramMap, String encoding) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, encoding);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTP请求上传文件
	 * <p>
	 * 调用示例：<br/>
	 * String url = "http://127.0.0.1:8080/fdfs/upload";<br/>
	 * Map<String, String> paramMap = new HashMap<String,String>();<br/>
	 * paramMap.put("uploadJson",
	 * "{'platformCode':'octopus','businessCode':'anypay'}");<br/>
	 * Map<String,String> fileMap = new HashMap<String,String>();<br/>
	 * fileMap.put("F:/pic.jpg", "file");<br/>
	 * fileMap.put("F:/pic1.jpg", "file");<br/>
	 * String ret = post(url, paramMap, fileMap, "UTF-8");<br/>
	 * </p>
	 * 
	 * @param url
	 * @param paramMap
	 *            非文件请求参数列表（key[参数名]:value[参数值]）
	 * @param fileMap
	 *            文件请求参数列表（key[文件本地绝对路径]:value[文件参数名]）
	 * @param encoding
	 *            响应报文编码
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @since 1.0.0
	 */
	public static String post(String url, Map<String, String> paramMap, Map<String, String> fileMap, String encoding) throws HttpException, IOException {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			List<Part> partList = new ArrayList<Part>();

			// 封装文件数据
			if (null != fileMap) {
				Set<String> paramSet = fileMap.keySet();
				for (String filePath : paramSet) {
					String fileParam = fileMap.get(filePath);
					File file = new File(filePath);
					FilePart fp = new FilePart(fileParam, file);
					partList.add(fp);
				}
			}

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					StringPart sp = new StringPart(key, value);
					partList.add(sp);
				}
			}

			Part[] parts = new Part[partList.size()];
			parts = partList.toArray(parts);

			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, encoding);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用Delete方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param requestBody
	 *            请求体内容
	 * @param contentType
	 *            请求体内容类型
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String delete(String url, String requestBody, String contentType, Map<String, String> headerMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		DeleteMethod deleteMethod = null;
		try {
			// 创建post方法
			deleteMethod = new DeleteMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					deleteMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			RequestEntity requestEntity = new StringRequestEntity(requestBody, contentType, "utf-8");
			deleteMethod.setRequestEntity(requestEntity);

			// postMethod.setRequestBody(requestBody);

			// 设置参数请求的编码
			deleteMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(deleteMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + deleteMethod.getStatusLine());
			}
			byte[] resp = deleteMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (deleteMethod != null) {
				deleteMethod.releaseConnection();
			}

		}
		return ret;
	}

	/**
	 * 使用Patch方式调用HTTP请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param requestBody
	 *            请求体内容
	 * @param contentType
	 *            请求体内容类型
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String patch(String url, String requestBody, String contentType, Map<String, String> headerMap) {
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PatchMethod patchMethod = null;
		try {
			// 创建post方法
			patchMethod = new PatchMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					patchMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			RequestEntity requestEntity = new StringRequestEntity(requestBody, contentType, "utf-8");
			patchMethod.setRequestEntity(requestEntity);

			// postMethod.setRequestBody(requestBody);

			// 设置参数请求的编码
			patchMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(patchMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + patchMethod.getStatusLine());
			}
			byte[] resp = patchMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (patchMethod != null) {
				patchMethod.releaseConnection();
			}
		}
		return ret;
	}

	
	/**
	 * 使用Get方式调用HTTPS请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String getHttps(String url, Map<String, String> paramMap) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);

			// 封装请求参数
			String queryString = "";
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					queryString += "&" + key + "=" + value;
				}
			}
			if (StringUtils.isNotBlank(queryString)) {
				queryString = queryString.substring(1);
				getMethod.setQueryString(URIUtil.encodeQuery(queryString, "UTF-8"));
			}
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] resp = getMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 使用Get方式调用HTTPS请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String getHttps(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					getMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			String queryString = "";
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					queryString += "&" + key + "=" + value;
				}
			}
			if (StringUtils.isNotBlank(queryString)) {
				queryString = queryString.substring(1);
				getMethod.setQueryString(URIUtil.encodeQuery(queryString, "UTF-8"));
			}
			getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			byte[] resp = getMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 使用POST方式调用HTTPS请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String postHttps(String url, Map<String, String> paramMap) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTPS请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param paramMap
	 *            请求的参数列表，必须转化为字符串的键值对
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String postHttps(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					postMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}
			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTPS请求
	 * 
	 * @param url
	 *            请求地址的URL
	 * @param requestBody
	 *            请求体内容
	 * @param contentType
	 *            请求体内容类型
	 * @param headerMap
	 *            请求的头信息，必须转化为字符串的键值对
	 * @return 返回响应的结果字符串
	 * @since 1.0.0
	 */
	public static String postHttps(String url, String requestBody, String contentType, Map<String, String> headerMap) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);  
		
		String ret=null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求头参数
			if (null != headerMap) {
				Set<String> headerNames = headerMap.keySet();
				for (String headerName : headerNames) {
					String headerValue = headerMap.get(headerName);
					postMethod.addRequestHeader(headerName, headerValue);
				}
			}
			// 封装请求参数
			RequestEntity requestEntity = new StringRequestEntity(requestBody, contentType, "utf-8");
			postMethod.setRequestEntity(requestEntity);

			//postMethod.setRequestBody(requestBody);

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			int statusCode = httpclient.executeMethod(postMethod);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, "UTF-8");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 
	 * 方法描述： 
	 * 创建人：slx-pc 创建时间：2014年10月31日 下午4:18:05
	 * @param url
	 *            请求地址
	 * @param paramMap
	 *            请求参数
	 * @param encoding
	 *            字符编码
	 * @version
	 */
	public static String postHttps(String url, Map<String, String> paramMap, String encoding) {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					postMethod.setParameter(key, value);
				}
			}

			// 设置参数请求的编码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);

			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, encoding);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	/**
	 * 使用POST方式调用HTTPS请求上传文件
	 * <p>
	 * 调用示例：<br/>
	 * String url = "http://127.0.0.1:8080/fdfs/upload";<br/>
	 * Map<String, String> paramMap = new HashMap<String,String>();<br/>
	 * paramMap.put("uploadJson",
	 * "{'platformCode':'octopus','businessCode':'anypay'}");<br/>
	 * Map<String,String> fileMap = new HashMap<String,String>();<br/>
	 * fileMap.put("F:/pic.jpg", "file");<br/>
	 * fileMap.put("F:/pic1.jpg", "file");<br/>
	 * String ret = post(url, paramMap, fileMap, "UTF-8");<br/>
	 * </p>
	 * 
	 * @param url
	 * @param paramMap
	 *            非文件请求参数列表（key[参数名]:value[参数值]）
	 * @param fileMap
	 *            文件请求参数列表（key[文件本地绝对路径]:value[文件参数名]）
	 * @param encoding
	 *            响应报文编码
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 * @since 1.0.0
	 */
	public static String postHttps(String url, Map<String, String> paramMap, Map<String, String> fileMap, String encoding) throws HttpException, IOException {
		//https 增加下面两行代码  
        Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);  
        Protocol.registerProtocol("https", myhttps);
		String ret = null;
		HttpClient httpclient = new HttpClient();
		PostMethod postMethod = null;
		try {
			// 创建post方法
			postMethod = new PostMethod(url);

			List<Part> partList = new ArrayList<Part>();

			// 封装文件数据
			if (null != fileMap) {
				Set<String> paramSet = fileMap.keySet();
				for (String filePath : paramSet) {
					String fileParam = fileMap.get(filePath);
					File file = new File(filePath);
					FilePart fp = new FilePart(fileParam, file);
					partList.add(fp);
				}
			}

			// 封装请求参数
			if (null != paramMap) {
				Set<String> keys = paramMap.keySet();
				for (String key : keys) {
					String value = paramMap.get(key);
					StringPart sp = new StringPart(key, value);
					partList.add(sp);
				}
			}

			Part[] parts = new Part[partList.size()];
			parts = partList.toArray(parts);

			MultipartRequestEntity mre = new MultipartRequestEntity(parts, postMethod.getParams());
			postMethod.setRequestEntity(mre);
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int statusCode = httpclient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			byte[] resp = postMethod.getResponseBody();
			ret = new String(resp, encoding);
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return ret;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	public static void main(String[] args) throws HttpException, IOException {

		// String url = "http://127.0.0.1:8086/permission/operator/add";
		// String requestBody =
		// "{'businesscode':'asda','controlCode':'super','operatorLogin':'test2','operatorName':'test123','operatorPwd':'123456'}";
		// String contentType = "application/json";
		// String ret = post(url, requestBody, contentType, null);
		// System.out.println(ret);

		// String url = "http://127.0.0.1:8080/fdfs/upload";
		// Map<String, String> paramMap = new HashMap<String,String>();
		// paramMap.put("uploadJson",
		// "{'serialNumber':'"+System.currentTimeMillis()+"','platformCode':'octopus','businessCode':'anypay'}");
		// Map<String,String> fileMap = new HashMap<String,String>();
		// fileMap.put("F:/pic.jpg", "file");
		// fileMap.put("F:/pic1.jpg", "file");
		// String ret = post(url, paramMap, fileMap, "UTF-8");
		// System.out.println(ret);

		/*
		 * String url1 = "http://192.168.0.101:10000/fdfs/delete"; Map<String,
		 * String> paramMap1 = new HashMap<String,String>();
		 * paramMap1.put("deleteJson",
		 * "{'serialNumber':'"+System.currentTimeMillis()+
		 * "','platformCode':'octopus','businessCode':'anypay','imgIdList':[13,14]}"
		 * );
		 * 
		 * String ret1 = post(url1, paramMap1, "UTF-8");
		 * System.out.println(ret1);
		 */

		/*
		 * String url =
		 * "http://192.168.0.100:22222/purge/group1/M00/00/00/wKgAZFSBUGOAFCxbAAFKrcZzWYg030.jpg"
		 * ; String ret = get(url, null); System.out.println(ret);
		 */

		/*String url = "http://192.168.0.101:7020/images/youfang";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("imagePaths", "/images/dolphin/0404d23c-ff47-4454-8f82-59c5f8f83c620.jpg");
		String ret = delete(url, "{'imagePaths':'/images/dolphin/0404d23c-ff47-4454-8f82-59c5f8f83c620.jpg'}", "application/json", null);
		System.out.println(ret);*/
		
		

		/*String url="https://a1.easemob.com/mipu/mipu/users";
		System.out.println(url);
		String requestBody ="{\"username\":\"xiangshimingadfsddssdaa\",\"password\":\"123456\"}";
		String contentType = "application/json";
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", "Bearer YWMtffv0BAtaEeWfcndEabW1zQAAAU73xBPB3qPAGHPtIBV_FtSls96Zqp6TjNs");
		String abc=post(url, requestBody, contentType,headerMap);
		System.out.println(abc);*/
			
		String url="https://api.im.jpush.cn/v1/users";
		System.out.println(url);
		String requestBody ="[{\"username\": \"dev_fang2012\", \"password\": \"password\"}, {\"username\": \"dev_fang2013\", \"password\": \"password\"}]";
		String contentType = "application/json";
		Map<String,String> headerMap=new HashMap<String,String>();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Authorization", "Basic ZDM2Y2ZiYTA1NGEyNDMxYmYzYjYxYjBjOjU5Y2EyNzIzMTdkYmNiYzEyNzY1M2VkOA==");
		String abc=postHttps(url, requestBody, contentType,headerMap);
		System.out.println(abc);
	}
}
