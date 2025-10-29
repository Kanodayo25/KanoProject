package com.kano.project.common.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.kano.project.common.config.CosClientConfig.getCosClient;

@Slf4j
public class CosUtils {
    //腾讯云COS服务器外网暴露地址
    private static String URL = "https://troubleinf-1301296837.cos.ap-guangzhou.myqcloud.com";

    //腾讯云COS服务器桶名
    private static String BUCKET_NAME = "troubleinf-1301296837";

    //通过文件路径和文件路径，文件保存的地址，后面成为存储桶的key
    public static String getFilePath(String dir,String fileName){
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String filePath =dir + UUID.randomUUID() + fileType;
        return filePath;
    }

    /**
     * 图片上传
     * @param file 文件对象
     * @return
     */
    public static String uploadImg(MultipartFile file){
        COSClient cosClient = getCosClient();
        InputStream inputStream = null;
        try {
            String filename = file.getOriginalFilename();
            inputStream = file.getInputStream();
            //设置存储的目录
            String filePath = getFilePath("infoImage/",filename);

            // 上传文件
            cosClient.putObject(new PutObjectRequest(BUCKET_NAME, filePath, inputStream, null));
            cosClient.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);

            return URL + "/" + filePath;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cosClient.shutdown();
        }
        return null;
    }

}
