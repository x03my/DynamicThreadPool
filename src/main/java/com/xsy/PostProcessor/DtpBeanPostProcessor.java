package com.xsy.PostProcessor;

import com.xsy.Registry.DtpRegistry;
import com.xsy.ThreadPool.DtpThreadPool;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/*
 *  扩展点beanPostPocessor bean的后置处理器
 */
@Component
public class DtpBeanPostProcessor implements BeanPostProcessor {
    private DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 判断是否实现DtpThreadPool的类 是则将其使用动态线程池的工具类将其注册到map中
        if (bean instanceof DtpThreadPool) {
            //直接纳入管理
            DtpRegistry.register(beanName, (DtpThreadPool) bean);
        }
        return bean;
    }
}