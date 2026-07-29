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
public class ExcelListener<T> extends AnalysisEventListener<T> {

    private List<Object> datas = new ArrayList<>();

    @Override
    public void invoke(T object, AnalysisContext context) {
        datas.add(object);
        doSomething();
    }

    /**
     * 根据业务自行实现该方法
     */
    private void doSomething() {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
