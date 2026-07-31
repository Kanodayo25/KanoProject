package com.kano.project.provider.dao.UserDaoImpl;

import com.kano.project.provider.dao.AiChatDao;
import com.kano.project.provider.repository.RedisChatMemoryStore;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiChatDaoImpl implements AiChatDao {

    @Resource
    OpenAiChatModel chatModel;

    @Resource
    RedisChatMemoryStore memoryStore;

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
}