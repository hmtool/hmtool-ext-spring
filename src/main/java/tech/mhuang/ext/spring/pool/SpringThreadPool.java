package tech.mhuang.ext.spring.pool;

import tech.mhuang.core.pool.BaseExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * spring 线程池、供kafka使用
 *
 * @author mhuang
 * @since 1.0.0
 */
public class SpringThreadPool extends ThreadPoolTaskExecutor implements BaseExecutor {

}
