package com.xsy.Registry;

import com.xsy.Dto.ThreadPoolProperties;
import com.xsy.ThreadPool.DtpThreadPool;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *      动态线程池的工具类
 *     减少去spring容器中获取动态线程池，直接从map中获取
 */


public class DtpRegistry {
    /*
     *     存储线程池
     */
    private static final Map<String , DtpThreadPool> executors = new ConcurrentHashMap<>();

    /*
     *     根据线程池名字获取线程池
     */
    public static DtpThreadPool getExecutor(String name){
        return executors.get(name);
    }
    /*
     *     获取所有线程池名字
     */

    public static Collection<String> getAllExecutorName(){
        return executors.keySet();
    }
    /*
     *     获取所有线程池
     */

    public static Collection<DtpThreadPool> getAllExecutor(){
        return executors.values();
    }
    /*
     *     注册线程池
     */
    public static void register(String name,DtpThreadPool executor){
        executors.put(name,executor);
    }
    /*
     *     刷新线程池的参数
     */

    public static void refresh(String name,ThreadPoolProperties executor){
        DtpThreadPool pool = executors.get(name);
        if(pool != null){
            pool.setCorePoolSize(executor.getCorePoolSize());
            pool.setMaximumPoolSize(executor.getMaximumPoolSize());

        }

    }



}
