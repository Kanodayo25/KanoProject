package com.kano.project.controller.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutpatientDepartmentExVO extends BaseRowModel {

    @ExcelProperty(value = "科室名称")
    @ColumnWidth(value = 10)
    private String departmentName;

    @ExcelProperty(value = "就诊日期")
    @ColumnWidth(value = 15)
    private String dateOfVisit;

    @ExcelProperty(value = "就诊结束时间")
    @ColumnWidth(value = 25)
    private String endTimeOfVisit;

    @ExcelProperty(value = "医生")
    @ColumnWidth(value = 10)
    private String doctorName;

    @ExcelProperty(value = "门诊号")
    @ColumnWidth(value = 15)
    private String outpatientNum;

    @ExcelProperty(value = "患者姓名")
    @ColumnWidth(value = 10)
    private String patientName;

    @ExcelProperty(value = "主诉低于4字")
    @ColumnWidth(value = 15)
    private String chiefComplainLow4Flag;

    @ExcelProperty(value = "现病史低于10字")
    @ColumnWidth(value = 15)
    private String currentIllnessLow10Flag;

    @ExcelProperty(value = "过敏史为空")
    @ColumnWidth(value = 15)
    private String allergiesNullFlag;

    @ExcelProperty(value = "既往史为空")
    @ColumnWidth(value = 15)
    private String pastIllnessNullFlag;

    @ExcelProperty(value = "体格检查低于8字")
    @ColumnWidth(value = 15)
    private String physicalExamLow8Flag;

    @ExcelProperty(value = "初步诊断为空")
    @ColumnWidth(value = 15)
    private String preliminaryDiagnosisNullFlag;

    @ExcelProperty(value = "治疗意见低于4字")
    @ColumnWidth(value = 15)
    private String preliminaryDiagnosisLow4Flag;

    @ExcelProperty(value = "病历合格数")
    @ColumnWidth(value = 10)
    private String qualifiedCaseFlag;

    @ExcelProperty(value = "不合格项目数")
    @ColumnWidth(value = 10)
    private String noneQualifiedCaseFlag;




}
