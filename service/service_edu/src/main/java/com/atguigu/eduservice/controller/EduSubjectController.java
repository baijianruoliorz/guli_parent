package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author liqiqiorz
 * @since 2020-05-28
 */
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类
    //获取上传的文件，把文件内容读取出来
    @PostMapping("addSubject")
    public R addsubject(MultipartFile file){
         eduSubjectService.saveSubject(file,eduSubjectService);
        return R.ok();
    }
    //课程分类的列表功能（树形）
    //如何实现符合要去的数据？----返回数组
    //第一种方式：拼字串
    //第二种方式：封装
    /* 针对返回数据创建实体类
     创建一级分类和二级分类
     第二步：一个一级分类有多个二级分类，在实体类表示出来
    *
    * */
    @GetMapping("getAllSubject")
    public R getAllShubject(){
        //查询所有一级二级分类返回list，集合中的泛型是。。
        //因为一级分类里面可以表示二级，所以返回一级分类就行了
       List<OneSubject> list =eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }
}

