package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //嗯，这个方法一开始会执行全局异常的。。。
    /*
    * org.apache.ibatis.binding.BindingException:  数据绑定异常，就是没有指定mapper.xml配置
    *  Invalid bound statement (not found):   解决：复制xml到target(不推荐
    * 解决2：把xml放到resource（不推荐
    * 解决3：配置  pom.xml  application.properties
    * com.atguigu.eduservice.mapper.EduCourseMapper.getPublishCourseInfo*/
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id查询课程信息，这里alt+enter就可以直接在xml创建方法啦
    CourseWebVo getBaseCourseInfo(String courseId);

}
