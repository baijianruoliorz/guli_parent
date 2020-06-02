package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liqiqi_tql
 * @date 2020/6/1 -15:40
 */
@Component
@FeignClient("service-order")
public interface OrdersClient {

    //根据课程id和用户id查询订单表中的订单状态啊
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId
            , @PathVariable("memberId") String memberId);
}
