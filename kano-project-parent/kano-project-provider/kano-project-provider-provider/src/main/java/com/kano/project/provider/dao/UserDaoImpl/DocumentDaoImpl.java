package com.kano.project.provider.dao.UserDaoImpl;

import com.kano.project.provider.dao.DocumentDao;
import com.kano.project.provider.document.DocumentIngestor;
import com.kano.project.provider.document.DocumentMetadataKeys;
import com.kano.project.provider.document.DocumentParser;
import com.kano.project.provider.document.SupportedFileTypes;
import com.kano.project.provider.document.VectorStoreManager;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dto.DocumentChunkVO;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import dto.DocumentSearchDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文档知识库编排实现：解析 -> 切分 -> embedding -> 入库 / 检索 / 删除
 */
@Slf4j
@Service
public class DocumentDaoImpl implements DocumentDao {

    private final DocumentParser documentParser;
    private final DocumentIngestor documentIngestor;
    private final VectorStoreManager vectorStoreManager;
    private final EmbeddingModel embeddingModel;

    /**
     * 检索默认返回条数，调用方不传 topK 时使用，可配置
     */
    @Value("${doc.search.topk:5}")
    private int defaultTopK;

    public DocumentDaoImpl(DocumentParser documentParser,
                           DocumentIngestor documentIngestor,
                           VectorStoreManager vectorStoreManager,
                           EmbeddingModel embeddingModel) {
        this.documentParser = documentParser;
        this.documentIngestor = documentIngestor;
        this.vectorStoreManager = vectorStoreManager;
        this.embeddingModel = embeddingModel;
    }

    @Override
    public DocumentResultVO upload(DocumentIngestDTO dto) {
        validate(dto);

        String documentId = UUID.randomUUID().toString();
        String text = documentParser.parse(dto.getFileContent(), dto.getFileName());
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("文档内容解析为空，请检查文件格式: " + dto.getFileName());
        }

        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put(DocumentMetadataKeys.DOCUMENT_ID, documentId);
        metadata.put(DocumentMetadataKeys.FILE_NAME, dto.getFileName());
        if (dto.getMetadata() != null) {
            metadata.putAll(dto.getMetadata());
        }

        String collectionName = vectorStoreManager.resolveCollection(dto.getCollectionName());
        List<String> chunkIds = documentIngestor.ingest(text, collectionName, metadata);

        log.info("文档入库成功 documentId={}, fileName={}, collection={}, chunkCount={}",
                documentId, dto.getFileName(), collectionName, chunkIds.size());
        return new DocumentResultVO(documentId, dto.getFileName(), collectionName, chunkIds.size());
    }

    @Override
    public List<DocumentResultVO> uploadBatch(List<DocumentIngestDTO> dtos) {
        List<DocumentResultVO> results = new ArrayList<>();
        for (DocumentIngestDTO dto : dtos) {
            results.add(upload(dto));
        }
        return results;
    }

    @Override
    public boolean delete(String documentId, String collectionName) {
        if (!StringUtils.hasText(documentId)) {
            throw new IllegalArgumentException("documentId 不能为空");
        }
        String collection = vectorStoreManager.resolveCollection(collectionName);
        EmbeddingStore<TextSegment> store = vectorStoreManager.getStore(collection);
        Filter filter = MetadataFilterBuilder.metadataKey(DocumentMetadataKeys.DOCUMENT_ID).isEqualTo(documentId);
        try {
            store.removeAll(filter);
            log.info("文档删除成功 documentId={}, collection={}", documentId, collection);
            return true;
        } catch (Exception e) {
            log.warn("文档删除失败 documentId={}, collection={}", documentId, collection, e);
            return false;
        }
    }

    @Override
    public List<DocumentChunkVO> search(DocumentSearchDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getQuery())) {
            throw new IllegalArgumentException("查询内容不能为空");
        }
        String collection = vectorStoreManager.resolveCollection(dto.getCollectionName());
        EmbeddingStore<TextSegment> store = vectorStoreManager.getStore(collection);

        Embedding queryEmbedding = embeddingModel.embed(dto.getQuery()).content();
        int topK = dto.getTopK() == null ? defaultTopK : dto.getTopK();
        double minScore = dto.getMinScore() == null ? 0.0 : dto.getMinScore();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(topK)
                .minScore(minScore)
                .build();

        List<EmbeddingMatch<TextSegment>> matches = store.search(request).matches();
        List<DocumentChunkVO> results = new ArrayList<>();
        for (EmbeddingMatch<TextSegment> match : matches) {
            TextSegment segment = match.embedded();
            DocumentChunkVO vo = new DocumentChunkVO();
            vo.setDocumentId(segment.metadata().getString(DocumentMetadataKeys.DOCUMENT_ID));
            vo.setFileName(segment.metadata().getString(DocumentMetadataKeys.FILE_NAME));
            vo.setChunkIndex(segment.metadata().getInteger(DocumentMetadataKeys.CHUNK_INDEX));
            vo.setText(segment.text());
            vo.setScore(match.score());
            results.add(vo);
        }
        return results;
    }

    @Override
    public boolean dropCollection(String collectionName) {
        String collection = vectorStoreManager.resolveCollection(collectionName);
        try {
            vectorStoreManager.dropCollection(collection);
            log.info("向量集合删除成功 collection={}", collection);
            return true;
        } catch (Exception e) {
            log.warn("向量集合删除失败 collection={}", collection, e);
            return false;
        }
    }

    private void validate(DocumentIngestDTO dto) {
        if (dto == null || dto.getFileContent() == null || dto.getFileContent().length == 0) {
            throw new IllegalArgumentException("文件内容不能为空");
        }
        if (!StringUtils.hasText(dto.getFileName()) || !SupportedFileTypes.isSupported(dto.getFileName())) {
            throw new IllegalArgumentException("不支持的文件类型: " + dto.getFileName());
        }
    }
}
