package com.kano.project.controller.controller;

import com.kano.project.common.model.Result;
import com.kano.project.common.utils.CosUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "日常问题", value = "troubleInfo")
@RestController
@RequestMapping("/troubleInfo")
@Validated
@Slf4j
public class TroubleInfoController {

    @ApiOperation("上传")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String resultStr = CosUtils.uploadImg(file);
        return Result.success(resultStr);
    }
}
