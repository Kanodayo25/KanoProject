package com.kano.project.provider.dao.UserDaoImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kano.project.provider.dao.UserDao;
import com.kano.project.provider.entity.User;
import com.kano.project.provider.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDaoImpl extends ServiceImpl<UserMapper,User> implements UserDao {
}
