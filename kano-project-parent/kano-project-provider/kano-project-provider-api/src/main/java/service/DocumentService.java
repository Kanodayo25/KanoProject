package service;

import com.kano.project.common.model.Result;
import dto.DocumentChunkVO;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import dto.DocumentSearchDTO;

import java.util.List;

/**
 * 文档知识库服务（Dubbo 导出，供外部调用）
 */
public interface DocumentService {

    /**
     * 单文档入库：解析 -> 切分 -> embedding -> 存入 Milvus
     */
    Result<DocumentResultVO> uploadDocument(DocumentIngestDTO dto);

    /**
     * 批量入库
     */
    Result<List<DocumentResultVO>> uploadBatch(List<DocumentIngestDTO> dtos);

    /**
     * 按 documentId 删除某文档的全部向量分块
     */
    Result<Boolean> deleteDocument(String documentId, String collectionName);

    /**
     * 相似度检索，返回 topK 个相关分块
     */
    Result<List<DocumentChunkVO>> search(DocumentSearchDTO dto);

    /**
     * 删除整个向量集合
     */
    Result<Boolean> deleteCollection(String collectionName);
}
