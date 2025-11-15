package com.kano.project.controller.controller;

import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import com.kano.project.common.utils.CosUtils;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.RepairInfoService;

@Api(tags = "日常问题", value = "troubleInfo")
@RestController
@RequestMapping("/troubleInfo")
@Validated
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class TroubleInfoController {

    //@Reference(url = "dubbo://175.178.101.42:20880")
    @Reference
    private RepairInfoService service;

    @ApiOperation("上传")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String resultStr = CosUtils.uploadImg(file);
        return Result.success(resultStr);
    }

    @ApiOperation("增")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody RepairInfoReqDTO reqDTO) {
        return  service.addRepairInfo(reqDTO);
    }

    @ApiOperation("删")
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam("id") Long id) {
        return service.deleteRepairInfo(id);
    }

    @ApiOperation("改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody RepairInfoReqDTO reqDTO) {
        return  service.updateRepairInfo(reqDTO);
    }

    @ApiOperation("查")
    @PostMapping("/query")
    public Result<PageResult<RepairInfoResDTO>> query(@RequestBody RepairInfoReqDTO reqDTO) {
        return service.getRepairInfoList(reqDTO);
    }
}
