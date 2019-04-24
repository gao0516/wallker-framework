package com.wallker.framework.core.redis;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterFactory {
	
	
	/** 集群地址,例子：172.19.116.82:7000,172.19.116.82:7001,172.19.116.82:7002,172.19.116.82:7003,172.19.116.82:7004,172.19.116.82:7005 **/
	private String addresses;
	
	/** 密码 **/
	private String password;
	
	/** 最大等待时间 **/
	private String maxWaitMillis = "-1";
	
	private int maxTotal = 1000;
	
	private int minIdle = 8;
	
	private int maxIdle = 100;
	
	private int timeout = 1000;
	
	private int soTimeout = 800;
	
	private int maxRedirections = 6;
	
	public JedisClusterFactory(String addresses, String password){
		this.addresses = addresses;
		this.password = password;
	}
	
	public JedisCluster init(){
		Set<HostAndPort> hostSet = new HashSet<>();
		String [] addressArray = addresses.split(",");
		for(String address : addressArray){
			String host = address.substring(0, address.indexOf(":"));
			String port = address.substring(address.indexOf(":")+1);
			HostAndPort hosts = new HostAndPort(host, Integer.parseInt(port));
			hostSet.add(hosts);
		}
		GenericObjectPoolConfig<?>  genericPoolConfig = new GenericObjectPoolConfig<>();
		genericPoolConfig.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
		genericPoolConfig.setMaxTotal(maxTotal);
		genericPoolConfig.setMinIdle(minIdle);
		genericPoolConfig.setMaxIdle(maxIdle);
		JedisCluster jedisCluster = new JedisCluster(hostSet, timeout, soTimeout, maxRedirections, password, genericPoolConfig);
		return jedisCluster;
	}
}
