package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

        //1 根据课程id删除小节
        // TODO 删除小节，删除对应视频文件
        @Override
        public void removeVideoByCourseId(String courseId) {
            //把课程所有id查出来
            //查小节表
            QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();

            wrapperVideo.eq("course_id",courseId);
            //查询指定的列用selcet
            wrapperVideo.select("video_source_id");

            List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

            //这里的集合不是普通的集合，而是在impl里面实现的固定格式的集合 list<EduVideo>变成list<string>

            List<String> videoIds=new ArrayList<>();
            for (int i = 0; i < eduVideoList.size(); i++) {
                EduVideo eduVideo = eduVideoList.get(i);
                String videoSourceId = eduVideo.getVideoSourceId();
                if(StringUtils.isEmpty(videoSourceId)) {
                    //放到集合里面
                    videoIds.add(videoSourceId);
                }
            }
            //根据多个视频id删除视频
            //判断list不为空的方法size>0
            if(videoIds.size()>0) {
                vodClient.deleteBatch(videoIds);
            }

            QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
            wrapper.eq("course_id",courseId);
            baseMapper.delete(wrapper);
    }
}
