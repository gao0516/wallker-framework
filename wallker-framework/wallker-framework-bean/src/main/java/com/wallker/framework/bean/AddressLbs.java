package com.wallker.framework.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AddressLbs implements Serializable{
	
	protected String lng;//经度
	protected String lat;//纬度
	
	protected String source;
	protected String province;//省
	protected String city;//市
	protected String district;//区
	protected String street;//街道
	
	public AddressLbs(String lng,String lat){
		this.lng = lng;
		this.lat = lat;
	}


}
