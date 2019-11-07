package tech.mhuang.ext.spring.pool;

import tech.mhuang.core.pool.BaseExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import tech.mhuang.core.pool.DefaultThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * spring 线程池
 *
 * @author mhuang
 * @since 1.0.0
 */
public class SpringThreadPool extends ThreadPoolTaskExecutor implements BaseExecutor {

    /**
     * default threadpool queue size
     */
    private final static Integer DEFAULT_QUEUE_CAPACITY = 20;

    /**
     * default threadpool keepalive secord
     */
    private final static Integer DEFAULT_KEEPALIVE_SECOND = 200;

    public SpringThreadPool(){
        this.setCorePoolSize(DefaultThreadPool.DEFAULT_CORE_POOL_SIZE);
        this.setMaxPoolSize(DefaultThreadPool.DEFAULT_MAX_POOL_SIZE);
        this.setQueueCapacity(DEFAULT_QUEUE_CAPACITY);
        this.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        this.setKeepAliveSeconds(DEFAULT_KEEPALIVE_SECOND);
    }
}
