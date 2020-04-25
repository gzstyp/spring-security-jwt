package com.fwtai.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * json web token
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-25 14:42
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class ToolJWT implements Serializable{

    private final static long expiry = 1000 * 60 * 45;

    private final static String issuer = "贵州富翁泰科技有限责任公司";

    private final static String secret = "V1JGR0dEZDJRZzAyYUhCVkhjVjZ1Umg5bHZvOG05VlVYd0FMUXlydEZOQUxhcitLWjM5ZitjNjR0WlgwSFBOQg==";

    private final static String getWeakKey(){
        final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private final static String create(final String id,final String subject){
        final Date date = new Date();
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtBuilder builder = Jwts.builder().signWith(key,SignatureAlgorithm.HS384);
        builder.setId(id).setIssuer(issuer).setIssuedAt(date).setExpiration(new Date(date.getTime() + expiry)).setSubject(subject);
        return builder.compact();
    }

    //解析
    private static Claims parser(final String token){
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}