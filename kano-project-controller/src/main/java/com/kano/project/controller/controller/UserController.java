package com.kano.project.controller.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.InsertUserReqVO;
import com.kano.project.controller.controller.vo.UpdateUserRoleReqVO;
import dto.UserReqDTO;
import dto.UserResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import jakarta.validation.Valid;

@Api(tags = "用户中心", value = "user")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Reference
    private UserService userService;


    @ApiOperation("新增用户（管理员）/ 注册（未登录调用视为开放注册，强制普通用户）")
    @PostMapping("/insertUser")
    public Result<Boolean> insertUser(@RequestBody @Valid InsertUserReqVO reqVO){
        // 未登录或非管理员：注册场景，强制普通用户，防止越权注册管理员；管理员新增用户按入参角色
        if (!isAdmin()) {
            reqVO.setRole(Boolean.TRUE);
        }
        UserReqDTO reqDTO = new UserReqDTO();
        BeanUtils.copyProperties(reqVO,reqDTO);
        Result<Boolean> booleanResult = userService.insetUser(reqDTO);
        if(booleanResult.isSuccess()){
            return Result.success(Boolean.TRUE);
        }
        return Result.fail(booleanResult.getMsg());
    }

    @ApiOperation("用户分页列表（管理员）")
    @GetMapping("/page")
    public Result<PageResult<UserResDTO>> page(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<Boolean> auth = checkAdmin();
        if (!auth.isSuccess()) {
            return Result.fail(auth.getMsg());
        }
        return userService.pageUsers(pageNo, pageSize);
    }

    @ApiOperation("修改用户角色（管理员）")
    @PostMapping("/updateRole")
    public Result<Boolean> updateRole(@RequestBody @Valid UpdateUserRoleReqVO reqVO) {
        Result<Boolean> auth = checkAdmin();
        if (!auth.isSuccess()) {
            return Result.fail(auth.getMsg());
        }
        Long userId = parseUserId(reqVO.getUserId());
        if (userId == null) {
            return Result.fail("用户ID格式错误");
        }
        return userService.updateUserRole(userId, reqVO.getRole());
    }

    @ApiOperation("删除用户（管理员，软删并踢下线）")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam("userId") String userId) {
        Result<Boolean> auth = checkAdmin();
        if (!auth.isSuccess()) {
            return Result.fail(auth.getMsg());
        }
        Long id = parseUserId(userId);
        if (id == null) {
            return Result.fail("用户ID格式错误");
        }
        // 1. 软删
        userService.deleteUser(id);
        // 2. 清除 Redis 登录信息并踢下线
        userService.logout(id);
        StpUtil.kickout(id);
        return Result.success(Boolean.TRUE);
    }

    /**
     * 前端传 userId 为 String（Long 序列化输出避免 JS 精度丢失），后端负责转 Long
     */
    private Long parseUserId(String userId) {
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 是否已登录管理员（未登录返回 false；供 insertUser 区分开放注册/管理员新增两种场景）
     */
    private boolean isAdmin() {
        if (!StpUtil.isLogin()) {
            return false;
        }
        Result<UserResDTO> info = userService.getLoginUserInfo(StpUtil.getLoginIdAsLong());
        return info.isSuccess() && Boolean.FALSE.equals(info.getData().getRole());
    }

    /**
     * 管理员权限校验：当前登录用户角色必须为 0（管理员）
     */
    private Result<Boolean> checkAdmin() {
        StpUtil.checkLogin();
        Result<UserResDTO> info = userService.getLoginUserInfo(StpUtil.getLoginIdAsLong());
        if (!info.isSuccess()) {
            return Result.fail("未登录或登录已过期");
        }
        if (!Boolean.FALSE.equals(info.getData().getRole())) {
            return Result.fail("无权限，仅管理员可操作");
        }
        return Result.success(Boolean.TRUE);
    }
}
