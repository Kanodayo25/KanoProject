package com.kano.project.controller.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutPatientDepartmentExportVO extends BaseRowModel {

    @ExcelProperty(value = "科室（门诊）")
    private String departmentName;

    @ExcelProperty(value = "抽查病例份数")
    private String patientTotalNum;

    @ExcelProperty(value = "住院病历号(门诊病历号)")
    private String patientNumStr;

    @ExcelProperty(value = "完整")
    private String truePatientTotal;

    @ExcelProperty(value = "不完整")
    private String falsePatientTotal;
}
