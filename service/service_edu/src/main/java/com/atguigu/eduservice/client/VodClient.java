package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author liqiqi_tql
 * @date 2020/5/30 -11:53
 */
//整合了熔断器。。fallback
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignCLient.class) //这里写在nacos里面的注册名字
@Component //交给spring管理！

public interface VodClient {
    //定义要调用的方法，直接去vodController复制
    //根据视频ID删除阿里云中的视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}") //然后把路径改成完全路径。。
    public R removeAlyVideo(@PathVariable("id") String id); //必须指定名称。。


    //定义调用删除多视频的方法
    //删除多个阿里云视频
    //参数 是list
    //删除阿里云视频的方法：删除多个视频
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);//这个注解不加也可以
}
