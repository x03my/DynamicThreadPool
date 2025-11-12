package com.xsy.ThreadPool;

import com.xsy.Dto.ThreadPoolProperties;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;


public class DtpThreadPool extends ThreadPoolExecutor {


    public DtpThreadPool(ThreadPoolProperties threadPoolProperties) {
        super(
                threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(),
                threadPoolProperties.getTimeUnit(),
                new ArrayBlockingQueue<>(threadPoolProperties.getQueueSize())
        );
    }

}
