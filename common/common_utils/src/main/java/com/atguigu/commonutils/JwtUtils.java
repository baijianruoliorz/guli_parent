package com.atguigu.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author helen          liqiqiorz
 * @since 2019/10/16      2020.05.30
 */
//工具类，建议保存方便以后随时使用，用的时候改改过期时间啥的就行了
public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";
   //生成token字符串
    public static String getJwtToken(String id, String nickname){
  //构建token字符串
        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")    //设置头信息
                .setHeaderParam("alg", "HS256")
                .setSubject("guli-user")

                .setIssuedAt(new Date())  //得到当前时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))  //设置过期时间

                .claim("id", id)  //设置token主体部分，存贮用户信息
                .claim("nickname", nickname)

                .signWith(SignatureAlgorithm.HS256, APP_SECRET)  //签名哈希
                .compact();

        return JwtToken;
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken
     * @return
     */
    //就是是否按jwt规则判断token是否有效啦。。
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request
     * @return
     */
    //这个也是判断是否有效的。。
    public static boolean checkToken(HttpServletRequest request) {
        try {
            //通过request判断。。
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     * @param request
     * @return
     */
    //取数据
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        //得到主体，根据key取value
        Claims claims = claimsJws.getBody();
        return (String)claims.get("id");
    }
}
