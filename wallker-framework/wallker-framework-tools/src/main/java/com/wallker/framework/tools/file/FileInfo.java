package com.wallker.framework.tools.file;

import java.io.Serializable;

public class FileInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 文件id，即文件的md5值
	 */
	private String fileId;
	
	/**
	 * 文件名
	 */
	private String fileName;
	
	/**
	 * 文件的扩展名
	 */
	private String extName;
	
	/**
	 * 文件大小(字节)
	 */
	private Long fileSize;
	
	/**
	 * 高度(像素)(图片、视频)
	 */
	private Integer height;

	/**
	 * 宽度(像素)(图片、视频)
	 */
	private Integer width;
	
	/**
	 * 播放时长(秒)
	 */
	private Integer length;
	
	/**
	 * 页数(doc,pdf等的页数)
	 */
	private Integer pageNumber;
	
	/**
	 * 文件上传用户ID
	 */
	private Integer uploadUserId;
	
	/**
	 * 文件上传时间
	 */
	private Long uploadTi;
	
	/**
	 * 上传文件结果：1:成功，0:失败
	 */
	private Integer result;

	/**
	 * 上传结果消息
	 */
	private String msg;

	/**
	 * 签名，默认30分钟内有效
	 */
	private String sign;
	
	public FileInfo(){}

	public FileInfo(Integer result, String msg){
		this.result = result;
		this.msg = msg;
	}

	public FileInfo(String fileId, String fileName, String extName, Long fileSize, Integer uploadUserId) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.extName = extName;
		this.fileSize = fileSize;
		this.uploadUserId = uploadUserId;
		this.uploadTi = System.currentTimeMillis();
	}
	
	public FileInfo(String fileId, String fileName, String extName, Long fileSize, Integer height, Integer width,
			Integer length, Integer pageNumber, Integer uploadUserId, Long uploadTi, Integer result, String msg) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.extName = extName;
		this.fileSize = fileSize;
		this.height = height;
		this.width = width;
		this.length = length;
		this.pageNumber = pageNumber;
		this.uploadUserId = uploadUserId;
		this.uploadTi = uploadTi;
		this.result = result;
		this.msg = msg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Long getUploadTi() {
		return uploadTi;
	}

	public void setUploadTi(Long uploadTi) {
		this.uploadTi = uploadTi;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return String.format(
				"FileInfo [fileId=%s, fileName=%s, extName=%s, fileSize=%s, height=%s, width=%s, length=%s, pageNumber=%s, uploadUserId=%s, uploadTi=%s, result=%s, msg=%s, sign=%s]",
				fileId, fileName, extName, fileSize, height, width, length, pageNumber, uploadUserId, uploadTi, result,
				msg, sign);
	}
	
}
