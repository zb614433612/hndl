package com.zbdemo.hndl.utlis;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;
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

    /**
     * 生成纯数字随机字符串
     *
     * @param len 随机数的位数
     * @return
     */
    public static String generateCode(int len) {
        len = Math.min(len, 8);
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(
                Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0, len);
    }

    /**
     * @return
     * @Description 生成指定长度的随机字符串
     * @Param len 随机数的位数
     * @Author zhangbing
     * @Date 2020/7/16 16:50
     */
    public static String randomAlphanumeric(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }
}
