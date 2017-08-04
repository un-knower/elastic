package com.manji.elastic.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;

import com.manji.elastic.common.exception.BusinessDealException;

/**
 * @项目名称：
 * @类名称：FileuploadHelper 
 * @类描述：org.apache.commons.fileupload和 Spring MVC的上传文件hleper
 * @创建人：XSM 
 * @创建时间：2015-01-18 上午9:24:55
 */
public class FileuploadHelper {
	
	/**
     * 上传文件 	(Spring MVC 上传)
     * @param multipartFile
     * @param request
     * @return
     */
    public static String upload(MultipartFile multipartFile, HttpServletRequest request,String uploadPath,String FileName) throws Exception{
    	//文件原名（用来获取后缀）
    	String sourseFileName = multipartFile.getOriginalFilename();
        //文件后缀
        String fileEnd = sourseFileName.substring(sourseFileName.lastIndexOf(".") + 1).toLowerCase();
    	File tempFile = new File(uploadPath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        multipartFile.transferTo(new File(uploadPath + "/" + FileName+"."+fileEnd));
        //上传成功 返回文件全名
        return FileName+"."+fileEnd;
    }
	
	 /**
     * 文件验证，没尺寸限制	(Spring MVC 上传验证)
     * @param multipartFile
     * @return
	 * @throws IOException 
     */
    public static void validate(MultipartFile multipartFile,String fileType,String maxSize) throws Exception {
    	long size = multipartFile.getSize();
	    if(size==0){
	    	throw new BusinessDealException("请选择要上传的文件");
	    }
    	//文件名
    	String fileName = multipartFile.getOriginalFilename();
        //文件后缀
        String fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        Set<String> typeSet = new HashSet<String>();
		String[]  types = fileType.split("\\|");
		for(String type:types){
			typeSet.add(type);
		}
		//判断文件类型是否是规定的文件类型 
		if(!typeSet.contains(fileEnd.toLowerCase())){
			throw new BusinessDealException("上传文件格式不对,支持"+fileType+"格式");
		}
        if (size > Double.parseDouble(maxSize)*1024*1024) {
        	throw new BusinessDealException("上传文件太大,不得超过"+maxSize+"M");
        }
    }
    /**
     * 文件验证，没尺寸限制	(Spring MVC 上传验证)
     * @param multipartFile
     * @return
	 * @throws IOException 
     */
    public static void validateType(MultipartFile multipartFile,String fileType,String maxSize) throws Exception {
    	long size = multipartFile.getSize();
	    if(size==0){
	    	throw new BusinessDealException("请选择要上传的文件");
	    }
        //文件后缀
        String filemini = multipartFile.getContentType();
        Set<String> typeSet = new HashSet<String>();
		String[] types = fileType.split("\\|");
		for(String type:types){
			typeSet.add(type.toLowerCase());
		}
		//判断文件类型是否是规定的文件类型 
		if(!typeSet.contains(filemini.toLowerCase())){
			throw new BusinessDealException("上传文件格式不对,支持"+fileType);
		}
        if (size > Double.parseDouble(maxSize)*1024*1024) {
        	throw new BusinessDealException("上传文件太大,不得超过"+maxSize+"M");
        }
    }
    /**
     * 文件（图片）验证，有尺寸限制  	(Spring MVC 上传验证)
     * @param multipartFile
     * @return 返回得到的图片尺寸
	 * @throws IOException 
     */
    public static HashMap<String,Object> validate(MultipartFile multipartFile,String fileType,String maxSize,Integer w,Integer h) throws Exception {
    	long size = multipartFile.getSize();
	    if(size==0){
	    	throw new BusinessDealException("请选择要上传的文件");
	    }
    	HashMap<String,Object> map=new HashMap<String,Object>();
    	//文件名
    	String fileName = multipartFile.getOriginalFilename();
        //文件后缀
        String fileEnd = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        Set<String> typeSet = new HashSet<String>();
		String[]  types = fileType.split("\\|");
		for(String type:types){
			typeSet.add(type);
		}
		//判断文件类型是否是规定的文件类型 
		if(!typeSet.contains(fileEnd.toLowerCase())){
			throw new BusinessDealException("上传文件格式不对,支持"+fileType+"格式");
		}
        if (size > Double.parseDouble(maxSize)*1024*1024) {
        	throw new BusinessDealException("上传文件太大,不得超过"+maxSize+"M");
        }
        //如果有尺寸限制 判断尺寸
        BufferedImage bufferedImg = ImageIO.read(multipartFile.getInputStream());
    	int imgW=0;
    	int imgH=0;
    	 try{imgW=bufferedImg.getWidth();}catch(Exception e){throw new BusinessDealException("非正确的图片格式，检查是否损坏");}
         try{imgH=bufferedImg.getHeight();}catch(Exception e){throw new BusinessDealException("非正确的图片格式，检查是否损坏");}
    	if((imgW<w)||(imgH<h)){
    		throw new BusinessDealException("图片太小,图片必须大于等于:"+w+"x"+h);
		}
        map.put("w", imgW);
        map.put("h", imgH);
        return map;
    }
	
	/**
	 * org.apache 上传(本项目中不使用  只做demo)
	 * @param request
	 * @param uploadPath 文件路径
	 * @param FileName   要保存的文件名（不含后缀）
	 * @param imageMaxSize  允许最大多少M
	 * @param imageType    允许上传类型 jpg|png|bmg ‘|’ 分开 定义
	 * @return	返回保存的文件全名（包含后缀名）
	 * @throws Exception
	 */
	public static String uploadFile(HttpServletRequest request,String uploadPath,String FileName,String imageMaxSize ,String imageType)throws Exception{
		Set<String> typeSet = new HashSet<String>();
		String[]  types = imageType.split("\\|");
		for(String type:types){
			typeSet.add(type);
		}
		//这个是存储非上传文件表单的值，name=表单名字  value=表单值
		Map<String,String> values = new HashMap<String,String>();
		File f = new File(uploadPath);
		if(!f.exists()){//查找文件夹是否存在，不存在则创建
			f.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory();//为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
		ServletFileUpload upload = new ServletFileUpload(factory);
		@SuppressWarnings("unchecked")
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> itr = items.iterator();
		FileItem uploadItem = null;
		//检查当前项目是普通表单项目还是上传文件。
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if (item.isFormField()) {//如果是普通表单项目
				values.put(item.getFieldName(),new String(item.getString().getBytes("ISO-8859-1"),"UTF-8"));
			}else{//上传文件
				if(!"".equals(item.getName()) && null!=item.getInputStream() ){
					uploadItem = item;	
					//判断图片文件后缀名	
					String suffix =uploadItem.getName().substring(uploadItem.getName().lastIndexOf(".")+1);//得到文件后缀名
					//判断文件类型是否是规定的文件类型 
					if(!typeSet.contains(suffix.toLowerCase())){
						throw new BusinessDealException("上传文件格式不对,支持"+imageType+"格式");
					}
					//判断不得超过规定的大小
					if(uploadItem.getSize()>Double.parseDouble(imageMaxSize)*1024*1024){
						throw new BusinessDealException("上传文件太大,不得超过"+imageMaxSize+"M");
					}
					FileName +="."+suffix.toLowerCase();
					values.put("",FileName);//将文件名称储存好 
				}
			}
		}
		if(null==uploadItem){
			throw new BusinessDealException("请选择要上传的文件！");
		}
		File saveUrl = new File(uploadPath+FileName);
		uploadItem.write(saveUrl);
		return FileName;//返回保存文件的 文件名+后缀名
	}
	/**
	 * 副文本获取 所有image src
	 * 去除前面域名
	 */
	public static List<String> getSrc(String contentStr ,String showImagePath)throws Exception{
		String regexImg = "<img.+?src\\s*=\\s*['|\"]?\\s*([^'\"\\s>]+).+?/?>?";
        String ImageSrcStr="";
        Pattern p = Pattern.compile(regexImg,Pattern.CASE_INSENSITIVE);  
        Matcher m = p.matcher(contentStr);  
        List<String> list=new ArrayList<String>();
        while(m.find()){  
            ImageSrcStr = m.group(1);
            System.out.println(ImageSrcStr.replaceFirst(showImagePath, ""));
            list.add(ImageSrcStr.replaceFirst(showImagePath, ""));//将图片服务器地址替换掉
        }  
        return list;
	}
}
