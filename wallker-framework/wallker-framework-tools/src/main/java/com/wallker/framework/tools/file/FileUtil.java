/**
 * 项目中使用到的文件处理方法
 * 20160108 add by 梁华山
 */
package com.wallker.framework.tools.file;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class FileUtil {

	/**
	 * 编码
	 */
	public final static String charset = "utf-8";

	/**
	 * 取目录下的某种类型的文件清单
	 *
	 * @param path
	 *            路径
	 * @param fileHeadStr
	 *            文件头
	 * @param extName
	 *            文件扩展名
	 * @param excludeFiles
	 *            排除的文件名
	 * @return
	 */
	public static List<String> getFiles(String path, String fileHeadStr, String extName, String excludeFiles) {
		File dir = new File(path);
		File[] children = dir.listFiles();
		List<String> list = new ArrayList<String>();
		if (children == null || children.length == 0) {
			return list;
		}
		for (int i = 0; i < children.length; i++) {
			File file = children[i];
			if (!file.isFile()) {
				continue;
			}
			String fileName = file.getName();
			if (fileName.equals(excludeFiles)) {
				continue;
			}
			if (!"*".equals(extName) && !fileName.toLowerCase().endsWith(extName.toLowerCase())) {
				continue;
			}
			if (!fileName.startsWith(fileHeadStr)) {
				continue;
			}
			list.add(fileName);
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 判断本地文件是否存在
	 *
	 * @param fileName
	 *            文件存
	 * @return
	 */
	public static boolean fileExists(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return false;
		}
		File fl = new File(fileName);
		return fl.exists();
	}

	/**
	 * 删除文件
	 *
	 * @param fileName
	 */
	public static boolean deleteFile(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return true;
		}
		File fl = new File(fileName);
		return fl.delete();
	}

	/**
	 * 创建本地目录，若目录已经存在，则不创建。
	 *
	 * @param path
	 *            目录名
	 * @return 目录存在或者创建成功时返回true，否则为false
	 */
	public static boolean localMkdir(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		File fl = new File(path);
		if (!fl.exists()) {
			return fl.mkdir();
		} else {
			return true;
		}
	}

	/**
	 * 写本地文件，并
	 * 
	 * @param fileName
	 *            文件名，包含完整的路径
	 * @param str
	 *            文件内容
	 * @param charset
	 *            文件的字符集
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String fileName, String str, String charset) throws IOException {
		FileOutputStream fout = new FileOutputStream(fileName);
		Writer write = new OutputStreamWriter(fout, charset);
		write.write(str);
		write.close();
		fout.close();
		return true;
	}

	/**
	 * 追加本地文件，并
	 * 
	 * @param fileName
	 *            文件名，包含完整的路径
	 * @param str
	 *            文件内容
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(String fileName, String str) throws IOException {
		return appendFile(fileName, str, charset);
	}

	/**
	 * 追加本地文件，并
	 * 
	 * @param fileName
	 *            文件名，包含完整的路径
	 * @param str
	 *            文件内容
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(String fileName, String str, String charset) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), charset));
		out.write(str);
		out.close();
		return true;
	}

	/**
	 * 读本地文件，并
	 * 
	 * @param fileName
	 *            文件名，包含完整的路径
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		FileInputStream fin = new FileInputStream(fileName);
		BufferedReader inn = new BufferedReader(new InputStreamReader(fin, charset));
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = inn.readLine()) != null) {
			sb.append(line + "\n");
		}
		inn.close();
		fin.close();
		return sb.toString();
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean createFile(String fileName, boolean overwrite) throws IOException {
		File file = new File(fileName);
		if (file.exists()) {
			if (!overwrite) {
				return false;
			}
			file.delete();
		}
		return file.createNewFile();
	}

	/**
	 * 生成一个本地临时目录
	 * 
	 * @param
	 * @return 本地临时路径
	 */
	public static String getLocalOutputPath() {
		// 取本地的临时目录，用于存放本地临时文件使用。
		// String path = AmadaParameter.localTempPath + AmadaParameter.tmpDirHead
		// + DateFormatUtils.format(System.currentTimeMillis(), "ddHHmmssSSS");
		// Random rand = new Random();
		// path = path + rand.nextInt(100) + "/";
		// File dir = new File(path);
		// if (!dir.mkdirs()) {
		// return "";
		// }
		// return path;
		return "";
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 *
	 * @param delpath
	 * @return boolean
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean deletePath(String delpath) throws Exception {
		try {
			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "/" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletePath(delpath + "/" + filelist[i]);
					}
				}
				file.delete();
			}
		} catch (FileNotFoundException e) {
			throw e;
		}
		return true;
	}

	/**
	 * 文件重命名
	 *
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 */
	public static boolean renameFile(String sourceFile, String targetFile) {
		if (StringUtils.isBlank(sourceFile) || StringUtils.isBlank(targetFile)) {
			return false;
		}
		File sf = new File(sourceFile);
		File tf = new File(targetFile);
		return sf.renameTo(tf);
	}

	/**
	 * 复制文件功能
	 * 
	 * @param s
	 *            源文件了路径
	 * @param t
	 *            目标文件路径
	 */
	public static boolean fileChannelCopy(File s, File t) {
		try {
			FileUtils.copyFile(s, t);
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	// 生成透明图片文字
	public static BufferedImage image;

	@SuppressWarnings("unused")
	public static void graphicsGeneration(String str, String imgurl) {

		int unicodeCount = 0;
		int szCount = 0;
		int zmCount = 0;
		int zw = 0;
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				szCount++; // 数字
			} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				zmCount++; // 字母
			} else if (c > 128) {// 表示是中文
				zw++;// 中文
			} else {
				unicodeCount++; // 其他
			}
		}

		int sum = (szCount * 7) + (zmCount * 7) + (zw * 14) + (unicodeCount * 7);

		int imageWidth = sum;// 图片的宽度
		int imageHeight = 15;// 图片的高度
		int lineSize = 7;// 干扰线数量

		if (str.length() < 5) {
			lineSize = 4;
		}

		Random random = new Random();
		image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		image = g2d.getDeviceConfiguration().createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
		g2d.dispose();
		g2d = image.createGraphics();
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(new Color(58, 58, 58));

		g2d.setFont(new Font("新宋体", Font.PLAIN, 14));
		g2d.drawString(str, 0, 13);
		// 释放对象
		g2d.dispose();
		// drowLine2(graphics,imageWidth,imageHeight);//下划线
		/*
		 * for(int i=0;i<=lineSize;i++){
		 * drowLine(random,graphics,imageWidth,imageHeight); }
		 */
		try {
			ImageIO.write(image, "PNG", new File(imgurl));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 生成图片方法一
	}

	/**
	 * 生成透明背景内容
	 * 
	 * @param str
	 *            生成内容
	 * @param imgurl
	 *            存放位置
	 * @param Type
	 *            字体类型如新宋体
	 * @param style
	 *            0 平滑 1 粗体 2 斜体
	 * @param Size
	 *            字体大小
	 * @param Height
	 *            图片高度
	 * @param strColor
	 *            字体颜色
	 * @param line
	 *            是否要干扰线
	 */
	public static void graphicsGeneration(String str, String imgurl, String Type, int style, int Size, int Height,
			Color strColor, boolean line) {

		graphicsGeneration(str, imgurl, Type, style, Size, Height, strColor, line, 0, 0);
	}

	/**
	 * 生成透明背景内容
	 * 
	 * @param str
	 *            生成内容
	 * @param imgurl
	 *            存放位置
	 * @param Type
	 *            字体类型如新宋体
	 * @param style
	 *            0 平滑 1 粗体 2 斜体
	 * @param Size
	 *            字体大小
	 * @param Height
	 *            图片高度
	 * @param strColor
	 *            字体颜色
	 * @param line
	 *            是否要干扰线
	 */
	public static void graphicsGeneration(String str, String imgurl, String Type, int style, int Size, int Height,
			Color strColor, boolean line, int zmNum, int zwNum) {

		// 判断文字宽度
		if (0 == zmNum) {
			zmNum = 10;
		}
		if (0 == zwNum) {
			zwNum = 17;
		}

		int unicodeCount = 0;
		int szCount = 0;
		int zmCount = 0;
		int zw = 0;
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				szCount++; // 数字
			} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				zmCount++; // 字母
			} else if (c > 128) {// 表示是中文
				zw++;// 中文
			} else {
				unicodeCount++; // 其他
			}
		}

		int sum = (szCount * zmNum) + (zmCount * zmNum) + (zw * zwNum) + (unicodeCount * zmNum);

		int imageWidth = sum;// 图片的宽度
		int imageHeight = Height;// 图片的高度
		int lineSize = 7;// 干扰线数量

		if (str.length() < 5) {
			lineSize = 4;
		}

		Random random = new Random();
		image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		image = g2d.getDeviceConfiguration().createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
		g2d.dispose();
		g2d = image.createGraphics();
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(strColor);

		g2d.setFont(new Font(Type, Font.PLAIN, Size));
		g2d.drawString(str, 0, Height - 3);
		// 释放对象
		g2d.dispose();
		// drowLine2(graphics,imageWidth,imageHeight);//下划线
		if (line == true) {
			for (int i = 0; i <= lineSize; i++) {
				drowLine(random, g2d, imageWidth, imageHeight);
			}
		}

		try {
			ImageIO.write(image, "PNG", new File(imgurl));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 绘制干扰线
	 */
	private static void drowLine(Random random, Graphics g, int width, int height) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	@SuppressWarnings("unused")
	private static void drowLine2(Graphics g, int width, int height) {
		int x = 0;
		int y = 0;
		int xl = width;
		int yl = height;
		g.drawLine(x, yl - 1, xl, yl - 1);
	}

	/**
	 * 读取url存储到本地
	 * 
	 * @param httpUrl
	 * @param fileFullPath
	 * @return
	 */
	public static boolean downloadToLocal(String httpUrl, String fileFullPath) {
		if (StringUtils.isBlank(httpUrl) || StringUtils.isBlank(fileFullPath)) {
			return false;
		}
		BufferedInputStream bis = null;
		OutputStream bos = null;
		try {
			File file = new File(fileFullPath);
			File pf = new File(file.getParent());
			if (pf.exists() && !pf.isDirectory()) {
				return false;
			}
			if (!pf.exists()) {
				if (!pf.mkdir()) {
					return false;
				}
			}
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			bis = new BufferedInputStream(conn.getInputStream());
			byte[] bytes = new byte[100];
			bos = new FileOutputStream(new File(fileFullPath));
			int len;
			while ((len = bis.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			bis.close();
			bos.flush();
			bos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (Exception e) {

			}
		}
		return false;
	}

	/**
	 * 移动文件位置
	 * 
	 * @param file1
	 *            源地址
	 * @param file2
	 *            目标地址文件夹
	 */
	public static void moveFilePath(String file1, String file2) {
		try {
			// 复制文件
			FileUtil.fileChannelCopy(new File(file1), new File(file2));
			// 删除文件
			delFile(file1);

		} catch (Exception e) {
			// TODO: handle exception

		}

	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			if (StringUtils.isBlank(filePathAndName)) {
				return;
			}
			File myDelFile = new File(filePathAndName);
			if (myDelFile != null && myDelFile.exists() && myDelFile.isFile()) {
				myDelFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 流转成文件
	 * 
	 * @param ins
	 * @param file
	 * @return
	 */
	public static File inputStreamToFile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
				if (ins != null)
					ins.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void main(String[] args) {

		boolean b = downloadToLocal(
				"http://wx.qlogo.cn/mmopen/ajNVdqHZLLBPWu3sr1QmPkWlVVjJPibeJMF43ibkNxWH6WgrNbkb3E4Gyql9cPt6VXtJkoURoQaatDXy2JIBDMEg/0",
				"d:\\a\\abc.jpg");
		System.out.println(b);

	}
}
