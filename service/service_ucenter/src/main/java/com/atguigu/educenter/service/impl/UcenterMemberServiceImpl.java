package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.MD5;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-31
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 登录的方法
    //用户名，密码，还有手机号 is_disable表示是否被禁用
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();
        //进行手机号和密码非空的判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败");
        }
        //判断手机号是否正确 service里面调用basemapper
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);

        //判断查询对象是否为空
        if (mobileMember == null) {
            throw new GuliException(20001, "登录失败，手机号可能不存在！");
        }
        //如果不等于空,判断密码
        //单纯判断密码的话，这个方法肯定会抛异常，因为数据库存储的密码进过了加密，而输入的密码没有进行加密，所以自己
        //写的密码也需要进行加密再匹配,MD5加密
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new GuliException(20001, "登陆失败，密码不匹配哦");
        }
        //判断用户是否禁用
        if (mobileMember.getIsDisabled()) {
            throw new GuliException(20001, "登录失败,可能被禁用了呢");
        }

        //登录成功
        //生成token字符串，使用jwt
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
    //把数据加到数据库就行了，然后判断一下验证码
        String code = registerVo.getCode(); //验证码
        String mobile = registerVo.getMobile(); //手机号
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)
        ||StringUtils.isEmpty(nickname)){
            throw new GuliException(20001,"注册失败，可能是有些信息不匹配呢");
        }
        //判断验证码 验证码在redis中
        String redisCode = redisTemplate.opsForValue().get(mobile);//手机号中的验证码

        if(!code.equals(redisCode)){
            throw new GuliException(20001,"验证码不对呢");
        }
        //手机号不能重复
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            //有数据
            throw new GuliException(20001,"手机号已经注册了哦~");
        }
        //数据添加到数据库中
        UcenterMember member=new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//存入数据库的密码进行加密
        member.setIsDisabled(false);//不禁用
        member.setAvatar("https://edu-1014.oss-cn-beijing.aliyuncs.com/OIPXAUSLXYG.jpg");//一个默认头像
        baseMapper.insert(member);

    }
   //根据openid..
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }


}
