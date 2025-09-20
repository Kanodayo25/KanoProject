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

    @ExcelProperty(value = "主诉低于4字")
    private String chiefComplainLow4Flag;

    @ExcelProperty(value = "现病史低于10字")
    private String currentIllnessLow10Flag;

    @ExcelProperty(value = "过敏史为空")
    private String allergiesNullFlag;

    @ExcelProperty(value = "既往史为空")
    private String pastIllnessNullFlag;

    @ExcelProperty(value = "体格检查低于8字")
    private String physicalExamLow8Flag;

    @ExcelProperty(value = "初步诊断为空")
    private String preliminaryDiagnosisNullFlag;

    @ExcelProperty(value = "治疗意见低于4字")
    private String preliminaryDiagnosisLow4Flag;

    @ExcelProperty(value = "病历合格数")
    private String qualifiedCaseFlag;

    @ExcelProperty(value = "不合格项目数")
    private String noneQualifiedCaseFlag;

    @ExcelProperty(value = "质控科室抽查标识")
    private String departmentKickFlag;




}
