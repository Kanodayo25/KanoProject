package com.kano.project.provider.document.impl;

import com.kano.project.provider.document.DocumentIngestor;
import com.kano.project.provider.document.DocumentSplitter;
import com.kano.project.provider.document.VectorStoreManager;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 基于 LangChain4j 标准流程的入库实现：split -> embedAll -> store.addAll。
 * 复用项目已有的 EmbeddingModel Bean，向量模型更换零侵入。
 */
@Component
public class LangChain4jIngestor implements DocumentIngestor {

    private final DocumentSplitter documentSplitter;
    private final EmbeddingModel embeddingModel;
    private final VectorStoreManager vectorStoreManager;

    public LangChain4jIngestor(DocumentSplitter documentSplitter,
                               EmbeddingModel embeddingModel,
                               VectorStoreManager vectorStoreManager) {
        this.documentSplitter = documentSplitter;
        this.embeddingModel = embeddingModel;
        this.vectorStoreManager = vectorStoreManager;
    }

    @Override
    public List<String> ingest(String text, String collectionName, Map<String, String> metadata) {
        Metadata baseMetadata = new Metadata();
        if (metadata != null) {
            metadata.forEach(baseMetadata::put);
        }

        List<TextSegment> segments = documentSplitter.split(text, baseMetadata);
        if (segments.isEmpty()) {
            return List.of();
        }
        //批量向量化
        Response<List<Embedding>> embedResponse = embeddingModel.embedAll(segments);

        EmbeddingStore<TextSegment> store = vectorStoreManager.getStore(collectionName);
        return store.addAll(embedResponse.content(), segments);
    }
}
