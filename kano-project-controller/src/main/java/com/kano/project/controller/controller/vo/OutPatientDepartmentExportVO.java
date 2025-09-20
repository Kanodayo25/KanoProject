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
public class OutPatientDepartmentExportVO extends BaseRowModel {

    @ExcelProperty(value = "科室（门诊）")
    @ColumnWidth(value = 25)
    private String departmentName;

    @ExcelProperty(value = "抽查病例份数")
    @ColumnWidth(value = 25)
    private String patientTotalNum;

    @ExcelProperty(value = "住院病历号(门诊病历号)")
    @ColumnWidth(value = 50)
    private String patientNumStr;

    @ExcelProperty(value = "完整")
    @ColumnWidth(value = 15)
    private String truePatientTotal;

    @ExcelProperty(value = "不完整")
    @ColumnWidth(value = 15)
    private String falsePatientTotal;
}
