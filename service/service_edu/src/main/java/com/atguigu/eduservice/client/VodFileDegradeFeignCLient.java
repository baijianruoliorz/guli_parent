package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liqiqi_tql
 * @date 2020/5/30 -16:12
 */
//如果要做熔断机制，必须要实现类
    @Component
public class VodFileDegradeFeignCLient implements VodClient {

        //出错再执行

    //删一个视频，如果原方法出错了，就调用这个方法
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错！");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错！");
    }
}
