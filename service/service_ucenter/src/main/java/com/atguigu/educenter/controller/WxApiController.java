package com.atguigu.educenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author liqiqi_tql
 * @date 2020/5/31 -16:16
 */
@CrossOrigin
@Controller  //不用RestController 是为了重定向，因为rest..会自动转成json格式
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //生成微信二维码
    @GetMapping("login")
    public String getWxCode(){
        //请求微信的一个地址，比较固定了
        //固定地址，后面拼接参数下面这种拼接很辣鸡
//        String url = "https://open.weixin.qq.com/" +
//                "connect/qrconnect?appid="+ ConstantWxUtils.WX_OPEN_APP_ID+"&response_type=code";
     //这波是常用的办法,%相当于？占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_REDIRECT_URL,
                redirectUrl,
                "atguigu"
        );
        //这波是重定向
        return "redirect:"+url;

    }

  /*
  * http://localhost:8150/api/ucenter/
  * wx/callback?code=081ADoIg0oqejt1cAJIg0mWFIg0ADoIr&state=
  * http://guli.shop/api/ucenter/wx/callback
  * */
  //扫描只会跳到这个地址，在域名地址里面写了程序做处理请求一个地址
    //所以我们要把本地的端口号改成8150，而且这个方法只能叫callback
    //注意的是实际公司并不需要这么做，他们有部署到域名服务器就行了。。
    @GetMapping("callback")
    public String callback(String code,String state){
        try {


            //调用这个方法，扫完二位码之后就跳转到这个地址
            //这里要得到扫码人信息，进行登录，这里在微信官方已经给出了答案呢
            //1.扫描之后，callback会得到两个值
            //state:原样传递的一个值 “com.atguigu”
            //code:类似于手机的验证码，随机的唯一一个值
            //2.拿着第一步获得d code值请求一个固定地址，获取到两个值
            //access_token 访问凭证
            //openid:每个微信的唯一标识
            //3.拿着第二步获取的两个值，再去请求一个微信提供的固定地址，最终获取到微信扫码人的信息：昵称，头像。。
            //用户确认后就能获取信息


            /*实现   ++++++++++++++++++++++*/

            //获取code值，参数里有code就行了
            //拿着code去请求固定地址，得到两个值
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接三个参数 ：id  秘钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );


            //请求这个拼接的地址，得到两个返回值access_token和openid
            //可以直接使用httpClient:不需要浏览器直接就请求啦！


            //这里有异常
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //以下是一段返回实例

            /*
            * {"access_token":
            * "33_Gqzp9fhp9Q29PTEI0AhDif1wREsoHwE6nneRufYuf5npHF4JqiT6kHoGFU9bOSrQ1mK0jVjCvGRLc1rVdMDHZ8cz3kyq_gfMonJ_D1Q2PM4",
            * "expires_in":7200,//过期时间      "refresh_token":"33_5oSc6GA-oKyDqR-qyeSKOHdNUZ3NEmSDTxbWjL2armXdw9goGqfagSJ7WuDDPE8kNk7-P8xl2fSRlwxZlQCvUq8rUxQm7asbIucKBXvpu7o",
            * "openid":"o3_SC5yeAZKlQrhk-2asqTi-OtIY",  //谁扫的就是谁的id这是我的
            * "scope":"snsapi_login","unionid":"oWgGz1LJSROsr2_6xrY5UzDGVags"}
            *
            * */

            //由实例输出可知返回的是json，我们得从返回值获取出来两个值
            //这个时候就可以有很多方法，我们使用gson --google爸爸提供的

            //先转成map
            //把accessTokenInfo转换成map,根据key来获取相应的值
            //使用gson，并不是因为它很强大，而是因为google爸爸tql orz
            Gson gson=new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            //key的值已经固定了
            String access_token= (String) mapAccessToken.get("access_token");
            String openid= (String) mapAccessToken.get("openid");


            //把扫码人信息添加到数据库
            //判断数据库表里面是否存在相同的信息
          UcenterMember member= memberService.getOpenIdMember(openid);
            //空就添加
          if(member==null){
              //拿着得到的两个值，再去访问固定地址来得到扫码人信息
              String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                      "?access_token=%s" +
                      "&openid=%s";
              //拼接两个参数

              String userInfoUrl = String.format(
                      baseUserInfoUrl,
                      access_token,
                      openid
              );

              //请求这个网址得到扫码人信息
              String userInfo = HttpClientUtils.get(userInfoUrl);

              //获取返回的扫码人信息
              //把字符串变成map集合
              HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
              String nickname = (String) userInfoMap.get("nickname");
              String headimgurl = (String) userInfoMap.get("headimgurl");

              member=new UcenterMember();
              member.setOpenid(openid);
              member.setNickname(nickname);
              //头像是一个地址
              member.setAvatar(headimgurl);
              memberService.save(member);
          }
          //使用jwt根据微信信息生成token,解决cookie的跨域问题
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后一步，回到首页面，回显信息

            return "redirect:http://localhost:3000?token="+jwtToken;



        }catch (Exception e){
            throw new GuliException(20001,"扫码出问题了嗯嗯");
        }

    }

}
