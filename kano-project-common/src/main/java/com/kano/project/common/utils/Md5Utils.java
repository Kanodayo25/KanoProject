package com.kano.project.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Md5Utils {

    /**
     * 字符串MD5加密
     *
     * @param text 明文
     * @return 32位MD5
     */
    public static String md5(String text) {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] bytes = md.digest(text.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();

            for (byte b : bytes) {

                String hex = Integer.toHexString(0xff & b);

                if(hex.length()==1){
                    sb.append("0");
                }

                sb.append(hex);
            }

            return sb.toString();

        }catch(Exception e){

            throw new RuntimeException("MD5计算失败",e);
        }

    }



    /**
     * 带盐MD5
     *
     * @param text 明文
     * @param salt 盐值
     */
    public static String md5WithSalt(String text,String salt){

        return md5(text + salt);

    }



    /**
     * MD5校验
     *
     * @param text 明文
     * @param md5 密文
     */
    public static boolean verify(String text,String md5){

        return md5(text).equalsIgnoreCase(md5);

    }



    /**
     * 文件MD5
     *
     * @param file 文件
     */
    public static String fileMd5(File file){

        try(FileInputStream fis = new FileInputStream(file)){

            MessageDigest md =
                    MessageDigest.getInstance("MD5");


            byte[] buffer = new byte[1024 * 8];

            int length;


            while((length=fis.read(buffer))!=-1){

                md.update(buffer,0,length);

            }


            byte[] bytes = md.digest();


            StringBuilder sb = new StringBuilder();

            for(byte b:bytes){

                String hex =
                        Integer.toHexString(0xff & b);

                if(hex.length()==1){
                    sb.append("0");
                }

                sb.append(hex);

            }


            return sb.toString();


        }catch(Exception e){

            throw new RuntimeException("文件MD5失败",e);
        }

    }

}
