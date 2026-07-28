package com.kano.project.provider.config;

import com.kano.project.common.utils.Base64Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiProjectConfig {

    public static String QwenApiKey;

    @Value("${ai.qwen.apiKey}")
    public String encodeKey;

    @Bean
    public void decodeApiKey() {
        QwenApiKey =  Base64Utils.decode(encodeKey);
    }
}
