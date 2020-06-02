package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author liqiqi_tql
 * @date 2020/5/28 -18:58
 */
public class TestEasyExcel {
    public static void main(String[] args) {
//        //实现写的操作
//        //设置写入的文件夹地址和名称
//        String fileName="D:\\write.xlsx";
//
//        //调用方法，实现操作
//        //第一个参数路径
//        //第二个参数：实体类
//        EasyExcel.write(fileName,DemoData.class).sheet("学生列表").doWrite(getData());
 /*上面是写操作*/

        //实现读操作
        String fileName="D:\\write.xlsx";
        //不用记。。。
        EasyExcel.read(fileName,DemoData.class,new ExcleListener()).sheet().doRead();


    }
    //创建一个方法，返回list
    private static List<DemoData>  getData(){
        List<DemoData> list=new ArrayList<>();

        for (int i=0;i<10;i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setName("liqiqiorz" + i);
            list.add(data);
        }
            return list;
    }
}
