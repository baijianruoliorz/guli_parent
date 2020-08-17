package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    //课程相关的代码，用responsebody是不能提交课程简介的，所以要VO类封装，要加到两张表上
    //把讲师和分类做成二级联动
    @Autowired
    private EduCourseService courseService;




//default
    //课程列表 基本实现
    //TODO  完善条件查询带分页
    @GetMapping
    public R getCourseList() {
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }

    //加基本信息
    @PostMapping("addCourseInfo")
    public R addCourserINfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回id为了添加课程使用
        String id =courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("course",id);
    }

  //根据课程ID查询课程基本信息
    @GetMapping("getCourse/{courseId}")
    public  R getCourseInfo(@PathVariable String courseId){
        //这个封装了课程的所有信息
       CourseInfoVo courseInfoVo= courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }
    //修改课程信息
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();

    }
    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
       CoursePublishVo coursePublishVo= courseService.publishCourseInfo(id);
       return R.ok().data("publishCourse",coursePublishVo);
    }
    //课程最终发布

//    default
    //修改课程状态
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");//设置课程发布状态
        courseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程 todo 怎么说呢，这个功能还是好好看看呢。。136
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);

        return R.ok();
    }

}

