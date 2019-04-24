package com.wallker.framework.core.mq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullConsumer {

	private final Logger logger = LoggerFactory.getLogger(PullConsumer.class);

	private static final Map<MessageQueue, Long> OFFSE_MAP = new HashMap<>();

	private String mqUrl;

	private String mqGroup;

	private DefaultMQPullConsumer pullConsumer;

	public PullConsumer(String mqUrl, String mqGroup) {
		this.mqUrl = mqUrl;
		this.mqGroup = mqGroup;
	}

	public DefaultMQPullConsumer init() {
		if (pullConsumer == null) {
			pullConsumer = new DefaultMQPullConsumer(mqGroup);
			pullConsumer.setNamesrvAddr(mqUrl);
		}
		return pullConsumer;
	}

	public void pullMq(String topic) {
		logger.info("=====[pullMq]=====topic:{}", topic);
		try {
			init();
			pullConsumer.start();
			Set<MessageQueue> mqSet = pullConsumer.fetchSubscribeMessageQueues(topic);
			for (MessageQueue mq : mqSet) {
				SINGLE_MQ: while (true) {
					Long offset = OFFSE_MAP.get(mq) == null ? 0L : OFFSE_MAP.get(mq);
					PullResult pullResult = pullConsumer.pullBlockIfNotFound(mq, null, offset, 32);
					OFFSE_MAP.put(mq, pullResult.getNextBeginOffset());
					switch (pullResult.getPullStatus()) {
					case FOUND:
						List<MessageExt> messageExtList = pullResult.getMsgFoundList();
						for (MessageExt msg : messageExtList) {
							logger.info("=====[pullMq]=====message.body:{}", new String(msg.getBody()));
						}
						break;
					case NO_MATCHED_MSG:
						break;
					case NO_NEW_MSG:
						break SINGLE_MQ;
					case OFFSET_ILLEGAL:
						break;
					default:
						break;
					}
				}
			}
		} catch (MQClientException e) {
			logger.error("=====[pullMq]=====mqClientException:{}", e);
		} catch (Exception e) {
			logger.error("=====[pullMq]=====Exception:{}", e);
		} finally {
			pullConsumer.shutdown();
		}
	}

	public static void main(String [] args){
		PullConsumer pullConsumer = new PullConsumer("172.16.5.133:9876", "common-pay-producer");
		pullConsumer.pullMq("TopicTest");
	}	
	
}
