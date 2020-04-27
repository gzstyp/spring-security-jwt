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

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    private final static long expiry = 1000 * 60 * 45;

    private final static String issuer = "贵州富翁泰科技有限责任公司";

    private final static String secret = "V1JGR0dEZDJRZzAyYUhCVkhjVjZ1Umg5bHZvOG05VlVYd0FMUXlydEZOQUxhcitLWjM5ZitjNjR0WlgwSFBOQg==";

    private final static String getWeakKey(){
        final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    //创建
    public final static String createToken(final String userName,final String roles){
        final Date date = new Date();
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtBuilder builder = Jwts.builder().signWith(key,SignatureAlgorithm.HS384);
        return builder.setSubject(userName).claim("roles",roles).setIssuer(issuer).setIssuedAt(date).setExpiration(new Date(date.getTime() + expiry)).compact();
    }

    public final static String extractRole(final String token){
        return parserToken(token).get("roles",String.class);
    }

    public final static String extractUsername(final String token){
        return parserToken(token).getSubject();
    }

    //解析
    private final static Claims parserToken(final String token){
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}