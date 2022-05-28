package com.kano.project.provider.dubboImpl;

import service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public void getStudent() {
        System.out.println("hello,dubbo!");
    }
}
