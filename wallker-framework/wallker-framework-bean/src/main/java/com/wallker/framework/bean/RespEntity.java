package com.wallker.framework.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
public class RespEntity implements Serializable {

	private String state;

	private String msg;

	private JSONObject data;

	public RespEntity() {

	}

	public RespEntity(String state, String msg) {
		this.state = state;
		this.msg = msg;
	}

	public RespEntity(String state, String msg, JSONObject data) {
		this.state = state;
		this.msg = msg;
		this.data = data;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	

}
