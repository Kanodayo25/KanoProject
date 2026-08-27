package com.kano.project.controller.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.AccountLoginReqVO;
import com.kano.project.controller.controller.vo.AccountLoginResVO;
import dto.UserResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
    public Result<AccountLoginResVO> login(@RequestBody @Valid AccountLoginReqVO reqVO) {
        // 1. Dubbo 调用 provider 校验账号密码（返回用户信息）
        Result<UserResDTO> check = userService.userCorrectCheckAndLogin(reqVO.getUserAccount(), reqVO.getUserPassword());
        if (!check.isSuccess()) {
            return Result.fail(check.getMsg());   // 账号或密码错误
        }
        UserResDTO userResDTO = check.getData();
        // 不向客户端回传密码
        userResDTO.setUserPassword(null);
        // 2. 校验通过，以用户 ID 作为登录账号签发 Token（登录用户信息已由 provider 写入 Redis，有效期 2 小时）
        StpUtil.login(userResDTO.getUserId());
        // 3. 会话管理：将登录用户写入当前会话（SaSession），供本请求上下文后续读取
        StpUtil.getSession().set("loginUser", userResDTO);
        // 4. 返回 token + 用户实体。token 直接放响应体（小程序无 cookie、响应头读取不可靠，
        //    正文返回最稳妥；响应头 satoken 仍由 Sa-Token 自动写入，浏览器端调用不受影响）
        AccountLoginResVO resVO = new AccountLoginResVO();
        resVO.setToken(StpUtil.getTokenValue());
        resVO.setUser(userResDTO);
        return Result.success(resVO);
    }

    @ApiOperation("注销（退出登录）")
    @PostMapping("/logout")
    public Result<String> logout() {
        StpUtil.checkLogin();
        // 1. 清除 Redis 登录用户信息并复位数据库登录状态
        userService.logout(StpUtil.getLoginIdAsLong());
        // 2. 使当前 Token 立即失效
        StpUtil.logout();
        return Result.success("注销成功");
    }

    @ApiOperation("踢下线（管理员操作）")
    @PostMapping("/kickout")
    public Result<String> kickout(@RequestParam("userId") @NotNull(message = "用户ID不能为空") Long userId) {
        // 1. 清除 Redis 登录用户信息并复位数据库登录状态
        userService.logout(userId);
        // 2. 踢下线：该用户所有 Token 立即失效，被踢用户下次请求抛 NotLoginException(KICK_OUT)
        StpUtil.kickout(userId);
        return Result.success("踢下线成功");
    }

    @ApiOperation("获取当前登录用户信息（Redis 缓存，有效期 2 小时）")
    @GetMapping("/getUserInfo")
    public Result<UserResDTO> getUserInfo() {
        StpUtil.checkLogin();
        return userService.getLoginUserInfo(StpUtil.getLoginIdAsLong());
    }

    @ApiOperation("测试")
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("1111");
    }
}
