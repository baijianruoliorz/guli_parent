package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqiqi_tql
 * @date 2020/5/28 -9:27
 */
//以后照着改就行！！
@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {


    //login方法,根据vue-admin-template里store的user.js,可知登陆要返回token值，所以这里必须有token
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    //info方法,获取用户信息
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://edu-1014.oss-cn-beijing.aliyuncs.com/OIPXAUSLXYG.jpg");
    }
}
