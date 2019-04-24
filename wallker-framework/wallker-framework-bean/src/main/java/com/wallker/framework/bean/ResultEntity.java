package com.wallker.framework.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
public class ResultEntity implements Serializable {

	// 支付状态
	private String status;

	// 支付返回码
	private String payCode;

	// 支付返回说明
	private String msg;

	// 报文体
	private JSONObject body;

	public ResultEntity() {

	}

	public ResultEntity(String status, String payCode, String msg) {
		this.status = status;
		this.payCode = payCode;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject body) {
		this.body = body;
	}

}
