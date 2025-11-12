package com.xsy.Event;


import com.xsy.Dto.ThreadPoolProperties;
import com.xsy.Registry.DtpRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/*
 * 监听事件
 */
@Component
@Slf4j
public class DtpEventListener {


    @EventListener(DtpEvent.class)
    public void onApplicationEvent(DtpEvent event) {
        log.info("监听到事件：" + event);
        ThreadPoolProperties properties = event.getProperties();
        log.info("修改线程池的参数：" + properties.toString());
        // 刷新线程池的参数
        DtpRegistry.refresh(properties.getPoolName() , properties);

    }



}
