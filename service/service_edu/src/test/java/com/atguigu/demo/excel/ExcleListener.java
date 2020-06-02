package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @author liqiqi_tql
 * @date 2020/5/28 -19:09
 */
public class ExcleListener extends AnalysisEventListener<DemoData> {
    //一行一行读取excel中的内容，表头不会读取
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("++++++++"+demoData);
    }
    //读取表头
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("白头"+headMap);
    }

    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
