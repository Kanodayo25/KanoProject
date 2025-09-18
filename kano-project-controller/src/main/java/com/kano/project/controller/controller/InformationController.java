package com.kano.project.controller.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "信息科用", value = "XXK")
@RestController
@RequestMapping("/XXK")
@Validated
@Slf4j
public class InformationController {

    @ApiOperation("门诊课月度门诊质量信息梳理导出")
    @PostMapping("/OutpatientDepartmentImport")
    public void OutpatientDepartmentImport(@RequestParam("file") MultipartFile file) {


    }
}
