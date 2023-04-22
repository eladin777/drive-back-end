package com.loivgehoto.disk.Util;

import com.loivgehoto.disk.Model.User;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JjwtUtil
{

    /////一天后过期
    private static long time= 1000*60*60*3;
    private static String signature="loivgehotofsdf84ht34h09jgjfj40g0fdgdfsdfvbetet43";


    //////配置jjwt
    public String jwt_token(String username)
    {
        JwtBuilder jwtBuilder= Jwts.builder();
        String jwt_token=jwtBuilder.
                ///HEADER
                setHeaderParam("typ","JWT").
                setHeaderParam("alg","HS256")
                ////payload
                .claim("username",username)
                .claim("role","player")
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                ///signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();

//        System.out.println(jwt_token);
        return jwt_token;

    }

    public Boolean decrypt_token(String token)
    {
//        User user=new User();

        Jws<Claims> claimsJws;

//        Claims claims;

        if(token==null||token.equals(""))
            return false;


        JwtParserBuilder jwtParser=Jwts.parserBuilder();



        try
        {
            claimsJws=jwtParser.setSigningKey(signature).build().parseClaimsJws(token);

//            user.setName(claimsJws.getBody().get("username",String.class));
//            System.out.println("解码为："+user.getName());

        }catch (Exception e)
        {
            return false;
        }




//        user.setName(claimsJws.getBody().get("username",String));
        return true;
//        Claims claims=claimsJws.getBody();

    }




//    public String getToken(User user, long time) {
//    Date start = new Date();//token起始时间
//    long currentTime = System.currentTimeMillis() + time;
//    Date end = new Date(currentTime);//token结束时间
//    String token = "";
//    token = JWT.create()
//            .withAudience(user.getLevel().toString()+user.getId().toString()) //存放接收方的信息
//            .withIssuedAt(start)//token开始时间
//            .withExpiresAt(end)//token存活截止时间
//            .sign(Algorithm.HMAC256(user.getPassword()));//加密
//    return token;
//}

}
