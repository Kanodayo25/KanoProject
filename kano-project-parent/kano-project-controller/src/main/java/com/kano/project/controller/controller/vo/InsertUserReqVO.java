package com.kano.project.controller.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertUserReqVO implements Serializable {

    private static final long serialVersionUID = -2657699193539029150L;


    /**
     * 用户名
     */
    private String username;
}
