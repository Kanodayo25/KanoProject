package com.kano.project.controller.controller;

import com.alibaba.excel.EasyExcel;
import com.kano.project.common.utils.DateUtils;
import com.kano.project.common.utils.EasyExcelUtils;
import com.kano.project.common.utils.ExcelUtil;
import com.kano.project.controller.controller.vo.OutPatientDepartmentExportVO;
import com.kano.project.controller.controller.vo.OutpatientDepartmentImportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "信息科用", value = "XXK")
@RestController
@RequestMapping("/XXK")
@Validated
@Slf4j
public class InformationController {

    //抽取百分比
    @Value(value = "${qualified.get.percent}")
    private double qualifiedPercent;

    //非剔除科室
    private final String NOT_KICK_FLAG = "0";

    //病例合格标记
    private final String PATIENT_TRUE_FLAG = "1";

    //总抽样份数
    private Integer totalPatientAfterPercent = 0;

    @ApiOperation("门诊课月度门诊质量信息梳理导出")
    @GetMapping("/OutpatientDepartmentImport")
    public void OutpatientDepartmentImport(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        //导入Excel获取数据
        OutpatientDepartmentImportVO importVOs = new OutpatientDepartmentImportVO();
        List<Object> importObjs = ExcelUtil.readExcel(file, importVOs);
        List<OutpatientDepartmentImportVO> importDataList = importObjs.stream()
                .map(obj -> (OutpatientDepartmentImportVO) obj)
                .collect(Collectors.toList());

        //去除带剔除标识的数据
        List<OutpatientDepartmentImportVO> filterImportDataList = importDataList.stream().filter(e -> NOT_KICK_FLAG.compareTo(e.getDepartmentKickFlag()) == 0)
                .collect(Collectors.toList());
        //将门诊数据按科室分组
        Map<String, List<OutpatientDepartmentImportVO>> groupedDataList = filterImportDataList.stream().collect(Collectors.groupingBy(e -> e.getDepartmentName()));

        //进行数据筛选处理，返回需导出数据。
        List<OutPatientDepartmentExportVO> exportVos =  outCalculateExportData(groupedDataList);

        //导出excel
        Date date = DateUtils.currDate();
        Integer year = DateUtils.getYear(date);
        Integer month = DateUtils.getMonth(date);
        String fileName = URLEncoder.encode(year +"年"+ month+"月份门（急）诊病例抽查情况统计表("+totalPatientAfterPercent+"份).xlsx","UTF-8").replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        EasyExcel.write(outputStream,OutPatientDepartmentExportVO.class)
                .sheet("sheet1")
                        .doWrite(exportVos);
    }


    //梳理数据

    /**
     *
     * @param groupedDataList Excel分组数据
     * @return
     */
    private List<OutPatientDepartmentExportVO> outCalculateExportData(Map<String, List<OutpatientDepartmentImportVO>> groupedDataList) {
        List<OutPatientDepartmentExportVO> resVOs = new ArrayList<>();
        //梳理数据
        groupedDataList.forEach((String key,List<OutpatientDepartmentImportVO> itemList)->{
            OutPatientDepartmentExportVO resVO = new OutPatientDepartmentExportVO();
            //获取单一科室总样本量
            int patientSum = itemList.size();
            //根据百分比获取抽取样本量
            BigDecimal mid = BigDecimal.valueOf(patientSum * qualifiedPercent);
            int patientSumAfterPercent = mid.setScale(0, RoundingMode.CEILING).intValue();
            //打乱列表顺序
            Collections.shuffle(itemList);
            //抽取数据
            List<OutpatientDepartmentImportVO> dataAfterPercent = itemList.subList(0, patientSumAfterPercent);
            //组装反参
            resVO = BuildResVO(dataAfterPercent,key);
            resVOs.add(resVO);
        });
        return resVOs;
    }


    //构建返参
    /**
     *
     * @param dataAfterPercent 单个科室记录数据
     * @param departmentName 科室名
     * @return 单个科室返参
     */
    private OutPatientDepartmentExportVO BuildResVO(List<OutpatientDepartmentImportVO> dataAfterPercent,String departmentName) {

        OutPatientDepartmentExportVO resVO = new OutPatientDepartmentExportVO();
        //科室名
        resVO.setDepartmentName(departmentName);
        //抽查病例份数
        resVO.setPatientTotalNum(String.valueOf(dataAfterPercent.size())+"份");
        //汇总抽样病例数，统计总数
        totalPatientAfterPercent += dataAfterPercent.size();
        //单号
        List<String> patientNumList = dataAfterPercent.stream().map(OutpatientDepartmentImportVO::getOutpatientNum).collect(Collectors.toList());
        resVO.setPatientNumStr(String.join(",",patientNumList));
        //合格份数
        long count = dataAfterPercent.stream().filter(e ->
            PATIENT_TRUE_FLAG.compareTo(e.getQualifiedCaseFlag()) == 0
        ).count();
        resVO.setTruePatientTotal(String.valueOf(count)+"份");
        //不合格份数
        resVO.setFalsePatientTotal(String.valueOf((long) dataAfterPercent.size() -count)+"份");
        return resVO;
    }
}
