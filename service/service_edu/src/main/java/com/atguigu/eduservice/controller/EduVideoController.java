package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {
    //把feigin 需要的interface注入到controller里面来
    @Autowired
    private VodClient vodClient;

    @Autowired
    private EduVideoService videoService;
    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }
    //删除小节  TODO 这个方法需要删除：同时删除视频--已经实现啦
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id){
        //根据小节ID获取视频ID，再根据视频ID调用方法实现删除
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //根据视频ID远程调用实现视频删除

        //判断小节是否有id
        if(!StringUtils.isEmpty(videoSourceId)){
            R result = vodClient.removeAlyVideo(videoSourceId);//视频ID
            if(result.getCode()==20001){
                throw new GuliException(20001,"删除视频失败，熔断器。。。");
            }
        }
        //删除小节
       videoService.removeById(id);
        return R.ok();
    }

    //修改小节 todo


}

