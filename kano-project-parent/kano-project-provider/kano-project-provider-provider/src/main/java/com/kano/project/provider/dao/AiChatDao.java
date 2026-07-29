package com.kano.project.provider.dao;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AiChatDao {

    @SystemMessage(fromResource = "AiProject_GWY_Prompt.txt")
    String doChat(String message);

}
