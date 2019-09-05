package tech.mhuang.ext.spring.util;

import tech.mhuang.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 数据处理工具类.
 * 该工具类基于spring提供的实现
 *
 * @author mhuang
 * @since 1.0.0
 */
public class DataUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    /**
     * 将源列表的数据复制到新列表的类上
     *
     * @param source           源列表
     * @param destinationClass 转换后的类
     * @param <E>              转换后的类型
     * @return E 返回转换后类的列表
     */
    public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass) {
        return copyTo(source, destinationClass, (String[]) null);
    }

    /**
     * 将源列表的数据复制到新列表的类上、并且忽略对应的属性
     *
     * @param source           源列表
     * @param destinationClass 转换后的类
     * @param ignore           忽略的源列表的属性
     * @param <E>              转换后的类类型
     * @return E   返回转换后类的列表
     */
    public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass, String... ignore) {
        if (CollectionUtil.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<E> res = new ArrayList<>(source.size());
        source.stream().forEach(o -> {
            try {
                E e = destinationClass.newInstance();
                BeanUtils.copyProperties(o, e, ignore);
                res.add(e);
            } catch (Exception e) {
                logger.error("copy数据异常失败", e);
            }
        });

        return res;
    }

    /**
     * 对象Copy
     *
     * @param source           原对象的类
     * @param destinationClass copy后存放的对象
     * @param <E>              copy后存放的对象类型
     * @return E 返回copy存放的类对象
     */
    public static <E> E copyTo(Object source, Class<E> destinationClass) {
        return copyTo(source, destinationClass, (String[]) null);
    }

    /**
     * 对象copy忽略对应参数
     *
     * @param source           原对象的类
     * @param destinationClass copy后存放的对象
     * @param <E>              copy后存放的对象类型
     * @param ignore           copy中忽略的字段
     * @return E 返回copy存在的类对象
     */
    public static <E> E copyTo(Object source, Class<E> destinationClass, String... ignore) {
        try {
            E e = destinationClass.newInstance();
            BeanUtils.copyProperties(source, e, ignore);
            return e;
        } catch (Exception e) {
            logger.error("copy数据异常失败", e);
        }
        return null;
    }

    /**
     * 根据模块名和参数获取value，适合不知道值的情况
     *
     * @param model      需要获取的模块名
     * @param key        需要获取的key
     * @param valueClass 需要获取的key的值的对象
     * @param <E>        需要获取的key的值的对象类型
     * @return E 返回对应值的类型
     * <p>
     * Object user,
     * 比如 User类中有个userName的字段。你现在不知道类名，可通过 DataUtils.getValueByModelKey(user,"username",String.class)
     */
    public static <E> E getValueByModelKey(Object model, String key, Class<E> valueClass) {
        Object result = null;
        Class source = model.getClass();
        PropertyDescriptor sourceKeyPd = BeanUtils.getPropertyDescriptor(source, key);
        if (sourceKeyPd != null) {
            Method readMethod = sourceKeyPd.getReadMethod();
            if (readMethod != null) {
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    result = readMethod.invoke(model);
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(false);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("access异常", e);
                } catch (IllegalArgumentException e) {
                    logger.error("参数异常", e);
                } catch (InvocationTargetException e) {
                    logger.error("目标调用异常", e);
                }
            }
        }
        return (E) result;
    }

    /**
     * copy對象中的某個屬性儅作爲key和值在組成對應的實體
     *
     * @param sourceList       copy的對象列表
     * @param destinationClass copy的值
     * @param <E>              copy的值的类型
     * @param fieldKey         对应copy实体得字段
     * @param fieldValue       对应copy实体后字段得值。
     * @return E 返回copy后对应的实体
     * 将List对象转换成对应得实体
     * 示例：
     * <p>
     * 比如 list
     * [{"key":"username","value":"huangmiao"},{"key":"age",value : 12}]
     * 转换成
     * {
     * "username" : "huangmiao",
     * "age" : 12
     * }
     * 调用方式 DataUtils.copyToKeyValue(list,User.class,"key","value")
     */
    public static <E> E copyToKeyValue(List<?> sourceList, Class<E> destinationClass, String fieldKey, String fieldValue) {
        try {
            E e = destinationClass.newInstance();

            if (CollectionUtil.isEmpty(sourceList)) {
                return null;
            }

            Class target = e.getClass();
            PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target);
            for (PropertyDescriptor targetPd : targetPds) {
                Method writeMethod = targetPd.getWriteMethod();
                if (writeMethod != null) {
                    for (Object obj : sourceList) {
                        Class source = obj.getClass();

                        PropertyDescriptor sourceKeyPd = BeanUtils.getPropertyDescriptor(source, fieldKey);
                        if (sourceKeyPd != null) {
                            Method readMethod = sourceKeyPd.getReadMethod();
                            if (readMethod != null) {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object key = readMethod.invoke(obj);
                                String setMethod = writeMethod.getName();
                                setMethod = key.toString().charAt(0) + setMethod.substring(4);
                                if (Objects.equals(key, setMethod)) {
                                    PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source, fieldValue);
                                    if (sourcePd != null) {
                                        Method readValueMethod = sourcePd.getReadMethod();
                                        if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readValueMethod.getReturnType())) {
                                            try {
                                                if (!Modifier.isPublic(readValueMethod.getDeclaringClass().getModifiers())) {
                                                    readValueMethod.setAccessible(true);
                                                }
                                                Object value = readValueMethod.invoke(obj);
                                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                                    writeMethod.setAccessible(true);
                                                }
                                                writeMethod.invoke(e, value);
                                            } catch (Throwable ex) {
                                                throw new FatalBeanException(
                                                        "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return e;
        } catch (Exception e) {
            logger.error("数据复制异常", e);
        }
        return null;
    }

    /**
     * 将原对象的类copy到目标对象类中
     *
     * @param source 原对象类
     * @param target 目标对象类
     */
    public static void copyTo(Object source, Object target) {
        try {
            BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            logger.error("copy数据异常失败", e);
        }
    }
}
