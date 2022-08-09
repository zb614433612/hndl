package com.zbdemo.hndl.utlis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhangbing
 * @ClassName: EntityUtil
 * @Description: 实体工具类
 * @date 2020/7/28 10:15
 * @Copyright
 */
public class EntityUtil {

    /**
     * 初始化新增参数
     * @param object
     * @throws Exception
     */
    public static void initInsert(Object object) throws Exception {
        Class cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        Method[] methods = cls.getDeclaredMethods();
        for (Field field:fields) {
            if ("id".equals(field.getName())){
                UUID uuid = UUID.randomUUID();
                String uu = uuid.toString().replaceAll("-", "");
                Method method = getMethod(field.getName(), methods);
                method.invoke(object, uu);
            }
            if ("createTime".equals(field.getName())){
                Method method = getMethod(field.getName(), methods);
                method.invoke(object, System.currentTimeMillis()/1000);
            }
        }
    }

    /**
     * 初始化修改参数
     * @param object
     * @throws Exception
     */
    public static void initUpdate(Object object) throws Exception {
        Class cls = object.getClass();
        Field[] fields = cls.getDeclaredFields();
        Method[] methods = cls.getDeclaredMethods();
        for (Field field:fields) {
            if ("updateTime".equals(field.getName())){
                Method method = getMethod(field.getName(), methods);
                method.invoke(object, System.currentTimeMillis()/1000);
            }
        }
    }

    /**
     * 获取相应属性的set方法
     * @param fieldName
     * @param methods
     * @return
     */
    private static Method getMethod(String fieldName, Method[] methods) {
        for(Method method: methods) {
            if(!method.getName().startsWith("set")) {
                continue;
            }
            if(method.getName().toLowerCase().endsWith(fieldName.toLowerCase())) {
                return method;
            }
        }
        return null;
    }
}
