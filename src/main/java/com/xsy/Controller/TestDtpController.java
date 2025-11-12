package com.xsy.Controller;


import com.xsy.Dto.ThreadPoolProperties;
import com.xsy.Event.DtpEvent;
import com.xsy.ThreadPool.DtpThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dtp")
public class TestDtpController implements ApplicationEventPublisherAware {

    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    DtpThreadPool dtpExecutor1;

    @GetMapping("/test")
    public String addOrder2(){

        dtpExecutor1.execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("下单...");
        });
        return "success!";
    }

    @PostMapping("/refresh")
    public String refresh(ThreadPoolProperties properties){
        applicationEventPublisher.publishEvent(new DtpEvent(properties));
        return "success!";
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
