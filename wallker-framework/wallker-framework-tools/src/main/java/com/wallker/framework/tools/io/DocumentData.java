package com.wallker.framework.tools.io;

import java.io.Serializable;

/**
 * @Filename: DocumentData
 * @Description:
 * @Author: fu.zhao@PAAT.com
 * @Version: 1.0
 * @CreateTime: 2018/9/17 17:33
 * @History:
 */
@SuppressWarnings("serial")
public class DocumentData implements Serializable {
    /**
     * 文件地址
     */
    private String src;
    /**
     * 文件名字
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Integer fileSize;
    /**
     * 文件格式
     */
    private String fileFormat;
    /**
     * 文件ID
     */
    private String fileId;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "DocumentData{" +
                "src='" + src + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
