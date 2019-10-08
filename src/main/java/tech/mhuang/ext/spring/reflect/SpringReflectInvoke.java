package tech.mhuang.ext.spring.reflect;

import tech.mhuang.core.reflect.BaseReflectInvoke;
import tech.mhuang.ext.spring.start.SpringContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * 反射调用（依赖spring）
 *
 * @author mhuang
 * @since 1.0.0
 */
public class SpringReflectInvoke implements BaseReflectInvoke {
    @Override
    public <T> T getMethodToValue(Class<?> clazz, String methodName, Object... params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = clazz.getClass().getMethod(methodName);
        return (T) method.invoke(clazz);
    }

    @Override
    public <T> T getMethodToValue(String clazzName, String methodName, Object... params) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object bean =  SpringContextHolder.getBean(clazzName);
        Class<?>[] clazzes = new Class[params.length];
        int index = 0;
        for(Object obj : params){
            clazzes[index++] = checkType(obj);
        }
        Method method = bean.getClass().getMethod(methodName, clazzes);
        return (T) method.invoke(bean,params);
    }
}
