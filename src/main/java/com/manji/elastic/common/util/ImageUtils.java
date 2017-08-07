package com.manji.elastic.common.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


/**
 * 图片处理工具类：<br>
 * 功能：缩放图像、切割图像、图像类型转换、彩色转黑白、文字水印、图片水印等
 * 
 * @author Administrator
 */
public class ImageUtils {
	/**
	 * 几种常见的图片格式
	 */
	public final static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	public final static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	public final static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	public final static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	public final static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	public final static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop

	/**
	 * 程序入口：用于测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 1-缩放图像：
		// 方法一：按比例缩放
		// ImageUtils.scale("c:/abc.jpg", "c:/abc_scale.jpg", 2, false);// 测试OK
		// 方法二：按高度和宽度缩放
		//ImageUtils.scale("c:/abc.jpg", "c:/abc_scale2.jpg", 503, 752, true);// 测试OK
		// 2-切割图像： ImageUtils.cut("c:/abc.jpg", "c:/abc_cut.jpg", 0, 0, 400,400);// 测试OK
		// 3-图像类型转换： ImageUtils.convert("c:/abc.jpg", "GIF","c:/abc_convert.gif");// 测试OK
		// 4-彩色转黑白： ImageUtils.gray("c:/abc.jpg", "c:/abc_gray.jpg");// 测试OK
		// 5-给图片添加文字水印： ImageUtils.pressText("我是水印文字", "c:/abc.jpg","c:/abc_pressText.jpg", "宋体", Font.BOLD, Color.white, 80, 0, 0,0.5f);// 测试OK
		// 6-给图片添加图片水印： ImageUtils.pressImage("c:/press.jpg", "c:/abc.jpg","c:/abc_pressImage.jpg", 0, 0, 0.5f);// 测试OK
		// 7-旋转图片：ImageUtils.rotateImage("c:/abc.jpg", "c:/abc_rotateImage.jpg", 60 , 250 ,350);// 测试OK
	}

	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public final static void scale(String srcImageFile, String result,
			int scale, boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param flag
	 *            是否保持宽高比,true保持宽高比，false不保持;
	 */
	public final static void scale(String srcImageFile, String result,
			int width, int height, boolean flag) {
		try {
			double ratio = 0.0; // 原图缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			int w = bi.getWidth(); // 得到源图宽
			int h = bi.getHeight(); // 得到源图长
			ratio = (new Integer(w)).doubleValue()
					/ (new Integer(h)).doubleValue();
			int wDest = 0;
			int hDest = 0;
			if (flag) {
				if (ratio * height <= width) {
					// 以高度为基准，等比例放缩图片
					hDest = height;
					wDest = (int) (ratio * height);

					Image itemp = bi.getScaledInstance(wDest, hDest,
							Image.SCALE_SMOOTH);
					BufferedImage image = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
					g.fillRect(0, 0, width, height);
					g.drawImage(itemp, (width - wDest) / 2, 0, wDest, hDest,
							Color.white, null);
					g.dispose();

					ImageIO.write(image, "JPEG", new File(result));
				} else {
					// 以宽度为基准，等比例放缩图片
					wDest = width;
					hDest = (int) (wDest / ratio);

					Image itemp = bi.getScaledInstance(wDest, hDest,
							Image.SCALE_SMOOTH);
					BufferedImage image = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
					g.fillRect(0, 0, width, height);
					g.drawImage(itemp, 0, (height - hDest) / 2, wDest, hDest,
							Color.white, null);
					g.dispose();

					ImageIO.write(image, "JPEG", new File(result));
				}
			} else {
				Image itemp = bi.getScaledInstance(width, height,
						Image.SCALE_SMOOTH);
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
				g.fillRect(0, 0, width, height);
				g.drawImage(itemp, 0, 0, itemp.getWidth(null), itemp
						.getHeight(null), Color.white, null);
				itemp = image;
				ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param flag
	 *            是否保持宽高比,true保持宽高比，false不保持;
	 */
	public final static BufferedImage scale(BufferedImage bi, int width,
			int height, boolean flag) {
		try {
			double ratio = 0.0; // 原图缩放比例
			int w = bi.getWidth(); // 得到源图宽
			int h = bi.getHeight(); // 得到源图长
			ratio = (new Integer(w)).doubleValue()
					/ (new Integer(h)).doubleValue();
			int wDest = 0;
			int hDest = 0;
			if (flag) {
				if (ratio * height <= width) {
					// 以高度为基准，等比例放缩图片
					hDest = height;
					wDest = (int) (ratio * height);

					Image itemp = bi.getScaledInstance(wDest, hDest,
							Image.SCALE_SMOOTH);
					BufferedImage image = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
					g.fillRect(0, 0, width, height);
					g.drawImage(itemp, (width - wDest) / 2, 0, wDest, hDest,
							Color.white, null);
					g.dispose();

					return image;
				} else {
					// 以宽度为基准，等比例放缩图片
					wDest = width;
					hDest = (int) (wDest / ratio);

					Image itemp = bi.getScaledInstance(wDest, hDest,
							Image.SCALE_SMOOTH);
					BufferedImage image = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g = image.createGraphics();
					g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
					g.fillRect(0, 0, width, height);
					g.drawImage(itemp, 0, (height - hDest) / 2, wDest, hDest,
							Color.white, null);
					g.dispose();

					return image;
				}
			} else {
				Image itemp = bi.getScaledInstance(width, height,
						Image.SCALE_SMOOTH);
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
				g.fillRect(0, 0, width, height);
				g.drawImage(itemp, 0, 0, itemp.getWidth(null), itemp
						.getHeight(null), Color.white, null);
				itemp = image;
				return image;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static void cut(String srcImageFile, String result, int x,
			int y, int width, int height) throws Exception {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width,
						height);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(bi.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(result));
			}
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param bi
	 *            源图像
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static BufferedImage cut(BufferedImage bi, int x, int y,
			int width, int height) {
		try {
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width,
						height);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(bi.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				return tag;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param formatName
	 *            包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static void convert(String srcImageFile, String formatName,
			String destImageFile) {
		try {
			File f = new File(srcImageFile);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, formatName, new File(destImageFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static void gray(String srcImageFile, String destImageFile) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, "JPEG", new File(destImageFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加文字水印
	 * 
	 * @param pressText
	 *            水印文字
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式
	 * @param color
	 *            水印的字体颜色
	 * @param fontSize
	 *            水印的字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressText(String pressText, String srcImageFile,
			String destImageFile, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, (width - (getLength(pressText) * fontSize))
					/ 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			ImageIO.write((BufferedImage) image, "JPEG",
					new File(destImageFile));// 输出到文件流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给图片添加图片水印
	 * 
	 * @param pressImg
	 *            水印图片
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param x
	 *            修正值。 默认在中间
	 * @param y
	 *            修正值。 默认在中间
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 */
	public final static void pressImage(String pressImg, String srcImageFile,
			String destImageFile, int x, int y, float alpha) {
		try {
			File img = new File(srcImageFile);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			// 水印文件
			Image src_biao = ImageIO.read(new File(pressImg));
			int width_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));
			g.drawImage(src_biao, (width - width_biao) / 2,
					(height - height_biao) / 2, width_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
			ImageIO.write((BufferedImage) image, "JPEG",
					new File(destImageFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 旋转图片为指定角度
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param degree
	 *            旋转角度
	 * @return
	 */
	public static void rotateImage(String srcImageFile, String destImageFile,
			final int degree, final int x, final int y) {
		try {
			File imgage = new File(srcImageFile);
			Image src = ImageIO.read(imgage);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage bufferedimage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedimage.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.dispose();

			int w = bufferedimage.getWidth();
			int h = bufferedimage.getHeight();
			int type = bufferedimage.getColorModel().getTransparency();
			BufferedImage img = new BufferedImage(w, h, type);
			Graphics2D graphics2d = img.createGraphics();
			graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2d.rotate(Math.toRadians(degree), x, y);
			graphics2d.drawImage(bufferedimage, 0, 0, null);
			graphics2d.dispose();

			ImageIO.write((BufferedImage) img, "JPEG", new File(destImageFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 旋转图片为指定角度
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 * @param degree
	 *            旋转角度
	 * @return
	 */
	public static BufferedImage rotateImage(BufferedImage bi, final int degree,
			final int x, final int y) {
		try {
			int w = bi.getWidth();
			int h = bi.getHeight();
			int type = bi.getColorModel().getTransparency();
			BufferedImage img = new BufferedImage(w, h, type);
			Graphics2D graphics2d = img.createGraphics();
			graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2d.rotate(Math.toRadians(degree), x, y);
			graphics2d.drawImage(bi, 0, 0, null);
			graphics2d.dispose();
			return img;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 计算text的长度（一个中文算两个字符）
	 * 
	 * @param text
	 * @return
	 */
	public final static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	/**
	 * 转换字符为int
	 * 
	 * @param text
	 * @return
	 */
	public final static int getInt(String text) {
		return (int) Float.parseFloat(text);
	}

	/**
	 * 得到图片原大小
	 * 
	 * @param text
	 * @return
	 */
	public final static Map<String, Integer> getImageSize(String srcImageFile) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			File imgage = new File(srcImageFile);
			Image src = ImageIO.read(imgage);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			map.put("w", width);
			map.put("h", height);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("w", 0);
			map.put("h", 0);
		}
		return map;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param imageSource
	 *            源图像
	 * @param imageDest
	 *            目标图像
	 * @return
	 */
	public static void cropzoom(String imageSource, String imageDest,
			String imageRotate, String viewPortW, String viewPortH,
			String imageX, String imageY, String imageW, String imageH,
			String selectorX, String selectorY, String selectorW,
			String selectorH) {
		try {
			// 整个大窗口的对象，其窗口大小等于可视窗口大小加上2倍的图片大小
			BufferedImage totalImage = new BufferedImage(getInt(viewPortW)
					+ getInt(imageW) * 2, getInt(viewPortH) + getInt(imageH)
					* 2, BufferedImage.TYPE_INT_RGB);

			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(imageSource));

			// 先把源图像保持宽高比，平滑的放大
			BufferedImage tmp1 = ImageUtils.scale(bi, getInt(imageW),
					getInt(imageH), false);

			// 把放大后的图片放到整个大窗口上
			Graphics2D g = totalImage.createGraphics();
			g.setColor(new Color(Integer.parseInt("CCCCCC", 16)));
			g.fillRect(0, 0, getInt(viewPortW) + getInt(imageW) * 2,
					getInt(viewPortH) + getInt(imageH) * 2);
			g.drawImage(tmp1, getInt(imageW) + getInt(imageX), getInt(imageH)
					+ getInt(imageY), getInt(imageW), getInt(imageH),
					Color.white, null);
			g.dispose();

			// 以图片中心为中心点，旋转图片
			BufferedImage tmp2 = ImageUtils.rotateImage(totalImage,
					getInt(imageRotate), getInt(imageW) + getInt(imageX)
							+ getInt(imageW) / 2, getInt(imageH)
							+ getInt(imageY) + getInt(imageH) / 2);

			// 在整个大窗口的基础上，截出可视窗口
			BufferedImage tmp3 = ImageUtils.cut(tmp2, getInt(imageW),
					getInt(imageH), getInt(viewPortW), getInt(viewPortH));

			// 最后在在可视窗口上截出选择区域图片
			BufferedImage tmp4 = ImageUtils.cut(tmp3, getInt(selectorX),
					getInt(selectorY), getInt(selectorW), getInt(selectorH));
			ImageIO.write(tmp4, "PNG", new File(imageDest));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}