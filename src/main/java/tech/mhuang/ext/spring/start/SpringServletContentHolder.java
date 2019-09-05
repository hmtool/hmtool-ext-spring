package tech.mhuang.ext.spring.start;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring servlet 使用工具
 *
 * @author mhuang
 * @since 1.0.0
 */
public final class SpringServletContentHolder {

    /**
     * 获取bean工厂类
     *
     * @param request 获取的servlet
     * @return 返回Bean工厂
     */
    public static BeanFactory getBeanFactory(final HttpServletRequest request) {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
    }

    /**
     * 根据request获取bean
     *
     * @param clazz   获取的bean class或interface
     * @param <T>     bean的类型
     * @param request 获取的bean的Request
     * @return 返回对应Bean
     */
    public static <T> T getBean(final Class<T> clazz,final HttpServletRequest request) {
        BeanFactory factory = getBeanFactory(request);
        return factory.getBean(clazz);
    }
}
