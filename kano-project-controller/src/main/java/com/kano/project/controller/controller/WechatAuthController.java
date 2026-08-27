package com.kano.project.controller.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kano.project.common.model.Result;
import com.kano.project.controller.controller.vo.InsertUserReqVO;
import com.kano.project.controller.controller.vo.PhoneLoginResVO;
import com.kano.project.controller.controller.vo.WxLoginReqVO;
import dto.UserReqDTO;
import dto.UserResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 小程序登录：wx.login 的 code 换 openid 签发 sa-token；getPhoneNumber 的 code 换手机号登录（新用户自动注册）
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

    @Reference
    private UserService userService;

    /** access_token 内存缓存（有效期 7200 秒，提前 60 秒过期刷新） */
    private static volatile String accessTokenCache;
    private static volatile long accessTokenExpireAt;

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

    @ApiOperation("手机号登录：getPhoneNumber code 换手机号，新用户自动注册")
    @PostMapping("/phoneLogin")
    public Result<PhoneLoginResVO> phoneLogin(@RequestBody @Valid WxLoginReqVO reqVO) {
        if (reqVO == null || !StringUtils.hasText(reqVO.getCode())) {
            return Result.fail("code 不能为空");
        }
        try {
            String phone = resolvePhoneNumber(reqVO.getCode());
            // 1. 按手机号查用户（userAccount = 手机号）
            Result<UserResDTO> exist = userService.getUserByAccount(phone);
            UserResDTO user;
            if (exist.isSuccess()) {
                user = exist.getData();
            } else {
                // 2. 新用户自动注册：直接复用新增用户逻辑
                InsertUserReqVO insert = new InsertUserReqVO();
                insert.setUserName("用户" + phone.substring(phone.length() - 4));
                insert.setUserAccount(phone);
                insert.setUserPassword(null);
                // 复用新增用户逻辑（与 UserController#insertUser 相同转换）
                UserReqDTO insertDTO = new UserReqDTO();
                BeanUtils.copyProperties(insert, insertDTO);
                Result<Boolean> created = userService.insetUser(insertDTO);
                if (!created.isSuccess()) {
                    return Result.fail(created.getMsg());
                }
                Result<UserResDTO> after = userService.getUserByAccount(phone);
                if (!after.isSuccess()) {
                    return Result.fail("注册后查询用户失败");
                }
                user = after.getData();
            }
            // 3. 签发 sa-token（登录用户信息由 provider 写入 Redis，有效期 2 小时）
            StpUtil.login(user.getUserId());
            // 4. 返回 token + 用户信息（role 供前端做管理员权限判断）
            PhoneLoginResVO resVO = new PhoneLoginResVO();
            resVO.setToken(StpUtil.getTokenValue());
            resVO.setUser(user);
            return Result.success(resVO);
        } catch (Exception e) {
            log.warn("手机号登录失败 code={}", reqVO.getCode(), e);
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

    /**
     * getPhoneNumber 的 code 换手机号；未配置 wechat.appid/secret 时进入开发模式返回固定测试号（便于本地联调）
     */
    private String resolvePhoneNumber(String code) throws Exception {
        if (!StringUtils.hasText(appId) || !StringUtils.hasText(secret)) {
            log.warn("未配置 wechat.appid/secret，进入开发模式手机号登录");
            return "dev_13900000000";
        }
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"code\":\"" + code + "\"}", StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());
        if (json.hasNonNull("errcode") && json.get("errcode").asInt() != 0) {
            throw new RuntimeException("获取手机号失败: " + json.get("errcode") + " " + json.path("errmsg").asText());
        }
        return json.path("phone_info").path("phoneNumber").asText();
    }

    /**
     * 获取微信接口调用凭据 access_token（有效期 7200 秒，内存缓存）
     */
    private String getAccessToken() throws Exception {
        if (accessTokenCache != null && System.currentTimeMillis() < accessTokenExpireAt - 60_000) {
            return accessTokenCache;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + appId + "&secret=" + secret;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());
        if (json.hasNonNull("errcode") && json.get("errcode").asInt() != 0) {
            throw new RuntimeException("获取 access_token 失败: " + json.get("errcode") + " " + json.path("errmsg").asText());
        }
        accessTokenCache = json.path("access_token").asText();
        accessTokenExpireAt = System.currentTimeMillis() + json.path("expires_in").asLong() * 1000;
        return accessTokenCache;
    }
}
