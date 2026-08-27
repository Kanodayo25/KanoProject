package com.kano.project.controller.controller.vo;

import dto.UserResDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号密码登录返回：token + 用户信息
 * 说明：小程序无 cookie 机制（Set-Cookie 不会保存/回传），且部分环境响应头读取不可靠，
 *      因此 token 直接在响应体返回；响应头 satoken 仍由 Sa-Token 自动写入，不影响浏览器端调用
 */
@Data
public class AccountLoginResVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Sa-Token 值，前端后续请求通过 Header 携带（satoken: <token>）
     */
    private String token;

    /**
     * 登录用户信息（密码已置空）
     */
    private UserResDTO user;
}
