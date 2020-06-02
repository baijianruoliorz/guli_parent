//package com.atguigu.eduservice.service.impl;
//
//import com.atguigu.eduservice.entity.EduCourse;
//import com.atguigu.eduservice.entity.EduCourseDescription;
//import com.atguigu.eduservice.entity.vo.CourseInfoVo;
//import com.atguigu.eduservice.mapper.EduCourseDescriptionMapper;
//import com.atguigu.eduservice.mapper.EduCourseMapper;
//import com.atguigu.eduservice.service.EduCourseDescriptionService;
//import com.atguigu.eduservice.service.EduCourseService;
//import com.atguigu.servicebase.exceptionhandler.GuliException;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * <p>
// * 课程 服务实现类
// * </p>
// *
// * @author testjava
// * @since 2020-05-29
// */
//@Service
//public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
//
//    //注入简介的service
//    @Autowired
//    private EduCourseDescriptionService courseDescriptionService;
//
//
//
//
//   //添加课程
//    @Override
//    public void saveCourseInfo(CourseInfoVo courseInfoVo) {
//        //想简介和课程表都加信息
//        //所以是两张表加数据
//        //由于这里是课程的service 所以课程表可以直接被加
//        //要转换
//        EduCourse eduCourse=new EduCourse();
//        BeanUtils.copyProperties(courseInfoVo,eduCourse);
//        //mapper的对象只能是eduCourse,返回值就是这次操作的yingxiang行数，大于等于1成功
//        int insert = baseMapper.insert(eduCourse);
//        if(insert<=0){
//            throw new GuliException(20001,"添加课程失败");
//        }
//        //2想课程简介表添加课程简介
//        //edu_course_description
//        //用谁就把谁住进来就行了autowired
//        EduCourseDescription courseDescription=new EduCourseDescription();
//        //这里放入简介就行了
//        courseDescription.setDescription(courseInfoVo.getDescription());
//        //记数据就行
//        courseDescriptionService.save(courseDescription);
//    }
//}

package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */

@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节service
    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService chapterService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert == 0) {
            //添加失败
            throw new GuliException(20001,"添加课程信息失败");
        }

        //获取添加之后课程id，保存在decription的课程ID里面
        String cid = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id，这样就是一对一了
        courseDescription.setId(cid);
        courseDescriptionService.save(courseDescription);

        return cid;
    }
  //根据课程ID查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        //包含了两张表数据

        //课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //封装
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
       //查询描述表
        //前面已经注入了相关service
        EduCourseDescription courseDecription = courseDescriptionService.getById(courseId);

        //封装
        courseInfoVo.setDescription(courseDecription.getDescription());

        return courseInfoVo;

    }
//修改方法，两张表
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {


        //修改课程表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        //影响行数
        int update = baseMapper.updateById(eduCourse);
        if(update==0){
            throw new GuliException(20001,"修改课程信息失败");
        }
        //修改描述表
        EduCourseDescription description=new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);

    }
   //实现类可以调用mapper,自己写的方法就不能用thisbasemapper了
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);

        return publishCourseInfo;
    }

    @Override
    public void removeCourse(String courseId) {
        //1 根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2 根据课程id删除章节
        chapterService.removeChapterByCourseId(courseId);

        //3 根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4 根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        //失败返回
        if (result == 0) {
            throw new GuliException(20001, "删除失败");
        }
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
         //2根据讲师id查询所讲课程
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        //判断条件值是否为空
        //判断一级分类是否存在
        if(StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
         wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        //判断二级分类是否存在
        if(StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        //根据关注度
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            wrapper.orderByDesc("buy_count");
        }
        //最新
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }
        //价格
        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam,wrapper);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        //下一页
        boolean hasNext = pageParam.hasNext();
        //上一页
        boolean hasPrevious = pageParam.hasPrevious();

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    //根据课程id编写sql语句查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
