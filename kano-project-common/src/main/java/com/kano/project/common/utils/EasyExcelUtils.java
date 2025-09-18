package com.kano.project.common.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.CellExtra;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class EasyExcelUtils{

    /**
     * 处理合并单元格
     *
     * @param data               解析数据
     * @param extraMergeInfoList 合并单元格信息
     * @param headRowNumber      起始行
     * @return 填充好的解析数据
     */
    public static <T> List<T> explainMergeData(List<T> data, List<CellExtra> extraMergeInfoList, Integer headRowNumber) {
        // 循环所有合并单元格信息
        extraMergeInfoList.forEach(cellExtra -> {
            //跳过标题行的合并单元格
            if (cellExtra.getFirstRowIndex() >= headRowNumber) {
                int firstRowIndex = cellExtra.getFirstRowIndex() - headRowNumber;
                int lastRowIndex = cellExtra.getLastRowIndex() - headRowNumber;
                int firstColumnIndex = cellExtra.getFirstColumnIndex();
                int lastColumnIndex = cellExtra.getLastColumnIndex();
                // 获取初始值
                Object initValue = getInitValueFromList(firstRowIndex, firstColumnIndex, data);
                // 设置值
                for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                    for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
                        setInitValueToList(initValue, i, j, data);
                    }
                }
            }

        });
        return data;
    }

    /**
     * 设置合并单元格的值
     *
     * @param filedValue  值
     * @param rowIndex    行
     * @param columnIndex 列
     * @param data        解析数据
     */
    private static <T> void setInitValueToList(Object filedValue, Integer rowIndex, Integer columnIndex, List<T> data) {
        if (rowIndex >= data.size()) return;

        T object = data.get(rowIndex);

        for (Field field : object.getClass().getDeclaredFields()) {
            // 提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == columnIndex) {
                    try {
                        field.set(object, filedValue);
                        break;
                    } catch (IllegalAccessException e) {
                        log.error("设置合并单元格的值异常：{}", e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 获取合并单元格的初始值
     * rowIndex对应list的索引
     * columnIndex对应实体内的字段
     *
     * @param firstRowIndex    起始行
     * @param firstColumnIndex 起始列
     * @param data             列数据
     * @return 初始值
     */
    private static <T> Object getInitValueFromList(Integer firstRowIndex, Integer firstColumnIndex, List<T> data) {
        Object filedValue = null;
        T object = data.get(firstRowIndex);
        for (Field field : object.getClass().getDeclaredFields()) {
            // 提升反射性能，关闭安全检查
            field.setAccessible(true);
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null) {
                if (annotation.index() == firstColumnIndex) {
                    try {
                        filedValue = field.get(object);
                        break;
                    } catch (IllegalAccessException e) {
                        log.error("设置合并单元格的初始值异常：{}", e.getMessage());
                    }
                }
            }
        }
        return filedValue;
    }

    /**
     * 格式化日期为YYYY-MM-DD
     *
     * @param sealTime
     * @return
     */
    public static String matchSealTime(String sealTime) throws ParseException {
        //提取sealtime中的日期字符串
        String patternString = "(\\d{4}([-/.])\\d{1,2}([-/.])\\d{1,2})"; // 匹配YYYY-MM-DD YYYY.MM.DD YYYY/MM/DD格式的日期
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(sealTime);

        //匹配到日期字符串
        if (matcher.find()) {
            String desiredFormat = "yyyy-MM-dd"; // 目标格式
            // 创建一个SimpleDateFormat对象来解析原始格式的日期
            SimpleDateFormat originalFormat = null;

            // 判断日期的分隔符
            String patternString1 = "(\\d{4})([-/.])(\\d{1,2})([-/.])(\\d{1,2})"; // 匹配常见的日期格式
            Pattern pattern1 = Pattern.compile(patternString1);
            Matcher matcher1 = pattern1.matcher(matcher.group(1));
            //匹配到日期分隔符
            if (matcher1.matches()) {
                String separator = matcher.group(2);
                switch (separator) {
                    case ".":
                        originalFormat = new SimpleDateFormat("yyyy.MM.dd");
                        break;
                    case "/":
                        originalFormat = new SimpleDateFormat("yyyy/MM/dd");
                        break;
                    case "-":
                        originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        break;
                }
                assert originalFormat != null;
                // 解析字符串为Date对象
                Date date = originalFormat.parse(matcher.group(1));
                // 使用目标格式创建另一个SimpleDateFormat对象来格式化日期
                SimpleDateFormat targetFormat = new SimpleDateFormat(desiredFormat);
                // 将Date对象格式化为目标字符串格式
                return targetFormat.format(date);
            }
        }
        return "";
    }
}

