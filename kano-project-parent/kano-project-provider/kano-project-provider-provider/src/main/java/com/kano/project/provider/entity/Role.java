package com.kano.project.provider.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    /**
     * 菜单权限id
     */
    private Long roleId;

    /**
     * 父菜单权限id
     */
    private Long roleParentId;

    /**
     * 菜单权限名（英文id）
     */
    private String roleName;

    /**
     * 菜单权限描述（中文描述）
     */
    private String roleDesc;
}
