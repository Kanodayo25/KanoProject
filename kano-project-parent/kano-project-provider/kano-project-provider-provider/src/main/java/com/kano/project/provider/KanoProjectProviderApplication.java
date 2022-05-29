package com.kano.project.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableDubbo
@Slf4j
@MapperScan(basePackages = {"com.kano.project.provider.mapper"})
@ComponentScan(basePackages = {"com.kano.project.provider.*"})
public class KanoProjectProviderApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        log.info("application.yml enable");
        SpringApplication.run(KanoProjectProviderApplication.class,args);
    }
}
