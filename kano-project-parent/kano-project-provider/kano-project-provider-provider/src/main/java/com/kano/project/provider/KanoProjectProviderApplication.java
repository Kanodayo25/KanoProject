package com.kano.project.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDubbo
@Slf4j
@MapperScan(basePackages = {"com.kano.project.provider.mapper"})
@EnableSwagger2
@ComponentScan(value = "com.kano")
public class KanoProjectProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanoProjectProviderApplication.class,args);
        log.info("服务启动成功");
    }
}
