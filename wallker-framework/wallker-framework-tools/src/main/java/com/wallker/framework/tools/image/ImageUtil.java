package com.wallker.framework.tools.image;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wallker.framework.tools.file.FileUtil;
import com.wallker.framework.tools.file.PathUtil;
import com.wallker.framework.tools.setting.AppSettings;



public class ImageUtil {
	
	// 水印透明度
	public static float ALPHA = 0.5f;
	// 水印横向位置
	public static int POSITIONWIDTH = 30;// 100;positionWidth
	// 水印纵向位置
	public static int POSITIONHEIGHT = 100;
	// 水印文字字体
	public static Font FONT = new Font("宋体", Font.BOLD, 20);
	// 水印文字颜色
	public static Color COLOR = Color.white;// Color.blue;
	// 水印的文字
	public static String logoText = "上海宝玉石交易中心 www.csgje.com";
	public static String  LOGOICON= AppSettings.getInstance().get("fangzz.resource.resourcePath")+ "/watermarkingIcon.png";
	public static String ebillPath = PathUtil.getAppPath(ImageUtil.class);
	public static String WHITE = AppSettings.getInstance().get("fangzz.resource.resourcePath")+"/White659_606.jpg";
	protected final static  Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 图像切割（改）对图片进行裁剪并保存
	 * @param srcImageFile
	 * @param dirImageFile
	 * @param x1
	 * @param y1
	 * @param destWidth1
	 * @param destHeight1
	 * 作者：曹敏杰
	 */
	 public static void getJpgCrop(String srcImageFile,String dirImageFile,String x1,
			 String y1,String destWidth1,String destHeight1){
		 
			//去除小数点后面的数字
			x1 = ImageUtil.removeChar(x1);
			y1 = ImageUtil.removeChar(y1);
			destWidth1 = ImageUtil.removeChar(destWidth1);
			destHeight1 = ImageUtil.removeChar(destHeight1);

	        int destHeight = Integer.parseInt(destHeight1);
	        int destWidth =Integer.parseInt(destWidth1);
	        int x =Integer.parseInt(x1);
			int y =Integer.parseInt(y1);
			
			try {  
	            Image img;  
	            ImageFilter cropFilter;  
	            // 读取源图像  
	            BufferedImage bi = ImageIO.read(new File(srcImageFile));  
	            int srcWidth = bi.getWidth(); // 源图宽度  
	            int srcHeight = bi.getHeight(); // 源图高度      
	            //没有选择区域就认为是全选
	            if(destHeight == 0 && destWidth == 0 && x == 0 && y == 0){
		        	  destHeight = srcHeight;
		        	  destWidth = srcWidth;
		         }
	            if (srcWidth >= destWidth && srcHeight >= destHeight) {  
	                Image image = bi.getScaledInstance(srcWidth, srcHeight,  
	                        Image.SCALE_DEFAULT);  
	               
	                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);  
	                img = Toolkit.getDefaultToolkit().createImage(  
	                        new FilteredImageSource(image.getSource(), cropFilter));  
	                BufferedImage tag = new BufferedImage(destWidth, destHeight,  
	                        BufferedImage.TYPE_INT_RGB);  
	                Graphics g = tag.getGraphics();  
	                g.drawImage(img, 0, 0, null); // 绘制缩小后的图  
	                g.dispose();  
	                // 输出为文件  
	                ImageIO.write(tag, "JPEG", new File(dirImageFile));  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            logger.error("logout error: [{}]",e.toString());
	        }  

	 }
	 /**
		 * 图像切割（改）MD5版 		对图片进行裁剪并保存
		 * @param srcImageFile	源图像地址
		 * @param dirImageFile	新图像地址
		 * @param x1			目标切片起点x坐标
		 * @param y1			目标切片起点y坐标 
		 * @param destWidth1	目标切片宽度
		 * @param destHeight1	目标切片高度
		 * @param force			指定位置Id
		 * 作者：曹敏杰
		 */
	 public static void getJpgCropMD5(String srcImageFile,String dirImageFile,String x1,
			 String y1,String destWidth1,String destHeight1,String force){
		
			//去除小数点后面的数字
			x1 = ImageUtil.removeChar(x1);
			y1 = ImageUtil.removeChar(y1);
			destWidth1 = ImageUtil.removeChar(destWidth1);
			destHeight1 = ImageUtil.removeChar(destHeight1);

	        int destHeight = Integer.parseInt(destHeight1);
	        int destWidth =Integer.parseInt(destWidth1);
	        int x =Integer.parseInt(x1);
			int y =Integer.parseInt(y1);
			
			//没有选择区域就认为是全选
			try {
				BufferedImage bii = ImageIO.read(new File(srcImageFile));
				  int wi = bii.getWidth(); // 源图宽度  
		          int hi = bii.getHeight(); // 源图高度  
		          if(destHeight == 0 && destWidth == 0 && x == 0 && y == 0){
		        	  destHeight = hi;
		        	  destWidth = wi;
		          }
		          
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error("logout error: [{}]",e1.toString());
			}  
			try {  
	            Image img;  
	            ImageFilter cropFilter;  
	            // 读取源图像  
	            BufferedImage bi = ImageIO.read(new File(srcImageFile));  
	            int srcWidth = bi.getWidth(); // 源图宽度  
	            int srcHeight = bi.getHeight(); // 源图高度            
	            if (srcWidth >= destWidth && srcHeight >= destHeight) {  
	                Image image = bi.getScaledInstance(srcWidth, srcHeight,  
	                        Image.SCALE_DEFAULT);  
	               
	                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);  
	                img = Toolkit.getDefaultToolkit().createImage(  
	                        new FilteredImageSource(image.getSource(), cropFilter));  
	                BufferedImage tag = new BufferedImage(destWidth, destHeight,  
	                        BufferedImage.TYPE_INT_RGB);  
	                Graphics g = tag.getGraphics();  
	                g.drawImage(img, 0, 0, null); // 绘制缩小后的图  
	                g.dispose();  
	                // 输出为文件  
	                ImageIO.write(tag, "JPEG", new File(dirImageFile));  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            logger.error("logout error: [{}]",e.toString());
	        }  
			try {
				
				 	//大尺寸头像上传处理
				 String newLocalFileName = dirImageFile.substring(0,dirImageFile.lastIndexOf("."))+"_200.jpg";//新保存地址
	  			 
		            if(force.equals("headImage")){
		            	BufferedImage bi = ImageIO.read(new File(dirImageFile));  
			            int w = bi.getWidth(); // 源图宽度  
			            int h = bi.getHeight(); // 源图高度  
			          if(h<286&&w<220){
			            	//补白
			        	  ImageUtil.NewPic(220, 286, dirImageFile, newLocalFileName);
			            	
			            }else{
			            	//不需要补白的
			            	FileUtil.fileChannelCopy(new File(dirImageFile), new File(newLocalFileName));
			            }
			          
		            }

				
				  		
			} catch (IOException e) {
				logger.error("logout error: [{}]",e.toString());
			}
			
	 }
	
	/**
	 * 将带小数的数字变为整数（如1.3 变为2，补充1）
	 * @param str	要处理的数字
	 * @return
	 */
	public static String removeChar(String str){
    	if(str.indexOf(".")>-1){
    		str=str.substring(0,str.indexOf("."));
    		str = ""+ (Integer.parseInt(str)+1); 
    	}
    	
    	return str;
    }
	
	/**
     * 图片补白功能，不达到指定尺寸，自动补白
     * @param setWidth		最终宽度
     * @param setHeight		最终高度
     * @param srcImgPath	图片路径
     * @param targerPath	保存路径
     * * 作者：曹敏杰
     */
    public static void NewPic(int setWidth,int setHeight,String srcImgPath,String targerPath){
    	String iconPath = WHITE;//白色填充色
    	try {
    	/* 1 读取需要处理的图片*/   
        File fileOne = new File(srcImgPath);  
        BufferedImage imageFirst = ImageIO.read(fileOne);  
        int width = imageFirst.getWidth();// 图片宽度  
        int height = imageFirst.getHeight();// 图片高度  
        int[] imageArrayFirst = new int[width * height];// 从图片中读取RGB  
        imageArrayFirst = imageFirst.getRGB(0, 0, width, height, imageArrayFirst, 0, width);  

        /* 1 对白色图片的处理 */  //做背景
        File fileTwo = new File(iconPath);  
        BufferedImage imageSecond = ImageIO.read(fileTwo); 
        int w1 = imageSecond.getWidth();
        int h1 = imageSecond.getHeight();
        int[] imageArraySecond = new int[w1 * h1];  
        imageArraySecond = imageSecond.getRGB(0, 0, w1, h1, imageArraySecond, 0, w1);  
       
          
        //补充计算偏差值
        int DW = 0 ;
        int DH = 0 ;
       
        DW = ImageUtil.getAfterDot(setWidth, width);
        DH = ImageUtil.getAfterDot(setHeight, height);
        // 生成新图片   
        BufferedImage imageResult = new BufferedImage(setWidth , setHeight,BufferedImage.TYPE_INT_RGB);  
       /**
        * @param startX      the starting X coordinate
        * @param startY      the starting Y coordinate
        * @param w           width of the region
        * @param h           height of the region
        * @param rgbArray    the rgb pixels
        * @param offset      offset into the <code>rgbArray</code>//偏移量
        * @param scansize    scanline stride for the <code>rgbArray</code> 
        * @see #getRGB(int, int)
        * @see #getRGB(int, int, int, int, int[], int, int)

        */
        //设置左部分的RGB 
        imageResult.setRGB(0, 0, (setWidth-width)/2, setHeight, imageArraySecond, 0, w1); 
        //设置中间部分的RGB
        imageResult.setRGB((setWidth-width)/2, (setHeight-height)/2,width, height, imageArrayFirst, 0,  width);
        //设置右边部分的RGB
        imageResult.setRGB(width+(setWidth-width)/2, 0,(setWidth-width)/2 +DW, setHeight, imageArraySecond, 0,  w1);
        //设置上边部分的RGB
        imageResult.setRGB((setWidth-width)/2, 0,width, (setHeight-height)/2, imageArraySecond, 0,  w1);
        //设置下边部分的RGB
        imageResult.setRGB((setWidth-width)/2, (setHeight-height)/2+height ,width, (setHeight-height)/2+DH, imageArraySecond, 0,  w1);
        
        
        File outFile = new File(targerPath);  
     // 写图片 
			ImageIO.write(imageResult, "jpg", outFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    
    	
    }
    /**
     * 补白图片使用的修正值
     * @param setNum	要求生成图片的值
     * @param Num		图片本身的值
     * @return
     */
    public static int  getAfterDot(int setNum,int Num){
    	double dd = ((double)setNum - (double)Num)/2;
    	int i = (setNum - Num)/2;
    	double f = dd-i;
    	if(f>0){
    		return 1;
    	}else{
    		return 0;
    	}
		
    	
    }

    /**
     * //生成透明图片文字
     * @param str		内容
     * @param imgurl	保存位置
     */
    public static  BufferedImage image;
    public static void graphicsGeneration(String str,String imgurl){
		 
		int unicodeCount = 0;  
        int szCount = 0;  
        int zmCount = 0; 
        int zw = 0;
		 for (int i = 0; i < str.length(); i++) {  
			  
            char c = str.charAt(i);  
            if (c >= '0' && c <= '9') {  
                szCount++;  //数字
            }else if((c >= 'a' && c<='z') || (c >= 'A' && c<='Z')){  
                zmCount++;  //字母
            }else if(c>128){//表示是中文
                zw++;//中文
            }else{  
                unicodeCount++;  //其他
            }  
        }  
		
        
		 int sum=(szCount*7)+(zmCount*7)+(zw*14)+(unicodeCount*7); 
		 
		  int imageWidth = sum;// 图片的宽度
		  int imageHeight = 15;// 图片的高度

		  

		  image = new BufferedImage(imageWidth, imageHeight,
		    BufferedImage.TYPE_INT_RGB);
		  Graphics2D g2d = image.createGraphics();
			image = g2d.getDeviceConfiguration().createCompatibleImage(imageWidth,imageHeight, Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = image.createGraphics();
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(new Color(58,58,58));
			 
			g2d.setFont(new Font("新宋体",Font.PLAIN,14));
			g2d.drawString(str, 0, 13);
			// 释放对象
			g2d.dispose();
		 
		  try {
			ImageIO.write(image, "PNG", new File(imgurl));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
    /**
	 * 
	 * @param alpha
	 *            水印透明度
	 * @param positionWidth
	 *            水印横向位置
	 * @param positionHeight
	 *            水印纵向位置
	 * @param font
	 *            水印文字字体
	 * @param color
	 *            水印文字颜色
	 */
	public static void setImageMarkOptions(float alpha, int positionWidth,
			int positionHeight, Font font, Color color) {
		if (alpha != 0.0f)
			ImageUtil.ALPHA = alpha;
		if (positionWidth != 0)
			ImageUtil.POSITIONWIDTH = positionWidth;
		if (positionHeight != 0)
			ImageUtil.POSITIONHEIGHT = positionHeight;
		if (font != null)
			ImageUtil.FONT = font;
		if (color != null)
			ImageUtil.COLOR = color;
	}

	/**
	 * 图片添加公司水印图片
	 * @param srcImgPath
	 */
	public static boolean markImageByIcon(String srcImgPath){
		return markImageByIcon(LOGOICON, srcImgPath, srcImgPath, null);
	}
	/**
	 * 给图片添加水印图片
	 * 
	 * @param iconPath
	 *            水印图片路径
	 * @param srcImgPath
	 *            源图片路径
	 * @param targerPath
	 *            目标图片路径
	 */
	public static boolean markImageByIcon(String iconPath, String srcImgPath, String targerPath) {
		return markImageByIcon(iconPath, srcImgPath, targerPath, null);
	}

	/**
	 * 给图片添加水印图片、可设置水印图片旋转角度
	 * 
	 * @param iconPath 水印图片路径
	 * @param srcImgPath 源图片路径
	 * @param targerPath 目标图片路径
	 * @param degree 水印图片旋转角度
	 */
	public static boolean markImageByIcon(String iconPath, String srcImgPath, String targerPath, Integer degree) {
		OutputStream os = null;
		try {
			Image srcImg = ImageIO.read(new File(srcImgPath));
			int height = srcImg.getHeight(null) - 80;// 图片的高度
			if (height < 0){
				height = 0;
			}

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// 1、得到画笔对象
			Graphics2D g = buffImg.createGraphics();

			// 2、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
			// 3、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}

			// 4、水印图片的路径 水印图片一般为gif或者png的，这样可设置透明度
			ImageIcon imgIcon = new ImageIcon(iconPath);

			// 5、得到Image对象。
			Image img = imgIcon.getImage();

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

			// 6、水印图片的位置
			g.drawImage(img, POSITIONWIDTH, height, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			// 7、释放资源
			g.dispose();

			// 8、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 缩放图像
	 * @param srcImageFile
	 * @param result
	 * @param height
	 * @param width
	 * @param fill 是否补白（四周补白）
	 * @return
	 */
	public final static boolean scale2(String srcImageFile, String result, int height, int width, boolean fill){
		
		return scale2(srcImageFile, result, height, width, fill,false);
	}
	/**
     * 缩放图像（按高度和宽度缩放）
     * @param srcImageFile 源图像文件地址
     * @param result 缩放后的图像地址
     * @param height 缩放后的高度
     * @param width 缩放后的宽度
     * @param fill 是否补白
     * @param top 补白时，原图是否上下居顶。false-图像居中。（左右始终居中）
     */
	public final static boolean scale2(String srcImageFile, String result, int height, int width, boolean fill,boolean top) {
		try {
			File f = new File(srcImageFile);
			BufferedImage srcImg = ImageIO.read(f);
			// 计算比例
			int newWidth=srcImg.getWidth(null);
			int newHeight=srcImg.getHeight(null);
			
			if (newWidth > width || newHeight > height) {
                double rate1 = ((double)newWidth) / (double) width;   
                double rate2 = ((double)newHeight) / (double) height;
                if(rate1>=rate2){
                	newWidth = width;
                	newHeight = (int) (((double)newHeight) / rate1); 
                }else{
                	newHeight = height;
                	newWidth = (int) (((double)newWidth) / rate2);  
                }
			}
			
			BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(srcImg.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
			
			if (fill) {// 补白
				BufferedImage itemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = itemp.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				int x = (width-newWidth)/2;
				int y = (height-newHeight)/2;
				if(top){
					y = 0;
				}
				g.drawImage(image, x, y, newWidth, newHeight, Color.white, null);
				g.dispose();
				image = itemp;
			}
			
			ImageIO.write(image, "JPEG", new File(result));
			return true;
		} catch (IOException e) {
			logger.error("大小转换出错[{}]", e);
			e.printStackTrace();
			return false;
		}
	}
    
	/**
	 * 给图片添加水印文字
	 * @param imgPath 源图片路径
	 */
	public static void markImageByText(String imgPath) {
		markImageByText(logoText, imgPath, imgPath, 0);
	}

	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * @param logoText
	 * @param srcImgPath
	 * @param targerPath
	 * @param degree
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree) {
		markImageByText(logoText, srcImgPath, targerPath, degree, COLOR, -1, -1);
	}
	
	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * @param logoText
	 * @param srcImgPath
	 * @param targerPath
	 * @param degree
	 */
	public static void markImageByText(String logoText, String srcImgPath, String targerPath, Integer degree, Color color, int x, int y) {

		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			// int width = srcImg.getWidth(null); // 得到源图宽
			int height = srcImg.getHeight(null) - 85;// 图片的高度
			if (height < 0)
				height = 0;

			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
			// 4、设置水印旋转
			if (null != degree) {
				g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
			}
			// 5、设置水印文字颜色
			g.setColor(color);
			// 6、设置水印文字Font
			g.setFont(FONT);
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			if (-1 == x) {
				x = POSITIONWIDTH;
			}
			if (-1 == y) {
				y = height;
			}
			g.drawString(logoText, x, y);
			// 9、释放资源
			g.dispose();
			// 10、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

			// logger.debug("图片完成添加水印文字");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给图片添加水印文字、可设置水印文字的旋转角度
	 * @param textMap
	 * @param targerPath
	 * @param orderTypeId
	 */
	public static String markImageByText(Map<String,String> textMap,String targerPath,Integer orderTypeId) {
		logger.debug("textMap==[{}]",textMap);
		String ebill = String.format("%s/ebill%s.png", ebillPath,orderTypeId);
		InputStream is = null;
		OutputStream os = null;
		try {
			// 1、源图片
			Image srcImg = ImageIO.read(new File(ebill));
			//int width = srcImg.getWidth(null); // 得到源图宽
			int height = srcImg.getHeight(null)-85;//图片的高度
			if (height<0) height=0;
			
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
					srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// 2、得到画笔对象
			Graphics2D g = buffImg.createGraphics();
			// 3、设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(
					srcImg.getScaledInstance(srcImg.getWidth(null),
							srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
					null);
			// 4、设置水印旋转
//			if (null != degree) {
//				g.rotate(Math.toRadians(degree),
//						(double) buffImg.getWidth() / 2,
//						(double) buffImg.getHeight() / 2);
//			}
			// 5、设置水印文字颜色
			g.setColor(Color.BLACK);
			// 6、设置水印文字Font
			g.setFont(FONT);
			// 7、设置水印文字透明度
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));
			// 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
			g.drawString(textMap.get("agentNo"), 183,230);//会员号
			g.drawString(textMap.get("orderDt"), 183,263);//订单日期
//			g.drawString(textMap.get("fullName"), 183,263);
//			g.drawString(textMap.get("userName"), 495,230);
			g.drawString(textMap.get("orderId"), 495,266);
			
			g.drawString(textMap.get("paymentId"), 90,353);
//			g.drawString(textMap.get("context"), 318,350);
			g.drawString(textMap.get("amount"), 570,353);
//			g.drawString(textMap.get("bigAmt"), 468,395);
			// 9、释放资源
			g.dispose();
			// 10、生成图片
			os = new FileOutputStream(targerPath);
			ImageIO.write(buffImg, "JPG", os);

//			logger.debug("图片完成添加水印文字");

		} catch (Exception e) {
			logger.error("生成电子凭证[{}]出错：\n[{}]",targerPath,e);
			return "";
//			e.printStackTrace();
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return targerPath;
	}
	
	@SuppressWarnings("unused")
	private static AttributedCharacterIterator cnFont(String markContent){
		AttributedString ats = new AttributedString(markContent);
        Font f = new Font("ukai",Font.BOLD, 14);

        ats.addAttribute(TextAttribute.FONT, f, 0,markContent.length() );
        AttributedCharacterIterator iter = ats.getIterator();
        return iter;
	}
	/**
	 * 上传多个房源图片保存处理
	 * @param realName	临时存放图片地址
	 * @param savePath	存放图片地址
	 * @return
	 */
	public static boolean houseImage(String realName, String savePath) {
		try {
			String savePath_0 = savePath.substring(0, savePath.lastIndexOf(".")) + "_0.jpg";// 原图
			String savePath_800 = savePath.substring(0, savePath.lastIndexOf(".")) + "_800.jpg";// 800x600
			String savePath_200 = savePath.substring(0, savePath.lastIndexOf(".")) + "_200.jpg";// 200x150
			//复制原图
			boolean flag = FileUtil.fileChannelCopy(new File(realName), new File(savePath_0));// 保存原图
			if(flag==false){
				return false;
			}
			//400*300
			flag = ImageUtil.scale2(realName, savePath, 300, 400, false);
			if(flag==false){
				return false;
			}
			ImageUtil.markImageByIcon(savePath);
			ImageUtil.scale2(savePath, savePath, 300, 400, true);
			
			//200x150
			flag = ImageUtil.scale2(realName, savePath_200, 150, 200, true);
			if(flag==false){
				return false;
			}
			
			//800*600
			flag = ImageUtil.scale2(realName, savePath_800, 600, 800, false);
			if(flag==false){
				return false;
			}
			ImageUtil.markImageByIcon(savePath_800);
			ImageUtil.scale2(savePath_800, savePath_800, 600, 800, true);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成透明背景内容
     * @param str		生成内容
     * @param imgurl 	存放位置
     * @param Type		字体类型如新宋体
     * @param style		0 平滑   1 粗体	2 斜体
     * @param Size		字体大小
     * @param Height 	图片高度
     * @param strColor 	字体颜色
     * @param line 		是否要干扰线
	 * @param zmNum		字母宽度
	 * @param szNum		数字宽度
	 * @param zwNum		中文宽度
	 * @param lineNum	干扰线数量
	 * @param x			内容x轴起点
	 * @param y			内容y轴起点
	 * 建议类型Type：新宋体，style：0，Size：12，Height：15，strColor：black，zmNum：6，szNum：6，zwNum：12，x：0或1，y：13
	 */
    public static void graphicsGeneration(String str,String imgurl,String Type,int style,int Size,
    		int Height,Color strColor,boolean line,int zmNum,int szNum,int zwNum,int lineNum,int x,int y){
    	
		int unicodeCount = 0;  
        int szCount = 0;  
        int zmCount = 0; 
        int zw = 0;
		 for (int i = 0; i < str.length(); i++) {  
			  
            char c = str.charAt(i);  
            if (c >= '0' && c <= '9') {  
                szCount++;  //数字
            }else if((c >= 'a' && c<='z') || (c >= 'A' && c<='Z')){  
                zmCount++;  //字母
            }else if(c>128){//表示是中文
                zw++;//中文
            }else{  
                unicodeCount++;  //其他
            }  
        }  
		
        
		 int sum=(szCount*szNum)+(zmCount*zmNum)+(zw*zwNum)+(unicodeCount*szNum); 
		 
		  int imageWidth = sum;// 图片的宽度
		  int imageHeight = Height;// 图片的高度
		  int lineSize = lineNum;//干扰线数量
		  //内容少的时候产生多少干扰线
		  if(str.length() <5){
			  lineSize = 4;
		  }
		  
		  Random random = new Random();
		  image = new BufferedImage(imageWidth, imageHeight,
		    BufferedImage.TYPE_INT_RGB);
		  Graphics2D g2d = image.createGraphics();
			image = g2d.getDeviceConfiguration().createCompatibleImage(imageWidth,imageHeight, Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = image.createGraphics();
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(strColor);
			 
			g2d.setFont(new Font(Type,style,Size));
			g2d.drawString(str, 0, 13);//内容，x起点，y起点
			// 释放对象
			g2d.dispose();
		  //drowLine2(graphics,imageWidth,imageHeight);//下划线
			if(line == true){
				 for(int i=0;i<=lineSize;i++){
			        drowLine(random,g2d,imageWidth,imageHeight);
			     }
			}
		 
		  try {
			ImageIO.write(image, "PNG", new File(imgurl));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
   
    /**
     *  绘制干扰线
     * @param random	随机数
     * @param g			图形类
     * @param width		干扰线宽度
     * @param height	干扰线高度度
     */
    private static void drowLine(Random random ,Graphics g,int width,int height){
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x+xl, y+yl);
    }
   public static void main(String[] args) {
	   markImageByText("D:/workspaces/fangzz-www/src/main/resources/ebill.jpg");
   }
}
