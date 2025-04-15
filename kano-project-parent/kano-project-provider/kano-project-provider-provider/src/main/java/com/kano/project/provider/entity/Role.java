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
     * 用户id
     */
    private Long userId;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 菜单权限集合（英文标识）
     */
    private String roleList;

    /**
     * 菜单权限集合（中文描述）
     */
    private String roleListDesc;
}
