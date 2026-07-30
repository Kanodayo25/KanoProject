package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.AiChatDao;
import com.kano.project.provider.dao.AiDao;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import service.AiService;

@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiDao aiDao;

    @Resource
    private AiChatDao chatService;

    @Override
    public Result<String> doChat(Long memoryId, String message) {
        String answer = chatService.doChat(memoryId, message);
        return Result.success(answer);
    }
}