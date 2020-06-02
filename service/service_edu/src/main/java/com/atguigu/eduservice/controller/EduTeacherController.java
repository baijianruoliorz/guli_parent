package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author liqiqiorz
 * @since 2020-05-27
 */

@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService eduTeacherService;

    //查询讲师所有数据
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }

    //逻辑删除讲师,获得路径中的ID值:1配插件，2写方法，因为浏览器只有GET提交，所以李游览器测不了，故要swagger2!!
    @DeleteMapping("{id}")
    public R removeTeacher(@PathVariable String id) {
        boolean b = eduTeacherService.removeById(id);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //分页+条件查询:1配插件2
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,
                             @PathVariable long limit) {
        //page要选mybatis plus的那个。。。
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //框架会把所有数据封装到pageTeacher对象里面
        eduTeacherService.page(pageTeacher, null);
        //总记录数
        long total = pageTeacher.getTotal();
        //数据的list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total", total).data("rows", records);
//        Map map=new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);   这个方法也可以，因为data有map封装，也有key value封装

    }

    //多条件组合查询带分页功能！
    //第一步：吧条件值封装到对象里面
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) TeacherQuery teacherQuery) {
        //TeacherQuery 也可以加@RequestBody:使用json传数据，把json数据封装到对象里面。
        //required =false 表示该值可以不存在
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询，可能有可能没有 动态sql，用判断拼接sql语句
        //判断条件值是否为空
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //一遍column里面写数据库中的字段名
        if (!StringUtils.isEmpty(name)) {
            //构建条件
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }
        //排序,对时间做排序
        wrapper.orderByDesc("gmt_create");
        eduTeacherService.page(pageTeacher, wrapper);
        long total = pageTeacher.getTotal();
        //数据的list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }

    //添加讲师的方法
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    //修改讲师1：查询数据回显，2修改
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher byId = eduTeacherService.getById(id);
        return R.ok().data("teacher", byId);
    }

    //修改,一般来说用put提交，但是这边参数是@requsetBody，所以只能用postmapping,测试的时候id还是要的。。
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        } else {
            return R.error();
        }
    }
    //统一异常处理，比如加个i=1/0;,状态码会变成500~~~
}
