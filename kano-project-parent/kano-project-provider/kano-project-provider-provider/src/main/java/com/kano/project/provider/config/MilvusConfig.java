package com.kano.project.provider.config;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Value("${milvus.address}")
    private String url;

    @Value("${milvus.token}")
    private String token;

    @Value("${milvus.Db.Name}")
    private String dbName;

    @Value("${embedding.model.dimensions}")
    private Integer dimension;

    /**
     * Milvus v2 原生客户端，供集合管理/运维等场景直接使用。
     * 文档向量化的 EmbeddingStore 由 document.MilvusVectorStoreManager 按集合懒加载创建。
     */
    @Bean
    public MilvusClientV2 milvusClient() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(url)
                .token(token)
                .dbName(dbName)
                .build();
        return new MilvusClientV2(connectConfig);
    }
}
