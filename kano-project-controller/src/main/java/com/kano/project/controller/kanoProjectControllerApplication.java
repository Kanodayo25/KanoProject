package com.kano.project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class kanoProjectControllerApplication {

    public static void main(String[] args) {

        SpringApplication.run(kanoProjectControllerApplication.class);

        log.info("服务启动成功");
    }
}
