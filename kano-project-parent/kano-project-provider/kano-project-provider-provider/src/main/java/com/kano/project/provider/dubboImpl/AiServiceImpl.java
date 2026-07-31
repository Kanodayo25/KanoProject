package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.Result;
import com.kano.project.common.utils.Base64Utils;
import com.kano.project.common.utils.ImageUtils;
import com.kano.project.provider.dao.AiChatDao;
import com.kano.project.provider.dao.AiDao;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.apache.zookeeper.common.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import service.AiService;


@Slf4j
@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiDao aiDao;

    @Autowired
    private AiChatDao chatService;

    @Override
    public Result<String> doChat(Long memoryId, String message,String file) {
        String answer = null;
        if(StringUtils.isEmpty(file)){
            answer = chatService.doChat(memoryId,
                    UserMessage.from(message));
        }
        else{
            answer = chatService.doChat(
                    memoryId,
                    UserMessage.from(TextContent.from(message),
                    ImageContent.from(file)));
        }
        return Result.success(answer);
    }

}