package com.wallker.framework.core.mq;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushConsumer {
	
	private final Logger logger = LoggerFactory.getLogger(PushConsumer.class);

	private String mqUrl;

	private String mqGroup;
	
	private DefaultMQPushConsumer pushConsumer;
	
	private PushConsumer (String mqUrl, String mqGroup){
		this.mqUrl = mqUrl;
		this.mqGroup = mqGroup;
	}
	
	public DefaultMQPushConsumer  init(){
		if(pushConsumer == null){
			pushConsumer = new DefaultMQPushConsumer();
			pushConsumer.setConsumerGroup(mqGroup);
			pushConsumer.setNamesrvAddr(mqUrl);
		}
		return pushConsumer;
	}
	
	public void pushMsg(String topic){
		init();
		try{
			pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			//pushConsumer.subscribe(topic, "createTag||payTag||sendTag"); //指定消
			pushConsumer.subscribe(topic, "*"); //指定消
			pushConsumer.registerMessageListener(new MessageListenerOrderly() {
				@Override
				public ConsumeOrderlyStatus consumeMessage(List<MessageExt> mexts,
						ConsumeOrderlyContext context) {//这里使用有序的监听
					// Auto-generated method stub
					logger.info("=====[pushMsg]=====push msg.body:{}", new String(mexts.get(0).getBody()));
					return ConsumeOrderlyStatus.SUCCESS;
				}
			});
			pushConsumer.start();
		}catch (MQClientException e) {
			logger.error("=====[pushMsg]=====mqClientException:{}", e);
		} catch (Exception e) {
			logger.error("=====[pushMsg]=====Exception:{}", e);
		} finally {
			pushConsumer.shutdown();
		}

	}
	
	public static void main(String [] args){
		PushConsumer pushConsumer = new PushConsumer("172.16.5.133:9876", "common-pay-producer");
		pushConsumer.pushMsg("TopicTest");
		
	}
}
