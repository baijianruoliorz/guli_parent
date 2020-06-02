package com.atguigu.eduservice.entity.vo;

import lombok.Data;

/**
 * @author liqiqi_tql
 * @date 2020/5/29 -21:06
 */

@Data
public class CoursePublishVo {
    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
