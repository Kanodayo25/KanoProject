package service;

import com.kano.project.common.model.Result;

public interface AiService {
    /**
     * 对话
     * @param message 提问
     * @return 回答
     */
    Result<String> doChat(Long memoryId,String message);
}
