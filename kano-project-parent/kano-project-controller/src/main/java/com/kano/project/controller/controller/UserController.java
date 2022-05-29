package com.kano.project.controller.controller;

import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.InsertUserReqVO;
import dto.UserReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import javax.validation.Valid;

@Api(tags = "用户中心" ,value = "user")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Reference
    private UserService userService;

    @ApiOperation("新增用户")
    @PostMapping("/insertUser")
    public Result<Boolean> insertUser(@RequestBody @Valid InsertUserReqVO reqVO){

        UserReqDTO reqDTO = new UserReqDTO();
        BeanUtils.copyProperties(reqVO,reqDTO);
        Result<Boolean> booleanResult = userService.insetUser(reqDTO);
        if(booleanResult.isSuccess()){
            return Result.success(Boolean.TRUE);
        }
        return Result.fail(booleanResult.getMsg());
    }
}
