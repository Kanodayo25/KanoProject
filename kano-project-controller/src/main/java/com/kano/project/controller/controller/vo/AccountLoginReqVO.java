package com.kano.project.controller.controller.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号密码登录入参
 */
@Data
public class AccountLoginReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户账号不能为空")
    private String userAccount;

    @NotBlank(message = "用户密码不能为空")
    private String userPassword;
}
