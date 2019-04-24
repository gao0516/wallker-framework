//package com.wallker.framework.tools.setting;
//
//import com.wallker.framework.tools.file.FileUtil;
//
//
//public class FilePath {
//	
//	/**
//	 * 取文件存temp目录：配置目录（ wallker.file.tmpPath ）
//	 * @return
//	 */
//	public static String getTmpPath(){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("paat.file.tmpPath");
//		FileUtil.localMkdir(tmp);
//		return tmp;
//	}
//	
//	/**
//	 * 文件的服务器本地存储位置 ，包括路径和完整的文件名
//	 * ([配置目录（ wallker.file.normal.savePath ）]/[文件md5值前三位]/[文件md5值].[扩展名])
//	 * @param fileId  文件的md5值
//	 * @param extName 文件的扩展名
//	 * @return
//	 */
//	public static String getLocalFileName(String fileId,String extName){
//		String tmp = getLocalFilePath(fileId); 
//		tmp = String.format("%s/%s.%s", tmp,fileId,extName); 
//		return tmp;
//	}
//
//	/**
//	 * 文件的服务器本地存储位置
//	 * ([配置目录（ wallker.file.normal.savePath ）]/[文件md5值前三位]
//	 * @param fileId  文件的md5值
//	 * @return
//	 */
//	public static String getLocalFilePath(String fileId){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("wallker.file.normal.savePath");
//		FileUtil.localMkdir(tmp);
//		tmp = String.format("%s/%s", tmp,fileId.substring(0,3)); 
//		FileUtil.localMkdir(tmp);
//		return tmp;
//	}
//	
//
//	/**
//	 * 文件的hadoop存储位置：路径
//	 * ([配置目录（ wallker.hadoop.savePath ）]/[文件md5值前三位]
//	 * @param fileId  文件的md5值
//	 * @return
//	 */
//	public static String getHadoopFileSavePath(String fileId){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("wallker.hadoop.savePath");
//		tmp = String.format("%s/%s", tmp,fileId.substring(0,3)); 
//		return tmp;
//	}
//
//	/**
//	 * 扫描文件的服务器本地存储位置 ，包括路径和完整的文件名
//	 * ([配置目录（ paat.file.normal.savePath ）]/scan/[fileId前6位]/[fileId].[扩展名])
//	 * @param fileId  文件的md5值
//	 * @param extName 文件的扩展名
//	 * @return
//	 */
//	public static String getScanLocalFileName(String fileId,String extName){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("paat.file.normal.savePath");
//		FileUtil.localMkdir(tmp);
//		tmp = String.format("%s/scan", tmp); 
//		FileUtil.localMkdir(tmp);
//		tmp = String.format("%s/%s", tmp,fileId.substring(0,6)); 
//		FileUtil.localMkdir(tmp);
//		tmp = String.format("%s/%s.%s", tmp,fileId,extName); 
//		return tmp;
//	}
//	
//	/**
//	 * 扫描文件的hadoop存储位置：路径
//	 * ([配置目录（ paat.hadoop.savePath ）]/scan/[fileId前6位]
//	 * @param fileId  文件的md5值
//	 * @return
//	 */
//	public static String getScanFileHadoopSavePath(String fileId){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("paat.hadoop.savePath");
//		tmp = String.format("%s/scan/%s", tmp,fileId.substring(0,6)); 
//		return tmp;
//	}
//	
//	/**
//	 * 图片的服务器本地存储位置 ，包括路径和完整的文件名
//	 * ([配置目录（ paat.file.image.savePath ）]/[文件md5值前三位]/[文件md5值].[扩展名])
//	 * @param fileId  文件的md5值
//	 * @param extName 文件的扩展名
//	 * @return
//	 */
//	public static String getLocalImgName(String fileId,String extName){
//		String tmp = getLocalImgPath(fileId); 
//		tmp = String.format("%s/%s.%s", tmp,fileId,extName); 
//		return tmp;
//	}
//	
//	/**
//	 * 图片的服务器本地存储位置
//	 * ([配置目录（paat.file.image.savePath ）]/[文件md5值前三位]
//	 * @param fileId  文件的md5值
//	 * @return
//	 */
//	public static String getLocalImgPath(String fileId){
//		AppSettings as = AppSettings.getInstance();
//		String tmp = as.get("paat.file.image.savePath");
//		FileUtil.localMkdir(tmp);
//		tmp = String.format("%s/%s", tmp,fileId.substring(0,3)); 
//		FileUtil.localMkdir(tmp);
//		return tmp;
//	}
//	
//}
