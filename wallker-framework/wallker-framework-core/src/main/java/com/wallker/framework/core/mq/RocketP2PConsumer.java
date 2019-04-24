package com.wallker.framework.core.mq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketP2PConsumer {

	private final Logger logger = LoggerFactory.getLogger(RocketP2PConsumer.class);

	private String mqUrl;

	private String mqGroup;

	private DefaultMQPushConsumer consumer;

	public RocketP2PConsumer(String mqUrl, String mqGroup) {
		this.mqUrl = mqUrl;
		this.mqGroup = mqGroup;
	}

	public DefaultMQPushConsumer init() {
		if (consumer == null) {
			consumer = new DefaultMQPushConsumer(mqGroup);
			consumer.setNamesrvAddr(mqUrl);
			// 这里设置的是一个consumer的消费策略
			// CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
			// CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
			// CONSUME_FROM_TIMESTAMP
			// 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		}
		return consumer;
	}

	public void consumerMsg(String topic) {
		init();
		try {
			// 设置consumer所订阅的Topic和Tag，*代表全部的Tag
			
			consumer.subscribe(topic, "*");
			// 设置一个Listener，主要进行消息的逻辑处理
			// 注意这里使用的是MessageListenerConcurrently这个接口
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {

					logger.info("=====[consumerMsg]=====topic:{}", topic);
					byte [] body = msgs.get(0).getBody();
					logger.info("=====[consumerMsg]=====msg new String:{}", new String(body));	
					
					/**
					 * 返回消费状态:
					 * CONSUME_SUCCESS 消费成功
					 * RECONSUME_LATER 消费失败，需要稍后重新消费
					 * **/ 
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			// 调用start()方法启动consumer
			consumer.start();
		} catch (Exception e) {
		}
	}
	
	public static void main(String [] args ){
		RocketP2PConsumer  p2p = new RocketP2PConsumer("172.16.5.133:9876", "common-pay-producer");
		p2p.consumerMsg("TopicTest");
		
	}
	
}
