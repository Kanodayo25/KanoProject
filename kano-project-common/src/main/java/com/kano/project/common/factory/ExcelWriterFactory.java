package com.kano.project.common.factory;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description
 * @Date 2018-06-07
 * @Time 16:47
 */
public class ExcelWriterFactory {
    private ExcelWriter writer;
    private OutputStream outputStream;
    private int sheetNo = 1;

    public ExcelWriterFactory() {
    }

    public void setWriter(ExcelWriter writer) {
        this.writer = writer;
    }

    public <T> ExcelWriterFactory write(List<T> list, String sheetName, Class<T> clazz) {
        this.sheetNo++;
        try {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, sheetName).head(clazz).build();
            writer.write(list, writeSheet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public void finish() {
        if (writer != null) {
            writer.finish();
        }
        try {
            if (outputStream != null) {
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源，释放内存
     */
    public void close() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
