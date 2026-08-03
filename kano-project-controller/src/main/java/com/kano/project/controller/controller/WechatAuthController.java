package com.kano.project.controller.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.WxLoginReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 小程序登录：wx.login 的 code 换微信 openid，再签发 sa-token
 */
@Api(tags = "小程序登录", value = "auth")
@RestController
@RequestMapping("/auth")
@Slf4j
public class WechatAuthController {

    @Value("${wechat.appid:}")
    private String appId;

    @Value("${wechat.secret:}")
    private String secret;

    @Value("${wechat.jscode2session.url:https://api.weixin.qq.com/sns/jscode2session}")
    private String jscode2sessionUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @ApiOperation("微信登录：code 换 token")
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid WxLoginReqVO reqVO) {
        if (reqVO == null || !StringUtils.hasText(reqVO.getCode())) {
            return Result.fail("code 不能为空");
        }
        try {
            String openid = resolveOpenId(reqVO.getCode());
            StpUtil.login(openid);
            return Result.success(StpUtil.getTokenValue());
        } catch (Exception e) {
            log.warn("微信登录失败 code={}", reqVO.getCode(), e);
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation("获取当前登录用户 openid（前端白名单判断用）")
    @GetMapping("/openid")
    public Result<String> openid() {
        if (!StpUtil.isLogin()) {
            return Result.fail("未登录");
        }
        return Result.success(StpUtil.getLoginId().toString());
    }

    /**
     * code 换 openid；未配置 wechat.appid/secret 时进入开发模式直接放行（便于本地联调）
     */
    private String resolveOpenId(String code) throws Exception {
        if (!StringUtils.hasText(appId) || !StringUtils.hasText(secret)) {
            log.warn("未配置 wechat.appid/secret，进入开发模式登录");
            return "dev_" + code;
        }
        String url = jscode2sessionUrl
                + "?appid=" + appId
                + "&secret=" + secret
                + "&js_code=" + code
                + "&grant_type=authorization_code";
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());
        if (json.hasNonNull("errcode") && json.get("errcode").asInt() != 0) {
            throw new RuntimeException("微信登录失败: " + json.get("errcode") + " " + json.path("errmsg").asText());
        }
        return json.path("openid").asText();
    }
}
