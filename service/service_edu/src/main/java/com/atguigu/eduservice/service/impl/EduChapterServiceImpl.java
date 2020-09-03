package com.atguigu.eduservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //由于在chapter里面查不了video,所以得把video注入进来进行查询
    @Autowired
    private EduVideoService videoService;

    //大纲列表，根据ID查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //根据课程ID查询所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);


        //根据课程ID查询课程里面所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);


        //创建list集合,用于最终封装数据
        List<ChapterVo>
                finalList = new ArrayList<>();
        //遍历查询章节list集合进行封装

        //遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            //把chapterVo放入集合
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<VideoVo> videoList = new ArrayList<>();


            //遍历查询小节的list结合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {

                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(m);
                //判断小节中的chapter_id是否等于章节中的ID
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {

                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    //放到小节，封装成集合
                    videoList.add(videoVo);
                }
            }
            //把封装之后的小节结合，放到章节对象上去
            chapterVo.setChildren(videoList);
        }

        //遍历查询小节list集合,进行封装


        return finalList;
    }


    public boolean deleteChapters(String chapterId){
//        查询,如果章节中有小节就不删,反之则删
//        论条件构造器的使用方式
        QueryWrapper<EduVideo> wrapper=new QueryWrapper<>();
         wrapper.eq("chapter_id",chapterId)
                 .and(i->i.eq("chapter_id",chapterId).ne("price",chapterId))
                .ge("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if(count>0){
//            这个貌似可以忽略返回值,抛出异常
            throw new GuliException(20001,"不存在小节,不可删除");

        }else {
            return true;

        }

    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        //先查询，如果章节中有小节，就不删，反之则删
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        //只想查章节下面有没有小节，而不是把小节值取到，所以list方法不太合适
        //count方法返回的是根据这个条件，能返回几条记录
        int count = videoService.count(wrapper);
        //判断
        if (count > 0) { //有数据查询
            throw new GuliException(20001, "存在小节呢~不能删除哦~");

        } else {
            //删除章节
            int result = baseMapper.deleteById(chapterId);

            //成功的话就是true啦
            return result > 0;
        }
    }

    //根据课程ID删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);

        baseMapper.delete(wrapper);

    }
}
