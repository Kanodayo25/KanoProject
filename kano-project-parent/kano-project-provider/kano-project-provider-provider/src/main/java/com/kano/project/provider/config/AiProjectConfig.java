package com.kano.project.provider.config;

import com.kano.project.common.utils.Base64Utils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AiProjectConfig {

    public static String QwenApiKey;

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    public String encodeKey;
    @Value("${langchain4j.open-ai.chat-model.model-name}")
    private String modelName;
    @Value("${langchain4j.open-ai.chat-model.base-url}")
    private String baseUrl;

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
                .timeout(Duration.ofSeconds(30))
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
                .timeout(Duration.ofSeconds(60))
                .temperature(0.7)
                .maxTokens(2000)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .apiKey(QwenApiKey)
                .modelName("text-embedding-v3")
                .baseUrl(baseUrl)
                .timeout(Duration.ofSeconds(30))
                .build();
    }
}

