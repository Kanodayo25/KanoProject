package com.kano.project.provider.dao.UserDaoImpl;

import com.kano.project.provider.dao.AiChatDao;
import com.kano.project.provider.dao.DocumentDao;
import com.kano.project.provider.repository.RedisChatMemoryStore;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dto.DocumentChunkVO;
import dto.DocumentSearchDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AiChatDaoImpl implements AiChatDao {

    @Resource
    OpenAiChatModel chatModel;

    @Resource
    RedisChatMemoryStore memoryStore;

    @Resource
    DocumentDao documentDao;

    /**
     * RAG 检索默认返回条数，调用方不传 topK 时使用，可配置
     */
    @Value("${doc.search.topk:5}")
    private int defaultTopK;

    /**
     * RAG system 提示词模板，从配置的 .txt 文件读取（默认 classpath，支持 file:绝对路径）
     */
    @Value("${rag.system.prompt.path:classpath:prompts/RagSystemPrompt.txt}")
    private org.springframework.core.io.Resource ragSystemPromptResource;

    @Override
    public String doChat(Long memoryId, UserMessage message) {
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(message);
        //获取历史对话
        List<ChatMessage> historyMessage = memoryStore.getMessages(memoryId);
        //如果有历史对话，则将历史对话放入对话内容中
        if(!historyMessage.isEmpty()){
            messageList.addAll(historyMessage);
        }
        //开启对话
        Response<AiMessage> res = chatModel.generate(messageList);

        //将对话结果以及历史对话持久化
        messageList.add(res.content());
        memoryStore.updateMessages(memoryId, messageList);

        return res.content().text();
    }

    @Override
    public String doChatWithRag(Long memoryId, String message, String collectionName, Integer topK) {
        return doChatWithRag(memoryId, message, collectionName, topK, null);
    }

    @Override
    public String doChatWithRag(Long memoryId, String message, String collectionName, Integer topK, String imageUrl) {
        // 1. 向量检索相关资料
        DocumentSearchDTO searchDTO = new DocumentSearchDTO();
        searchDTO.setQuery(message);
        searchDTO.setCollectionName(collectionName);
        searchDTO.setTopK(topK == null ? defaultTopK : topK);
        List<DocumentChunkVO> chunks = documentDao.search(searchDTO);

        // 2. 用检索到的资料组装 system 提示词
        String systemPrompt = buildRagPrompt(chunks);

        // 3. 组装当前问题消息：有图片则文字 + 图片一起交给模型
        UserMessage userMessage = StringUtils.hasText(imageUrl)
                ? UserMessage.from(TextContent.from(message), ImageContent.from(imageUrl))
                : UserMessage.from(message);

        // 4. 按时间顺序组装消息：system + 历史 + 当前问题
        List<ChatMessage> history = memoryStore.getMessages(memoryId);
        List<ChatMessage> generateList = new ArrayList<>();
        generateList.add(SystemMessage.from(systemPrompt));
        generateList.addAll(history);
        generateList.add(userMessage);

        // 5. 生成回答
        Response<AiMessage> res = chatModel.generate(generateList);

        // 6. 持久化本轮 user + ai；system 提示词每轮随检索结果变化，不入历史库
        List<ChatMessage> persisted = new ArrayList<>(history);
        persisted.add(userMessage);
        persisted.add(res.content());
        memoryStore.updateMessages(memoryId, persisted);
        log.info("反参回答:{}",res.content().text());
        return res.content().text();
    }

    /**
     * 组装 RAG system 提示词：把检索到的分块作为参考资料注入，约束模型只依据资料回答
     */
    private String buildRagPrompt(List<DocumentChunkVO> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return "你是一个公务员考试专家，如果没有找到相关资料，直接线上搜索，不要编造内容。";
        }
        StringBuilder context = new StringBuilder();
        for (DocumentChunkVO chunk : chunks) {
            int index = chunk.getChunkIndex() == null ? 0 : chunk.getChunkIndex() + 1;
            context.append("【资料】").append(chunk.getFileName())
                    .append("（第").append(index).append("段，相似度 ")
                    .append(String.format("%.2f", chunk.getScore())).append("）：\n")
                    .append(chunk.getText()).append("\n\n");
        }
        return loadRagSystemPrompt() + "\n\n"
                + "==================== 检索到的资料 ====================\n"
                + context
                + "====================================================\n";
    }

    /**
     * 从配置的 .txt 文件读取 RAG system 提示词模板；读取失败时回退内置默认
     */
    private String loadRagSystemPrompt() {
        try (InputStream in = ragSystemPromptResource.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            log.warn("读取 RAG system 提示词失败: {}", ragSystemPromptResource, e);
            return "你是一个公务员考试专家。请严格依据下面的资料回答用户问题；"
                    + "如果没有找到相关资料，直接线上搜索，不要编造内容。";
        }
    }
}