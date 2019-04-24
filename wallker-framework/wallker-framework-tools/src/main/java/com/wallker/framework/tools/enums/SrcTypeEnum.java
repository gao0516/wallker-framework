package com.wallker.framework.tools.enums;

/**
 * @Filename: SrcTypeEnum
 * @Description: 获取内容中的文件类型枚举
 * @Author: fu.zhao@PAAT.com
 * @Version: 1.0
 * @CreateTime: 2018/9/11 16:38
 * @History:
 */
public enum SrcTypeEnum {
    /**
     * 图片标签（单个标签）
     */
    IMG(1, "img"),
    /**
     * 视频标签（多个标签 中间用 ; 隔开 英文 ; 号）
     */
    VIDEO(2, "video;embed"),;

    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    SrcTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
