package com.xsy.Event;



import com.xsy.Dto.ThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Slf4j
@Component
public class ConfigNacosEvent implements ApplicationListener<EnvironmentChangeEvent> , ApplicationEventPublisherAware {


    ApplicationEventPublisher applicationEventPublisher;

    private Map<Integer , ThreadPoolProperties> executors = new ConcurrentHashMap<>();
    private static final Pattern EXECUTOR_INDEX_PATTERN = Pattern.compile("spring\\.Dtp\\.executors\\[(\\d+)\\]");



    @Resource
    private ConfigurableEnvironment environment;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {

        if ( null == event.getKeys()){
            return;
        }
        log.info("===== 全局配置变更事件触发 =====");
        // 打印所有变更的配置键
        log.info("event.getKeys() : {} " , event.getKeys());

        event.getKeys().forEach(key -> {

            Matcher matcher = EXECUTOR_INDEX_PATTERN.matcher(key);
            if (matcher.find()) {
                // 正则表达式匹配线程池的序号
                String group = matcher.group(1);
                Integer i = Integer.valueOf(group);

                // 获取修改的参数名
                int index = key.lastIndexOf(".");
                String value = index == -1 ? key : key.substring(index + 1);

                // 判断是否已经存在
                if ( executors.size() < 1 || executors.get(i) == null){
                    ThreadPoolProperties threadPoolProperties = new ThreadPoolProperties();
                    threadPoolProperties.setPoolName("dtpExecutor" + ( i + 1));
                    switch (value){
                        case "corePoolSize":
                            threadPoolProperties.setCorePoolSize(Integer.parseInt(environment.getProperty(key)));
                            break;
                        case "maximumPoolSize":
                            threadPoolProperties.setMaximumPoolSize(Integer.parseInt(environment.getProperty(key)));
                            break;
                    }
                    executors.put(i,threadPoolProperties);

                }else {
                    ThreadPoolProperties threadPoolProperties = executors.get(i);
                    switch (value){
                        case "corePoolSize":
                            threadPoolProperties.setCorePoolSize(Integer.parseInt(environment.getProperty(key)));
                            break;
                        case "maximumPoolSize":
                            threadPoolProperties.setMaximumPoolSize(Integer.parseInt(environment.getProperty(key)));
                            break;
                    }
                }
            }
        });
        // 存储完成 刷星线程池
        handleDtpConfigChange();
    }

    private void handleDtpConfigChange() {
        // 遍历hashmap刷新动态线程池
        Collection<ThreadPoolProperties> values = executors.values();
        for (ThreadPoolProperties value : values) {
            applicationEventPublisher.publishEvent(new DtpEvent(value));
        }
        // 刷新完成清空map集合
        executors.clear();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
