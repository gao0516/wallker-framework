/**
 * 二维码信息
 */
package com.wallker.framework.tools.qrcode;

public class QRCodeInfo {

	/**
	 * 二维码大小
	 */
	public Integer width;
	
	/**
	 * 二维码内容
	 */
	public String content;
	
	/**
	 * 文件ID
	 */
	public String fileId;
	/**
	 * md5
	 */
	public String md5;

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public QRCodeInfo() {}
	
	@Override
	public String toString() {
		return String.format("QRCodeInfo [width=%s, content=%s, fileId=%s, md5=%s]", width, content, fileId, md5);
	}

	public QRCodeInfo(Integer width, String content, String fileId) {
		super();
		this.width = width;
		this.content = content;
		this.fileId = fileId;
	}
	
}
