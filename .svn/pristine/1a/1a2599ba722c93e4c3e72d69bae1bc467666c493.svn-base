package com.manji.elastic.common.util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
public class PhotoUtil {  

//	private static final Log logger = LogFactory.getLog(PhotoUtil.class);
	
	private String sourceDir; // 图片源路径
	private String destinationDir; //压缩后图片路径
	private String mode; // 生成缩略图的模式，可选ScaleOnly（等比例）或ClipAndScale（裁切）
	private int width; // 压缩图的宽度
	private int height; // 压缩图的高度
	public static PhotoUtil instance;

	public synchronized static PhotoUtil getInstance() {
		if (instance == null) {
			instance = new PhotoUtil();
		}
		return instance;
	}
	public void setDestinationDir(String destinationDir) {
		this.destinationDir = destinationDir;
	}
	public void setHeight(String height) {
		this.height = Integer.parseInt(height);
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}
	public void setWidth(String width) {
		this.width = Integer.parseInt(width);
	}
	

	/**
	 *@throws Exception 
	 * 返回值  boolean  
	 * 创建时间： Feb 3, 2009 4:12:51 PM
	 * 功能：等比例压缩图片和固定尺寸裁切图片方法
	 */
	public boolean compressImage() {

		String srcFileName = sourceDir;
		String distFileName = destinationDir;

		// 创建文件，并判断原文件是否存在
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			return false;
		}
		
		// 根据扩展名判断原文件的格式
		String extension = srcFileName.substring(srcFileName.lastIndexOf(".") + 1);
		if (!extension.equalsIgnoreCase("jpg")&& !extension.equalsIgnoreCase("bmp")&& !extension.equalsIgnoreCase("gif")&& !extension.equalsIgnoreCase("png")&& !extension.equalsIgnoreCase("jpeg")) {
			return false;
		}

		// 目标尺寸 高度和宽度
		int distWidth = width;
		int distHeight = height;

		// 判断缩放模式是否正确，如果配置错误，则抛出异常 ScaleOnly：等比例 ClipAndScale：裁切
		if (!mode.equalsIgnoreCase("ScaleOnly")&& !mode.equalsIgnoreCase("ClipAndScale")){
			return false;
		}

		BufferedImage image = null;
		try {
			image = ImageIO.read(srcFile);
		} catch (IOException e) {
//			logger.error(e);
			e.printStackTrace();
			return false;
		}  
		
		if(image==null){
			return false;
		}
		
		//原图像 高度和宽度
		int srcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		
		// 生成缩略图
		if (mode.equalsIgnoreCase("ScaleOnly")){
			
			//如果图片高度和宽度同时小于目标数据，不进行缩放
			if(srcWidth < distWidth && srcHeight < distHeight){
				distWidth = srcWidth;
				distHeight = srcHeight;
			}
			
			BufferedImage destinationImage;
			if ((float) srcWidth / distWidth > (float) srcHeight / distHeight){
				Image tempImage = image.getScaledInstance(distWidth,(int) (distWidth * ((float) srcHeight / srcWidth)),Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,(int) (distWidth * ((float) srcHeight / srcWidth)),BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(tempImage, 0, 0, null);

			} else {
				Image tempImage = image.getScaledInstance((int) (distHeight * ((float) srcWidth / srcHeight)),distHeight, Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage((int) (distHeight * ((float) srcWidth / srcHeight)),distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(tempImage, 0, 0, null);
			}
			File destinationFile = new File(distFileName);
			try {
				ImageIO.write(destinationImage, extension, destinationFile);
			} catch (IOException e) {
//				logger.error(e);
				e.printStackTrace();
				return false;
			}

		} else {

			BufferedImage destinationImage;
			if ((float) srcWidth / distWidth > (float) srcHeight/ distHeight) {
				
				// 先裁减
				int x = srcWidth - (int) (srcHeight * ((float) distWidth / distHeight));
				Image clipedImage = image.getSubimage((int) (0.5 * x),0,(int) (srcHeight * ((float) distWidth / distHeight)),srcHeight);
				
				// 后缩放
				Image scaledImage = clipedImage.getScaledInstance(distWidth, distHeight,Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(scaledImage, 0, 0, null);  

			} else {
				
				// 先裁减    
				int y = srcHeight - (int) (srcWidth * ((float) distHeight / distWidth));
				Image clipedImage = image.getSubimage(0,(int) (0.5 * y),srcWidth,(int) (srcWidth * ((float) distHeight / distWidth)));
				
				// 后缩放
				Image scaledImage = clipedImage.getScaledInstance(distWidth, distHeight,Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(scaledImage, 0, 0, null);
			}

			File destinationFile = new File(distFileName);
			try {
				ImageIO.write(destinationImage, extension, destinationFile);
			} catch (IOException e) {
//				logger.error(e);
				e.printStackTrace();
				return false;
			}

		}
		
		return true;
	}
	
	
	
	
	/**
	 *@param fileName 图片名称
	 *@throws Exception 返回值  void 
	 * 创建时间： Dec 2, 2008 2:29:32 PM
	 * 功能：等比例压缩图片&固定尺寸裁切图片
	 */
	public boolean compressImage(String fileName){

		String srcFileName = sourceDir + fileName;//相片源路径
		String distFileName = destinationDir;

		// 创建文件，并判断原文件是否存在
		File srcFile = new File(srcFileName);
		if (!srcFile.exists()) {
			return false;
		}
		
		// 根据扩展名判断原文件的格式
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (!extension.equalsIgnoreCase("jpg")&& !extension.equalsIgnoreCase("bmp")&& !extension.equalsIgnoreCase("gif")&& !extension.equalsIgnoreCase("png")&& !extension.equalsIgnoreCase("jpeg")) {
			return false;
		}

		// 目标尺寸 高度和宽度
		int distWidth = width;
		int distHeight = height;

		// 判断缩放模式是否正确，如果配置错误，则抛出异常 ScaleOnly：等比例 ClipAndScale：裁切
		if (!mode.equalsIgnoreCase("ScaleOnly")&& !mode.equalsIgnoreCase("ClipAndScale")){
			return false;
		}

		BufferedImage image = null;
		try {
			image = ImageIO.read(srcFile);
		} catch (IOException e) {
//			logger.error(e);
			e.printStackTrace();
		}  
		
		if(image==null){
			return false;
		}
		
		//原图像 高度和宽度
		int srcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		
		// 生成缩略图
		if (mode.equalsIgnoreCase("ScaleOnly")){
			
			//如果图片高度和宽度同时小于目标数据，不进行缩放
			if(srcWidth < distWidth && srcHeight < distHeight){
				distWidth = srcWidth;
				distHeight = srcHeight;
			}
			
			BufferedImage destinationImage;
			if ((float) srcWidth / distWidth > (float) srcHeight / distHeight){
				Image tempImage = image.getScaledInstance(distWidth,(int) (distWidth * ((float) srcHeight / srcWidth)),Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,(int) (distWidth * ((float) srcHeight / srcWidth)),BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(tempImage, 0, 0, null);

			} else {
				Image tempImage = image.getScaledInstance((int) (distHeight * ((float) srcWidth / srcHeight)),distHeight, Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage((int) (distHeight * ((float) srcWidth / srcHeight)),distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(tempImage, 0, 0, null);
			}
			File destinationFile = new File(distFileName);
			try {
				ImageIO.write(destinationImage, extension, destinationFile);
			} catch (IOException e) {
//				logger.error(e);
				e.printStackTrace();
			}

		} else {

			BufferedImage destinationImage;
			if ((float) srcWidth / distWidth > (float) srcHeight/ distHeight) {
				
				// 先裁减
				int x = srcWidth - (int) (srcHeight * ((float) distWidth / distHeight));
				Image clipedImage = image.getSubimage((int) (0.5 * x),0,(int) (srcHeight * ((float) distWidth / distHeight)),srcHeight);
				
				// 后缩放
				Image scaledImage = clipedImage.getScaledInstance(distWidth, distHeight,Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(scaledImage, 0, 0, null);  

			} else {
				
				// 先裁减    
				int y = srcHeight - (int) (srcWidth * ((float) distHeight / distWidth));
				Image clipedImage = image.getSubimage(0,(int) (0.5 * y),srcWidth,(int) (srcWidth * ((float) distHeight / distWidth)));
				
				// 后缩放
				Image scaledImage = clipedImage.getScaledInstance(distWidth, distHeight,Image.SCALE_SMOOTH);
				destinationImage = new BufferedImage(distWidth,distHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = destinationImage.createGraphics();
				graphics.drawImage(scaledImage, 0, 0, null);
			}
			File destinationFile = new File(distFileName);
			try {
				ImageIO.write(destinationImage, extension, destinationFile);
			} catch (IOException e) {
//				logger.error(e);
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	
	/**
	 *@param type
	 *@return 返回值  boolean 
	 * 创建时间： Aug 28, 2009 2:10:28 PM
	 * 功能：可以上传的图片类型
	 */
	public static boolean isAllowUpload(String type){  
		
		if(type==null||type.equals("")){
			return false;
		}

		if(type.equals("image/jpeg")){   
			return true;
		}
		if(type.equals("image/pjpeg")){
			return true;
		}
		if(type.equals("image/gif")){
			return true;
		}
		if(type.equals("image/bmp")){
			return true;
		}
		if(type.equals("image/x-png")){
			return true;
		}
		if(type.equals("image/png")){
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		
		PhotoUtil pu = new PhotoUtil();
		pu.setSourceDir("D:/upload/aaa.jpg");
		pu.setDestinationDir("D:/upload/1.jpg");
		pu.setMode("ClipAndScale");
		pu.setWidth("30");
		pu.setHeight("30");
		pu.compressImage();	//压缩裁剪图片
		
		pu.setSourceDir("D:/upload/aaa.jpg");
		pu.setDestinationDir("D:/upload/2.jpg");
		pu.setMode("ClipAndScale");
		pu.setWidth("80");
		pu.setHeight("80");
		pu.compressImage();	//压缩裁剪图片
	}

}
