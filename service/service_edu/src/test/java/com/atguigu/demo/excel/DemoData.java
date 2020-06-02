package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author liqiqi_tql
 * @date 2020/5/28 -18:56
 */
//index是读操作才要的
@Data
public class DemoData {
    //设置exce;表头名称：例如一级二级分类
    @ExcelProperty(value = "学生编号",index = 0)
    private Integer sno;
    @ExcelProperty(value = "学生姓名",index = 1)
    private String name;
}
