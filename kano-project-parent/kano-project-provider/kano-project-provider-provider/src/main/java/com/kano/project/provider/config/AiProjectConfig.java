package com.kano.project.provider.config;

import com.kano.project.common.utils.Base64Utils;
import com.kano.project.provider.repository.RedisChatMemoryStore;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class AiProjectConfig {

    public static String QwenApiKey;

    @Value("${model.api-key}")
    public String encodeKey;
    @Value("${model.name}")
    private String modelName;
    @Value("${model.base-url}")
    private String baseUrl;
    @Value("${embedding.model.name}")
    private String embeddingModelName;
    @Value("${embedding.model.dimensions}")
    private Integer dimensions;

    @Autowired
    private RedisChatMemoryStore redisChatMemoryStore;

    @PostConstruct
    public void decodeApiKey() {
        QwenApiKey = Base64Utils.decode(encodeKey);
    }

    @Bean
    public OpenAiChatModel chatModel() {
        return OpenAiChatModel.builder()
                .apiKey(QwenApiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)                    // 关键：指向通义千问
                .timeout(Duration.ofSeconds(300))
                .temperature(0.7)
                .maxTokens(2000)
                .logRequests(true)                   // 开发期打印请求日志
                .logResponses(true)                  // 开发期打印响应日志
                .build();
    }

    @Bean
    public OpenAiStreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(QwenApiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .timeout(Duration.ofSeconds(300))
                .temperature(0.7)
                .maxTokens(2000)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(QwenApiKey)
                .modelName(embeddingModelName)
                .baseUrl(baseUrl)
                .timeout(Duration.ofSeconds(30))
                .dimensions(dimensions)
                // 通义/百炼嵌入接口单次最多 20 条，langchain4j 默认 64 会超限
                .maxSegmentsPerBatch(20)
                .build();
    }

    @Bean
    public ChatMemory chatMemory() {

        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

    //构建ChatMemoryProvider对象
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        // 设置存储对象
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(redisChatMemoryStore) // 设置存储对象
                .build();
    }
}

