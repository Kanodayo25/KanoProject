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

    // 单例模式：静态内部类实现线程安全的懒加载
    private static class CosClientHolder {
        private static final COSClient INSTANCE = createCosClient();
        
        private static COSClient createCosClient() {
            // 1 初始化用户身份信息（secretId, secretKey）。
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2.1 设置存储桶的地域（上文获得）
            Region region = new Region(cosAddress);
            ClientConfig clientConfig = new ClientConfig(region);
            // 2.2 使用https协议传输
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 生成 cos 客户端。
            return new COSClient(cred, clientConfig);
        }
    }

    /**
     * 获取COS客户端单例
     * 使用单例模式避免频繁创建和销毁客户端，COSClient内部维护连接池，应该复用
     * @return COS客户端实例
     */
    public static COSClient getCosClient(){
        return CosClientHolder.INSTANCE;
    }

    /**
     * 手动关闭COS客户端（仅在应用关闭时调用）
     * 正常使用时不应该调用此方法
     */
    public static void shutdownCosClient() {
        if (CosClientHolder.INSTANCE != null) {
            CosClientHolder.INSTANCE.shutdown();
        }
    }
}
