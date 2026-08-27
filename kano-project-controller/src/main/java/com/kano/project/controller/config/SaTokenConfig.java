package com.kano.project.controller.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Bean
    public SaInterceptor saTokenInterceptor() {
        return new SaInterceptor(handle -> StpUtil.checkLogin());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SaInterceptor saInterceptor = BeanUtils.instantiateClass(SaInterceptor.class);
        // 注册 Sa-Token 拦截器：默认所有请求都要登录
        registry.addInterceptor(saInterceptor)
                .addPathPatterns("/**")
                // 放行无需登录的接口（登录、注册、测试等）
                .excludePathPatterns(
                        "/auth/login",        // 小程序登录
                        "/loginCheck/login",  // 账号密码登录
                        "/loginCheck/test",   // 连通性测试
                        "/user/insertUser"    // 按业务需要决定是否放行
                );
    }


}