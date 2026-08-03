package com.kano.project.provider.document.impl;

import com.kano.project.provider.document.VectorStoreManager;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.param.MetricType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Milvus 存储管理实现。
 * 按集合名懒加载并缓存 EmbeddingStore，一套代码支撑 N 个知识库；集合首次写入时由 MilvusEmbeddingStore 自动建集合。
 */
@Component
public class MilvusVectorStoreManager implements VectorStoreManager {

    private final String uri;
    private final String token;
    private final String databaseName;
    private final Integer dimension;
    private final String defaultCollection;

    private final Map<String, EmbeddingStore<TextSegment>> storeCache = new ConcurrentHashMap<>();

    public MilvusVectorStoreManager(
            @Value("${milvus.address}") String uri,
            @Value("${milvus.token}") String token,
            @Value("${milvus.Db.Name}") String databaseName,
            @Value("${embedding.model.dimensions}") Integer dimension,
            @Value("${milvus.collection.default:knowledge_base}") String defaultCollection) {
        this.uri = uri;
        this.token = token;
        this.databaseName = databaseName;
        this.dimension = dimension;
        this.defaultCollection = defaultCollection;
    }

    @Override
    public EmbeddingStore<TextSegment> getStore(String collectionName) {
        String collection = resolveCollection(collectionName);
        return storeCache.computeIfAbsent(collection, this::createStore);
    }

    @Override
    public void dropCollection(String collectionName) {
        String collection = resolveCollection(collectionName);
        storeCache.remove(collection);
        MilvusEmbeddingStore store = (MilvusEmbeddingStore) createStore(collection);
        store.dropCollection(collection);
    }

    @Override
    public String resolveCollection(String collectionName) {
        return (collectionName == null || collectionName.isBlank()) ? defaultCollection : collectionName;
    }

    private MilvusEmbeddingStore createStore(String collectionName) {
        return MilvusEmbeddingStore.builder()
                .uri(uri)
                .token(token)
                .databaseName(databaseName)
                .collectionName(collectionName)
                .dimension(dimension)
                .metricType(MetricType.COSINE)
                .build();
    }
}
