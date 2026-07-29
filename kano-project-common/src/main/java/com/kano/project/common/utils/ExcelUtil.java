package com.kano.project.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.kano.project.common.Exception.ExcelException;
import com.kano.project.common.factory.ExcelWriterFactory;
import com.kano.project.common.listenner.ExcelListener;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 工具类
 * @Date 2018-06-06
 * @Time 14:07
 */
public class ExcelUtil {
    /**
     * 读取 Excel(多个 sheet)
     *
     * @param excel    文件
     * @param clazz    实体类映射
     * @return Excel 数据 list
     */
    public static <T> List<Object> readExcel(MultipartFile excel, Class<T> clazz) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        try (InputStream inputStream = new BufferedInputStream(excel.getInputStream())) {
            EasyExcel.read(inputStream, clazz, excelListener).doReadAll();
            return excelListener.getDatas();
        } catch (IOException e) {
            throw new ExcelException("读取Excel失败！");
        }
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel    文件
     * @param clazz    实体类映射
     * @param sheetNo  sheet 的序号 从1开始
     * @return Excel 数据 list
     */
    public static <T> List<Object> readExcel(MultipartFile excel, Class<T> clazz, int sheetNo) {
        return readExcel(excel, clazz, sheetNo, 1);
    }

    /**
     * 读取某个 sheet 的 Excel
     *
     * @param excel       文件
     * @param clazz       实体类映射
     * @param sheetNo     sheet 的序号 从1开始
     * @param headLineNum 表头行数，默认为1
     * @return Excel 数据 list
     */
    public static <T> List<Object> readExcel(MultipartFile excel, Class<T> clazz, int sheetNo,
                                              int headLineNum) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        try (InputStream inputStream = new BufferedInputStream(excel.getInputStream())) {
            EasyExcel.read(inputStream, clazz, excelListener)
                    .sheet(sheetNo - 1)  // EasyExcel 3.x sheet 从0开始
                    .headRowNumber(headLineNum)
                    .doRead();
            return excelListener.getDatas();
        } catch (IOException e) {
            throw new ExcelException("读取Excel失败！");
        }
    }

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz     映射实体类，Excel 模型
     */
    public static <T> void writeExcel(HttpServletResponse response, List<T> list,
                                      String fileName, String sheetName, Class<T> clazz) {
        try {
            OutputStream outputStream = getOutputStream(fileName, response);
            EasyExcel.write(outputStream, clazz)
                    .sheet(sheetName)
                    .doWrite(list);
        } catch (Exception e) {
            throw new ExcelException("导出Excel失败！");
        }
    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz     映射实体类，Excel 模型
     */
    public static <T> ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response, List<T> list,
                                                              String fileName, String sheetName, Class<T> clazz) {
        try {
            OutputStream outputStream = getOutputStream(fileName, response);
            ExcelWriter writer = EasyExcel.write(outputStream, clazz).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            writer.write(list, writeSheet);
            ExcelWriterFactory factory = new ExcelWriterFactory();
            factory.setWriter(writer);
            return factory;
        } catch (Exception e) {
            throw new ExcelException("导出Excel失败！");
        }
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) {
        String filePath = fileName + ".xlsx";
        File dbfFile = new File(filePath);
        try {
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();
            }
            fileName = new String(filePath.getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException e) {
            throw new ExcelException("创建文件失败！");
        }
    }
}
