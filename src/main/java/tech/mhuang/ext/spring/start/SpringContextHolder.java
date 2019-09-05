package tech.mhuang.ext.spring.start;

import tech.mhuang.core.util.CollectionUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * spring启动装载类
 *
 * @author mhuang
 * @since 1.0.0
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static DefaultListableBeanFactory beanFacotory;

    /**
     * 获取上下文
     *
     * @return ApplicationContext 上下文
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 上下文初始化
     *
     * @param applicationContext 传递的上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (SpringContextHolder.applicationContext == null) {
            SpringContextHolder.applicationContext = applicationContext;
            beanFacotory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        }
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name bean方法名
     * @return Object 返回对应bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 通过名字获取上下文中的bean
     *
     * @param name  bean方法名
     * @param clazz 获取Bean指定的class
     * @param <T> 获取Bean指定的Class类型
     * @return BeanName的Bean
     */

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 通过名字删除上下文的bean
     *
     * @param name 删除的BeanName
     */
    public static void removeBean(String name) {
        beanFacotory.removeBeanDefinition(name);
    }

    /**
     * bean注册
     * @param beanName 注册的Bean Name
     * @param clazz 注册的Bean class
     * @param <T> 注册的Bean Class类型
     * @return 返回注册的Bean
     */
    public static <T> T registerBean(String beanName,Class<T> clazz){
        return registerBean(beanName,clazz,new HashMap<>());
    }

    /**
     * bean注册
     *
     * @param beanName 注册的Bean Name
     * @param clazz   注册的Bean class
     * @param <T> 注册的Bean Class类型
     * @param params 注册的Bean 传递的参数
     * @return  返回注册的Bean
     */
    public static <T> T registerBean(String beanName, Class<T> clazz, Map<String, Object> params) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        if(CollectionUtil.isNotEmpty(params)){
            params.forEach((key, value) -> beanDefinitionBuilder.addPropertyValue(key, value));
        }
        beanFacotory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        return getBean(beanName, clazz);
    }

    /**
     * 根据class获取上下文的bean
     *
     * @param requiredType 获取Bean的Class
     * @return Object 返回对应Bean
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}