package com.kano.project.controller.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutpatientDepartmentImportVO extends BaseRowModel {

    @ExcelProperty(value = "科室名称")
    private String departmentName;

    @ExcelProperty(value = "就诊日期")
    private String dateOfVisit;

    @ExcelProperty(value = "就诊结束时间")
    private String endTimeOfVisit;

    @ExcelProperty(value = "医生")
    private String doctorName;

    @ExcelProperty(value = "门诊号")
    private String outpatientNum;

    @ExcelProperty(value = "患者姓名")
    private String patientName;

    @ExcelProperty(value = "病历合格标记")
    private String qualifiedCaseFlag;

    @ExcelProperty(value = "科室剔除标记")
    private String departmentKickFlag;




}
