package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.AiDao;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.kano.project.provider.dao.AiChatDao;
import service.AiService;

import jakarta.annotation.Resource;

@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiDao aiDao;

    @Resource
    private OpenAiChatModel openAiChatModel;


    @Override
    public Result<String> doChat(String message) {
        AiChatDao chatService = AiServices.create(AiChatDao.class, openAiChatModel);
        String answer = chatService.doChat(message);
        log.info("answer: " + answer);
        return Result.success(answer);
    }
}
