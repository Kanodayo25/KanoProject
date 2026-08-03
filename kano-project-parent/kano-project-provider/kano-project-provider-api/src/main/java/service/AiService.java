package service;

import com.kano.project.common.model.Result;

public interface AiService {
    /**
     * 对话
     * @param message 提问
     * @return 回答
     */
    Result<String> doChat(Long memoryId,String message,String file);

    /**
     * 知识库问答（RAG）：先向量检索资料，再结合对话历史生成回答
     *
     * @param memoryId       对话记忆 id
     * @param message        问题
     * @param collectionName Milvus 集合名，为空走默认知识库
     * @param topK           检索资料条数，为空默认 5
     * @return 基于检索资料的回答
     */
    Result<String> doChatWithRag(Long memoryId, String message, String collectionName, Integer topK);

    /**
     * 知识库问答（RAG），可附带图片：检索资料后，把问题文字与图片一起交给模型理解回答
     *
     * @param imageUrl 图片可访问 URL（可选，先经图片上传接口获取）
     */
    Result<String> doChatWithRag(Long memoryId, String message, String collectionName, Integer topK, String imageUrl);
}
