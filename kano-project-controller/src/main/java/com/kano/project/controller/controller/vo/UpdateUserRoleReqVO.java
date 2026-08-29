package com.kano.project.controller.controller.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改用户角色入参
 */
@Data
public class UpdateUserRoleReqVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（前端传 String，Long 序列化后避免 JS 精度丢失，由后端负责转 Long）
     */
    @NotNull(message = "用户ID不能为空")
    private String userId;

    /**
     * 角色（0：管理员 1：用户)
     */
    @NotNull(message = "角色不能为空")
    private Boolean role;
}
