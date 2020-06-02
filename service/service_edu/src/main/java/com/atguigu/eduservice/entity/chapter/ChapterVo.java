package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liqiqi_tql
 * @date 2020/5/29 -13:07
 */
@Data
public class ChapterVo {
    private String id;
    private String title;

    //章节里面有多个小节
    private List<VideoVo> children=new ArrayList<>();

}
