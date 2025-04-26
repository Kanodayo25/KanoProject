package com.kano.project.controller.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.kano.project.common.model.Result;
import dto.UserResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import javax.validation.constraints.NotNull;

@Api(tags = "登录鉴权中心", value = "loginCheck")
@RestController
@RequestMapping("/loginCheck")
@Validated
@Slf4j
public class SaTokenController {

    @Reference
    private UserService userService;

    @ApiOperation("登陆")
    @PostMapping("/login")
    public Result<String> login(@RequestParam("userAccount") @NotNull(message = "用户账号不能为空") String userAccount,
                                @RequestParam("userPassword") @NotNull(message = "用户密码不能为空") String userPassword) {
        //账号密码校验
        try {
            UserResDTO userResDTO = UserCorrectCheck(userAccount, userPassword);
            StpUtil.login(userResDTO.getUserid());
        }
        catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.success("登陆成功");
    }

    @ApiOperation("测试")
    @PostMapping("/test")
    public Result<String> test() {

        return Result.success("1111");
    }

    private UserResDTO UserCorrectCheck(String account,String password) {
        boolean isLoginFlag = StpUtil.isLogin();
        if(!isLoginFlag){
            throw new RuntimeException("用户已登录");
        }
        Result<UserResDTO> userResDTOResult = userService.userCorrectCheckAndLogin(account, password);
        Assert.isTrue(userResDTOResult.isSuccess(),userResDTOResult.getMsg());
        return userResDTOResult.getData();
    }
}
