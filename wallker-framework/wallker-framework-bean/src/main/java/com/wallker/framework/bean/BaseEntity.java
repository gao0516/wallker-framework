package com.wallker.framework.bean;

import java.io.Serializable;

import javax.xml.crypto.Data;

@SuppressWarnings("serial")
public class BaseEntity implements Serializable{
	private Long id;
	
	private Data createTime;
	
	private Data updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Data getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Data createTime) {
		this.createTime = createTime;
	}

	public Data getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Data updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
