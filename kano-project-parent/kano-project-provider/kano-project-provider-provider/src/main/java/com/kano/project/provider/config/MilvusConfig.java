package com.kano.project.provider.config;

import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Value("${milvus.address}")
    private static String URL;

    @Value("${milvus.token}")
    private static String TOKEN;

    @Value("${milvus.Db.Name}")
    private String DB_NAME;

    @Bean
    public MilvusClientV2 milvusClient() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(URL)
                .token(TOKEN)
                .dbName(DB_NAME)
                .build();
        return new MilvusClientV2(connectConfig);
    }

    @Bean
    public MilvusEmbeddingStore milvusEmbeddingStore() {
        return MilvusEmbeddingStore.builder()
                .uri(URL)
                .dimension(1024)
                .databaseName(DB_NAME)

                .build();
    }
}
