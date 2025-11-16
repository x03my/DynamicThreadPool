package com.xsy.Timing;

import com.xsy.Registry.DtpRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/*
 *     扩展点 当实现SmartLifecycle接口后，spring容器启动时会自动调用start方法
 *      当isRunning返回为false时，spring容器不会调用start方法
 *
 */


@Component
@Slf4j
public class DtpMonitor implements SmartLifecycle {

    private ScheduledFuture<?> scheduledFuture;
    private boolean running;


    private void monitor() {
        for (String name :  DtpRegistry.getAllExecutorName()) {
            ThreadPoolExecutor dtpExecutor =(ThreadPoolExecutor) DtpRegistry.getExecutor(name);

            log.info("线程池名字：{}，线程池核心线程数：{}，线程池最大线程数：{}，线程池当前线程数：{}", name, dtpExecutor.getCorePoolSize(), dtpExecutor.getMaximumPoolSize(), dtpExecutor.getActiveCount());
        }
    }

    private void alarm() {
        // 读取配置
        int max = 10;
        for (Executor executor : DtpRegistry.getAllExecutor()) {
            ThreadPoolExecutor threadPoolExecutor=(ThreadPoolExecutor)executor;
            int activeCount = threadPoolExecutor.getActiveCount();
            if (activeCount >= max) {
                log.warn("告警，当前线程池的线程个数为{}，告警阈值为{} ", activeCount, max);
            }
        }
    }

    @Override
    public void start() {
        /*
         *      启动监控线程
         *        定时任务没5秒
         */

        scheduledFuture = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            monitor();
            alarm();
        }, 10, 10, TimeUnit.SECONDS);
        running = true;

    }

    @Override
    public void stop() {
        scheduledFuture.cancel(false);
        running = false;

    }

    @Override
    public boolean isRunning() {
        return running;
    }
}