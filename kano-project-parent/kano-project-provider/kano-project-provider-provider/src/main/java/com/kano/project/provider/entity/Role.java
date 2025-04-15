package com.kano.project.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(type = IdType.ASSIGN_ID)
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
