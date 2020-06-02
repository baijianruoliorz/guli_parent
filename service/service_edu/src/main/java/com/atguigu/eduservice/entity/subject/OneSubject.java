package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liqiqi_tql
 * @date 2020/5/28 -21:25
 */
//一级分类
    @Data
public class OneSubject {

    private String id;
    private String title;

    //有多个二级分类，所以用list
    private List<TwoSubject> children=new ArrayList<>();

}
