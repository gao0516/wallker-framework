package com.wallker.framework.core.mq;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketP2PProducer {

	private final Logger logger = LoggerFactory.getLogger(RocketP2PProducer.class);

	private String mqUrl;

	private String mqGroup;

	private DefaultMQProducer producer;


	public RocketP2PProducer(String mqUrl, String mqGroup) {
		this.mqUrl = mqUrl;
		this.mqGroup = mqGroup;
	}

	public DefaultMQProducer init() {
		if (producer == null) {
			producer = new DefaultMQProducer(mqGroup);
			producer.setNamesrvAddr(mqUrl);
			producer.setInstanceName(increInit());
		}
		return producer;
	}

	public String increInit() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String info = runtime.getName();
		int pid = (new Random()).nextInt();
		int index = info.indexOf("@");
		if (index > 0) {
			pid = Integer.parseInt(info.substring(0, index));
		}
		AtomicInteger atomic = new AtomicInteger();
		String result = "pid" + pid + "_index" + atomic.incrementAndGet();
		return result;
	}

	/** 可靠异步发送 **/
	public void asyncSend(String topic, String tag, String key, String msg) {
		try{
			init();
			producer.start();
			producer.setRetryTimesWhenSendAsyncFailed(0);
			Message message = new Message(topic, tag, key, msg.getBytes());
			producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                	logger.info("=====[asyncSend]=====MQ消息发送成功，内容为{}", msg);
                }
                @Override
                public void onException(Throwable e) {
                	logger.error("=====[asyncSend]=====MQ消息发送异常，内容为{}, exception:", msg, e);
                    e.printStackTrace();
                }
            });
		} catch (MQClientException e) {
			logger.error("=====[asyncSend]=====MQ消息发送异常，内容为{}, MQClientException:", msg, e);
		} catch (Exception e) {
			logger.error("=====[asyncSend]=====MQ消息发送异常，内容为{}, Exception:", msg, e);
		} finally {
			producer.shutdown();
		}
	}

	/** 可靠同步发送 **/
	public boolean syncSend(String topic, String tag, String key, String msg) {
		boolean flag = false;
		try {
			init();
			producer.start();
			producer.setRetryTimesWhenSendAsyncFailed(0);
			Message message = new Message(topic, tag, key, msg.getBytes());
			SendResult sendResult = producer.send(message);
			logger.info("=====[syncSend]====MQ消息发送成功，内容为{},sendResult:{}", msg, sendResult);
			if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
				flag = true;
			} 
		} catch (MQClientException e) {
			logger.error("=====[syncSend]====MQ消息发送失败，内容为{},MQClientException:{}", msg, e);
		} catch (Exception e) {
			logger.error("=====[syncSend]====MQ消息发送失败，内容为{},Exception:{}", msg, e);
		} finally {
			producer.shutdown();
		}
		return flag;

	}

	/** 单向发送：例如 日志收集 **/
	public void oneWaySend(String topic, String tag, String key, String msg) {
		logger.info("=====[syncSend]====topic:{},tag:{},key:{}, msg:{}", topic, tag, key, msg);
		try {
			init();
			producer.start();
			producer.setRetryTimesWhenSendAsyncFailed(0);
			Message message = new Message(topic, tag, key, msg.getBytes());
			producer.sendOneway(message);
		} catch (MQClientException e) {
			logger.error("=====[oneWaySend]====MQ消息发送失败，内容为{},MQClientException:{}", msg, e);
		} catch (Exception e) {
			logger.error("=====[oneWaySend]====MQ消息发送失败，内容为{},Exception:{}", msg, e);
		} finally {
			producer.shutdown();
		}
	}
	
	/** 顺序传递消息 **/
	public void sortedSend(){
		
	}
	
	public static void main(String[] args) {
		RocketP2PProducer p2p = new RocketP2PProducer("172.16.5.133:9876", "common-pay-producer");
		//p2p.asyncSend("TopicTest", "tag_test", "", "test:test async for consumer 1");
		p2p.syncSend("TopicTest", "tag_test", "", "test:test sync for consumer 2");
		//p2p.oneWaySend("TopicTest", "tag_test", "", "test:test oneway for consumer 1");
	}

}
