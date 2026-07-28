package com.kano.project.provider.dao.UserDaoImpl;

import com.kano.project.provider.dao.AiDao;
import org.springframework.stereotype.Service;

import static com.kano.project.provider.config.AiProjectConfig.QwenApiKey;

@Service
public class AiDaoImpl implements AiDao {

    @Override
    public String doChat(String message) {
        return null;
    }
}
