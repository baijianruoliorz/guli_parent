package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-31
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {
  @Autowired
    private UcenterMemberService memberService;

  //登陆的方法
    //最原始方法：根据用户民密码到数据库中查,一定要用post提交哦，最开始用get忘记了..导致错了很久
//    @PostMapping("login")
//    public  R loginUser(@RequestBody UcenterMember member){
//        //调用Service中的方法进行登录
//        //登录之后返回token,token根据jwt生产
//      String token=  memberService.login(member);
//      return R.ok().data("token",token);
//    }
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
//        调用service中的方法进行登录
//        登录之后返回token token由jwt生产
        String token=memberService.login(member);
        return R.ok().data("token",token);
    }


    //注册的方法：由于初始实体类没有验证码，所以再创建一个实体类专门封装验证码
   @PostMapping("register")
  public R register(@RequestBody RegisterVo registerVo){
      memberService.register(registerVo);
      return R.ok();
   }

   //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //调用jwt工具类方法，根据request返回头信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member=memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }
}

