package com.kano.project.controller.controller;

import com.kano.project.common.model.Result;
import dto.DocumentIngestDTO;
import dto.DocumentResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.DocumentService;

import java.io.IOException;

/**
 * 文档知识库接口：上传文档入库（解析→切分→向量化→写入 Milvus）
 */
@Api(tags = "文档知识库", value = "document")
@RestController
@RequestMapping("/document")
@Slf4j
public class DocumentController {

    @Reference
    private DocumentService documentService;

    @ApiOperation("上传文档入库")
    @PostMapping("/upload")
    public Result<DocumentResultVO> upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "fileName", required = false) String fileName,
                                           @RequestParam(value = "collectionName", required = false) String collectionName) {
        if (file == null || file.isEmpty()) {
            return Result.fail("文件不能为空");
        }
        String name = StringUtils.hasText(fileName) ? fileName : file.getOriginalFilename();
        DocumentIngestDTO dto = new DocumentIngestDTO();
        dto.setFileName(name);
        dto.setCollectionName(collectionName);
        try {
            dto.setFileContent(file.getBytes());
            Result<DocumentResultVO> result = documentService.uploadDocument(dto);
            if (result.isSuccess()) {
                return Result.success(result.getData());
            }
            return Result.fail(result.getMsg());
        } catch (IOException e) {
            log.warn("读取上传文件失败 fileName={}", name, e);
            return Result.fail("读取文件失败: " + e.getMessage());
        } catch (Exception e) {
            log.warn("文档上传入库失败 fileName={}", name, e);
            return Result.fail(e.getMessage());
        }
    }
}
