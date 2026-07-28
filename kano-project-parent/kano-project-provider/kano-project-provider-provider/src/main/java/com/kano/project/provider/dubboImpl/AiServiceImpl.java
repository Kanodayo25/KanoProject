package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.AiDao;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import service.AiService;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private AiDao aiDao;

    @Override
    public Result<String> doChat(String message) {
        return Result.success(aiDao.doChat(message));
    }
}
