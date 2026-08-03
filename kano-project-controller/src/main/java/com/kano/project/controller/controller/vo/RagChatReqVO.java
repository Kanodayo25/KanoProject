package com.kano.project.controller.controller.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * RAG 知识库问答入参
 */
@Data
public class RagChatReqVO {

    /**
     * 对话记忆 id，多轮对话共用；为空后端用默认
     */
    private Long memoryId;

    @NotBlank(message = "问题不能为空")
    private String message;

    /**
     * 知识库集合名，为空走默认集合
     */
    private String collectionName;

    /**
     * 检索资料条数，为空走后端配置默认（doc.search.topk）
     */
    private Integer topK;

    /**
     * 附带图片 URL（可选，先调 POST /troubleInfo/upload 上传图片获取）
     */
    private String imageUrl;
}
