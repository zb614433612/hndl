package com.zbdemo.hndl.utlis;

import java.util.UUID;

/**
 * @description
 * @auther Caoheyi
 * @date 2020/9/2 11:54 上午
 */
public class StringUtil {

    /**
     * 生成UUID
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        String result = uuid.toString().replaceAll("-", "").toLowerCase();
        return result;
    }
}
