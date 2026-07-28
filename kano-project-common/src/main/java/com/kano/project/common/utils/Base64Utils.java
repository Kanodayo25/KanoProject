package com.kano.project.common.utils;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {
    /**
     * Base64编码
     *
     * @param text 明文
     * @return Base64字符串
     */
    public static String encode(String text){

        byte[] bytes =
                text.getBytes(StandardCharsets.UTF_8);


        return Base64.getEncoder()
                .encodeToString(bytes);

    }



    /**
     * Base64解码
     *
     * @param base64 Base64字符串
     * @return 原文
     */
    public static String decode(String base64){

        byte[] bytes =
                Base64.getDecoder()
                        .decode(base64);


        return new String(
                bytes,
                StandardCharsets.UTF_8
        );

    }



    /**
     * 文件Base64编码
     *
     */
    public static String encode(byte[] data){

        return Base64.getEncoder()
                .encodeToString(data);

    }



    /**
     * Base64转byte数组
     *
     */
    public static byte[] decodeToBytes(String base64){

        return Base64.getDecoder()
                .decode(base64);

    }

}
