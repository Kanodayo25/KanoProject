package com.kano.project.common.factory;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

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
public class ExcelWriterFactory extends ExcelWriter {
    private OutputStream outputStream;
    private int sheetNo = 1;

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactory write(List<? extends BaseRowModel> list, String sheetName,
                                    BaseRowModel object) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, object.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception ex) {
            ex.printStackTrace();
            // 异常时不需要flush，在finish中统一处理
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            if (outputStream != null) {
                outputStream.flush();
                // 注意：不要在这里关闭outputStream，因为它是由外部传入的
                // 由调用方负责关闭，通常是ServletOutputStream会自动关闭
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源，释放内存
     * 注意：仅在非ServletOutputStream的情况下调用
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
