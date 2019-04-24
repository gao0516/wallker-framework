package com.wallker.framework.core.thread;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPool {
	
	private Integer corePoolSize;
	
	private Integer maxPoolSize;
	
	private Integer queueCapacity;
	
	private Integer keepAliveTime;
	
	public ThreadPool(Integer corePoolSize,Integer maxPoolSize,Integer queueCapacity,Integer keepAliveTime) {
		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
		this.queueCapacity = queueCapacity;
		this.keepAliveTime = keepAliveTime;
	}
	
	@Bean
    public ThreadPoolTaskExecutor defaultThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目 cpu*2
        executor.setCorePoolSize(corePoolSize);
        
        //指定最大线程数 cpu*25
        executor.setMaxPoolSize(maxPoolSize);
        
        //队列中最大的数目是否為有界，或者无界队列
        executor.setQueueCapacity(queueCapacity);
        
        //线程名称前缀
        executor.setThreadNamePrefix("defaultThreadPool_");
        
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        //线程空闲后的最大存活时间 
        executor.setKeepAliveSeconds(keepAliveTime);
        
        //加载
        executor.initialize();
        return executor;
    }


}
