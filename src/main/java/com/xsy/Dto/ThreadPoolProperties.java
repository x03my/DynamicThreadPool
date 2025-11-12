package com.xsy.Dto;


import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class ThreadPoolProperties {
    /**
     * 标识每个线程池的唯一名字
     */
    private String poolName;
    private String poolType = "common";

    /**
     * 是否为守护线程
     */
    private boolean isDaemon = false;

    /**
     * 以下都是核心参数
     */
    // 核心线程数
    private int corePoolSize = 1;
    // 最大线程数
    private int maximumPoolSize = 1;
    // 线程空闲时间
    private long keepAliveTime;
    // 线程存活时间
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    // 阻塞队列
    private String queueType = "arrayBlockingQueue";
    // 队列大小
    private int queueSize = 3;
    // 线程名字
    private String threadFactoryPrefix = "-td-";
    // 拒绝策略
    private String RejectedExecutionHandler;
}