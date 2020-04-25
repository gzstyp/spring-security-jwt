package com.fwtai.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJwtUtils {
	
	public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String SUBJECT = "congge";

	public static final long EXPIRITION = 1000 * 24 * 60 * 60 * 7;

	public static final String APPSECRET_KEY = "congge_secret";

    private static final String ROLE_CLAIMS = "rol";

    private final static long expiry = 1000 * 60 * 45;

    private final static String issuer = "贵州富翁泰科技有限责任公司";

    private final static String secret = "V1JGR0dEZDJRZzAyYUhCVkhjVjZ1Umg5bHZvOG05VlVYd0FMUXlydEZOQUxhcitLWjM5ZitjNjR0WlgwSFBOQg==";

    private final static String getWeakKey(){
        final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS384);
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public final static String createToken(final String username,final String role){
        final Date date = new Date();
        final Map<String,String> claims = new HashMap<>();
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        claims.put(ROLE_CLAIMS,role);
        final JwtBuilder builder = Jwts.builder().signWith(key,SignatureAlgorithm.HS384);
        builder.claim("username",username).setClaims(claims).setIssuer(issuer).setIssuedAt(date).setExpiration(new Date(date.getTime() + expiry)).setSubject(username);
        return builder.compact();
    }

    //解析
    private static Claims parser(final String token){
        final Key key = Keys.hmacShaKeyFor(secret.getBytes());
        final JwtParserBuilder builder = Jwts.parserBuilder();
        return builder.requireIssuer(issuer).setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

	public static Claims checkJWT(String token) {
		try {
			final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
			return claims;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取用户名
	 * @param token
	 * @return
	 */
	public static String getUsername(final String token){
        final Claims claims = parser(token);
    	return claims.get("username").toString();
    }
	
	/**
	 * 获取用户角色
	 * @param token
	 * @return
	 */
    public static String getUserRole(String token){
    	final Claims claims = parser(token);
    	return claims.get("rol").toString();
    }
}
