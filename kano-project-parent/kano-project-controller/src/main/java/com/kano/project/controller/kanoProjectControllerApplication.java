package com.kano.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@Slf4j
@EnableSwagger2
public class kanoProjectControllerApplication {

    public static void main(String[] args) {

        SpringApplication.run(kanoProjectControllerApplication.class);

        log.info("服务启动成功");
    }
}
