package com.kano.project.controller.controller.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序登录入参
 */
@Data
public class WxLoginReqVO {

    @NotBlank(message = "code 不能为空")
    private String code;
}
