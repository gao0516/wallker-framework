package com.wallker.framework.enums;

public enum SysCodeEnum {
	
	SUCC("S0000",""),
	ING("I0000","交易中."),
	FENOUGH("F000","余额不足."),
	EPARAM("E0001","参数缺失."),
	EEXIST("E0002","订单已存在."),
	EUNEXIST("E0003","订单不存在."),
	UEXIST("E0004","用户不存在."),
	EEXCEP("E0005","乐观锁异常"),
	
	EXCEPTION("E0000","系统异常"),
	;
	
	private String code;
	private String msg;
	
	private SysCodeEnum(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public static String getMsgByCode(String code){
		for(SysCodeEnum sysCode : SysCodeEnum.values()){
			if(sysCode.getCode().equals(code)){
				return sysCode.getMsg();
			}
			
		}
		return "";
	}
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
