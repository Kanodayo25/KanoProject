package com.kano.project.common.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosClientConfig {

    // 上文获得的SecretId
    private static String secretId = "AKIDzqZ6VlqjR2VnxypUbdXFnANPGpFWcWGr";

    // 上文获得的SecretKey
    private static String secretKey = "VoeQ1TkpnaosmaUN4dIpAKI3g7MRJ0Um";

    //cos地址
    private static String cosAddress = "ap-guangzhou";

    public static COSClient getCosClient(){
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2.1 设置存储桶的地域（上文获得）
        Region region = new Region(cosAddress);
        ClientConfig clientConfig = new ClientConfig(region);
        // 2.2 使用https协议传输
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 返回COS客户端
        return cosClient;
    }
}
