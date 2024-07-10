package com.kano.project.provider.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Lombok;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2022-05-29 10:35:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -34629915826014442L;
    /**
    * 主键id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userid;
    /**
    * 用户名
    */
    @TableField(value = "userName")
    private String userName;

    /**
     * 账号
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 是否删除
     */
    @TableField(value = "del")
    private Boolean del;

    /**
     * 创建时间
     */
    @TableField(value = "createdTime")
    private Date createdTime;

    /**
     * 修改时间
     */
    @TableField(value = "updatedTime")
    private Date updatedTime;


}