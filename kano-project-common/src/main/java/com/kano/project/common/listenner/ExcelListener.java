package com.kano.project.common.listenner;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 监听类，可以自定义
 * @Date 2018-06-05
 * @Time 16:58
 */
public class ExcelListener extends AnalysisEventListener {

    //自定义用于暂时存储data。
    //可以通过实例获取该值
    private List<Object> datas = new ArrayList<>();

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     * 如数据过大，建议使用分批处理避免内存溢出
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(object);
        //根据业务自行 do something
        doSomething();

        // 如数据过大，可以进行定量分批处理，避免内存溢出
        // 建议：对于超大文件，可以启用以下分批处理逻辑
        /*
        final int BATCH_SIZE = 1000; // 每1000条数据处理一次
        if(datas.size() >= BATCH_SIZE){
            doSomething(); // 批量处理数据
            datas.clear(); // 清空已处理的数据，释放内存
        }
         */

    }

    /**
     * 根据业务自行实现该方法
     */
    private void doSomething() {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析结束销毁不用的资源，防止内存泄漏
        // 注意：这里不清理datas，因为外部还需要通过getDatas()获取数据
        // 数据由调用方在使用完毕后负责清理
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}