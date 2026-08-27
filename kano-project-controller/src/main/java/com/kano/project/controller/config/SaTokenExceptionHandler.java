package com.kano.project.controller.config;

import cn.dev33.satoken.exception.NotLoginException;
import com.kano.project.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SaTokenExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public Result<String> handleNotLogin(NotLoginException e) {
        // 记录异常现场：账号体系、细分类型、出问题的 token
        log.warn("登录校验失败: loginType={}, type={}, msg={}",
                e.getLoginType(), e.getType(), e.getMessage());

        // 7 种细分类型（e.getType()）：-1 未携带token / -2 token无效 / -3 已过期
        // / -4 被顶下线 / -5 被踢下线 / -6 token被冻结 / -7 未按指定前缀提交
        return switch (e.getType()) {
            case NotLoginException.NOT_TOKEN ->       // -1 请求未携带 Token
                    Result.fail(401, "未检测到登录凭证，请先登录");
            case NotLoginException.INVALID_TOKEN ->   // -2 错误的/伪造的 Token
                    Result.fail(401, "登录凭证无效，请重新登录");
            case NotLoginException.TOKEN_TIMEOUT ->   // -3 Token 已过期（默认 30 天）
                    Result.fail(401, "登录已过期，请重新登录");
            case NotLoginException.BE_REPLACED ->     // -4 被顶下线（is-concurrent=false 时别处登录）
                    Result.fail(401, "账号在其他设备登录，您已被顶下线");
            case NotLoginException.KICK_OUT ->        // -5 被管理员 StpUtil.kickout() 踢下线
                    Result.fail(401, "账号已被强制下线，如有疑问请联系管理员");
            case NotLoginException.TOKEN_FREEZE ->    // -6 Token 被冻结（配合封禁功能）
                    Result.fail(403, "账号已被冻结，请联系管理员");
            case NotLoginException.NO_PREFIX ->       // -7 未按配置的 token-prefix 前缀提交
                    Result.fail(400, "登录凭证格式错误");
            default -> Result.fail(401, "未登录或登录已过期，请重新登录");
        };
    }
}