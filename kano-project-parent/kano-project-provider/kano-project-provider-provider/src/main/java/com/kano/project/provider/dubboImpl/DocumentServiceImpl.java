package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.DocumentDao;
import dto.DocumentChunkVO;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import dto.DocumentSearchDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import service.DocumentService;

import java.util.List;

/**
 * 文档知识库 Dubbo 实现（薄转发，业务编排在 DocumentDao）
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentDao documentDao;

    @Override
    public Result<DocumentResultVO> uploadDocument(DocumentIngestDTO dto) {
        try {
            return Result.success(documentDao.upload(dto));
        } catch (Exception e) {
            log.warn("文档入库失败 fileName={}", dto == null ? null : dto.getFileName(), e);
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result<List<DocumentResultVO>> uploadBatch(List<DocumentIngestDTO> dtos) {
        try {
            return Result.success(documentDao.uploadBatch(dtos));
        } catch (Exception e) {
            log.warn("文档批量入库失败 size={}", dtos == null ? null : dtos.size(), e);
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteDocument(String documentId, String collectionName) {
        try {
            return Result.success(documentDao.delete(documentId, collectionName));
        } catch (Exception e) {
            log.warn("文档删除失败 documentId={}", documentId, e);
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result<List<DocumentChunkVO>> search(DocumentSearchDTO dto) {
        try {
            return Result.success(documentDao.search(dto));
        } catch (Exception e) {
            log.warn("向量检索失败 query={}", dto == null ? null : dto.getQuery(), e);
            return Result.fail(e.getMessage());
        }
    }

    @Override
    public Result<Boolean> deleteCollection(String collectionName) {
        try {
            return Result.success(documentDao.dropCollection(collectionName));
        } catch (Exception e) {
            log.warn("向量集合删除失败 collectionName={}", collectionName, e);
            return Result.fail(e.getMessage());
        }
    }
}
