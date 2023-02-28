package com.kano.project.common.utils;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class ImageUtils {
    /**
     * 通过图片的url获取图片的base64字符串
     *
     * @param imgUrl 图片url
     * @return 返回图片base64的字符串
     */
    public static String imgToBase64(String imgUrl) {
        log.info("imgToBase64入参imgUrl={}", imgUrl);
        URL url;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        String base64 = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            base64 = encoder.encode(outStream.toByteArray());
        } catch (Exception e) {
            log.error("imgToBase64读取图片异常：" + e.getMessage());
        } finally {
            try {
                if (Objects.nonNull(is)) {
                    is.close();
                }
                if (Objects.nonNull(outStream)) {
                    outStream.close();
                }
            } catch (IOException e) {
                log.error("imgToBase64关闭流异常:" + e.getMessage());
            }
            if (Objects.nonNull(httpUrl)) {
                httpUrl.disconnect();
            }
        }
        log.info("imgToBase64出参base64={}", base64);
        return base64;
    }

    /**
     * 本地图片转Base64
     *
     * @author yangxiaodian
     * @date 2021/8/31 20:40
     */
    public static String localImageToBase64(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(in)) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("localImageToBase64关闭流异常:" + e.getMessage());
            }
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
}
