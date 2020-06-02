package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liqiqi_tql
 * @date 2020/5/30 -19:17
 */

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private  EduTeacherService  teacherService;
    //查询前8条热门课程
    //前四名师
    @GetMapping("index")
    public R index(){
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduList = courseService.list(wrapper);

        //前四
       QueryWrapper<EduTeacher> wrapperTeacher=new QueryWrapper<>();

       wrapperTeacher.orderByDesc("id");

       wrapperTeacher.last("limit 4");

        List<EduTeacher> teacherList = teacherService.list(wrapperTeacher);



        return R.ok().data("eduList",eduList).data("teacherList",teacherList);
    }
}
