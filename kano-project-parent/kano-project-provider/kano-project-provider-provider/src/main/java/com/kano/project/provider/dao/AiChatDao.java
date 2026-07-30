package com.kano.project.provider.dao;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(chatMemoryProvider = "chatMemoryProvider",
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "chatModel",
        streamingChatModel = "streamingChatModel")
public interface AiChatDao {

    //@SystemMessage(fromResource = "AiProject_GWY_Prompt.txt")
    @SystemMessage("你是一个资深的公务员考试专家，回答相关问题需要足够专业，细致，如遇到不知道的，直接回答不会。")
    String doChat(@MemoryId Long memoryId, @UserMessage String message);

}
