package com.wallker.framework.tools.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.wallker.framework.tools.date.DateUtil;
import com.wallker.framework.tools.utils.ChangeTypeUtil;

public class FileCopy {

    
	/** 
     * 写文件到当前目录的upload目录中 
     * @param in 
     * @param fileId
     * @return 文件名（不含路径 和 后缀）
     */  
    public static String saveFileToSavePath(InputStream in, String fileId, String savePath){
    	try{
    		String fileName = String.format("%s_%s", DateUtil.getCurrentDate("yyyyMMddHHmmss"),fileId);
    		if(StringUtils.isEmpty(savePath)){
    			return "";
    		}
    		String ym = savePath.split("/")[savePath.split("/").length-1];
    		if(!fileName.substring(0,6).equals(ym)){
    			fileName = String.format("%s_%s", DateUtil.getCurrentDate("yyyyMMddHHmmss"),fileId);
    		}
	        String realName = String.format("%s/%s.%s" ,savePath, fileName , "jpg");
	        if(!saveFileToSavePath2(in,realName).equals("")){
	        	return fileName;
	        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "";
    } 
    
    /** 
     * 写文件到当前目录的temp目录中 
     * @param in 
     * @param fileName 
     * @throws IOException
     */  
    public static String saveFileToTmpPath(InputStream in, String fileName, String extName, String filePath){
    	try{
    		String fileId = String.format("%s_%s", DateUtil.getCurrentDate1(),ChangeTypeUtil.randomNumber(6));
	        String realName = String.format("%s/%s.%s" , filePath, fileId , extName);
	        return saveFileToSavePath2(in,realName);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "";
    }  

	/** 
     * 写文件到目标文件
     * @param in 
     * @param tarFile
     * @return 目标文件，若失败，则返回空字符串
     */  
    public static String saveFileToSavePath2(InputStream in, String tarFile){
    	try{
	        File file = new File(tarFile);
	        file.createNewFile();  
	        FileUtils.copyInputStreamToFile(in, file); 
	        return tarFile;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return "";
    } 
    
    public static void main(String args[])    {
    }
}
