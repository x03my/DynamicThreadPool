package com.xsy.PostProcessor;

import com.xsy.Dto.DtpProperties;
import com.xsy.Dto.ThreadPoolProperties;
import com.xsy.ThreadPool.DtpThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import java.util.List;
import java.util.Objects;


@Slf4j
public class DtpBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 将配置文件与类进行绑定
        BindResult<DtpProperties> bind = Binder.get(environment).bind("spring.dtp", DtpProperties.class);
        DtpProperties dtpProperties = bind.get();


        List<ThreadPoolProperties> executors = dtpProperties.getExecutors();
        if ( Objects.isNull(executors)) {
            log.info("未配置线程池");
            return;
        }


        for (ThreadPoolProperties executor : executors) {
            // 构造beanDefiniton
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DtpThreadPool.class);
            beanDefinitionBuilder.addConstructorArgValue(executor);
            // 注册bean
            registry.registerBeanDefinition(executor.getPoolName() , beanDefinitionBuilder.getBeanDefinition());
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
