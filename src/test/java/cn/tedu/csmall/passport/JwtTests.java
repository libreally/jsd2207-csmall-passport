package cn.tedu.csmall.passport;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTests {
    @Test
    public void test(){
        String secretKey="sdhfk3skldh5HFGKhg";
        Date date=new Date(System.currentTimeMillis()+30*60*60*1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",348975);
        claims.put("username","chengheng");
        String jwt=Jwts.builder()
                .setHeaderParam("alg","HS256")
                .setHeaderParam("tyb","JWT")
                .setClaims(claims)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        System.out.println("jwt = " + jwt);
    }
    @Test
    public void parse(){
        String jwt="eyJhbGciOiJIUzI1NiIsInR5YiI6IkpXVCJ9.eyJleHAiOjE2Njc5NzkxNTgsInVzZXJuYW1lIjoicm9vdCJ9.dw4hJ8Jd0XJv2PNf3n-O7_piBe6xXBxtyQKqghLeBHM";
        String secretKey="sdhfk3skldh5HFGKhg";
        Claims claims=Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        Object id=claims.get("id");
        Object username=claims.get("username");
        System.out.println("id = " + id);
        System.out.println("username = " + username);
    }

}
