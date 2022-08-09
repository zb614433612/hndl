package com.zbdemo.hndl.utlis;

import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbing
 * @title: DozerUtil
 * @projectName hndl
 * @description: 对象转换工具类
 * @date 2022/7/15下午10:36
 */
@Component
public class DozerUtil {
    //dozer-spring-boot-stater中注入的对象
    @Resource
    private Mapper mapper;

    public DozerUtil (Mapper mapper){
        this.mapper=mapper;
    }


    //单个对象转换
    public <T> T map(Object source,Class<T> destination){
        return mapper.map(source,destination);
    }


    //List 对象转换
    public <T> List<T> mapList(List<Object> sourceList, Class<T>destination){
        if(sourceList==null||destination==null){
            return null;
        }else{
            //遍历转换
            List<T> list=new ArrayList<>();
            for(Object o:sourceList){
                T obj=map(o,destination);
                list.add(obj);
            }
            return list;
        }
    }
}
