package com.xsy;

import com.xsy.PostProcessor.DtpBeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DtpBeanDefinitionRegistry.class)
public class DynamicThreadPoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicThreadPoolApplication.class, args);
    }

}
