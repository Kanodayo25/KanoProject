package com.kano.project.provider.dao;

import dto.DocumentChunkVO;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import dto.DocumentSearchDTO;

import java.util.List;

/**
 * 文档知识库编排接口（内部业务编排层）
 */
public interface DocumentDao {

    /**
     * 单文档入库
     */
    DocumentResultVO upload(DocumentIngestDTO dto);

    /**
     * 批量入库
     */
    List<DocumentResultVO> uploadBatch(List<DocumentIngestDTO> dtos);

    /**
     * 按 documentId 删除文档的全部向量分块
     *
     * @return 是否删除成功
     */
    boolean delete(String documentId, String collectionName);

    /**
     * 相似度检索
     */
    List<DocumentChunkVO> search(DocumentSearchDTO dto);

    /**
     * 删除整个向量集合
     */
    boolean dropCollection(String collectionName);
}
