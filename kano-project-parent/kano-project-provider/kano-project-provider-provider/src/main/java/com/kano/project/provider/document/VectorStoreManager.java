package com.kano.project.provider.document;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;

/**
 * 向量库存储管理接口。
 * 负责按集合名获取/缓存 EmbeddingStore，支持多知识库隔离与集合生命周期管理。
 */
public interface VectorStoreManager {

    /**
     * 获取指定集合的存储实例（集合不存在时首次写入自动创建）
     */
    EmbeddingStore<TextSegment> getStore(String collectionName);

    /**
     * 删除整个向量集合
     */
    void dropCollection(String collectionName);

    /**
     * 解析实际集合名：为空时返回默认集合
     */
    String resolveCollection(String collectionName);
}
