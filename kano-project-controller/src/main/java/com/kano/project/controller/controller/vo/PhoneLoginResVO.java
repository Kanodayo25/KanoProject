package com.kano.project.controller.controller.vo;

import dto.UserResDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 手机号登录返回：token + 用户信息
 */
@Data
public class PhoneLoginResVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Sa-Token 值，前端后续请求通过 Header 携带（satoken: <token>）
     */
    private String token;

    /**
     * 登录用户信息（新用户为自动注册后的信息）
     */
    private UserResDTO user;
}
