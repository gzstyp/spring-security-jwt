package com.fwtai.filter;

import com.fwtai.tool.ToolJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证成功当然就是进行鉴权过滤器了,即登录成功之后走此类进行鉴权操作

 验证成功当然就是进行鉴权了，每一次需要权限的请求都需要检查该用户是否有该权限去操作该资源，当然这也是框架帮我们做的，
 那么我们需要做什么呢？很简单，只要告诉spring-security该用户是否已登录，是什么角色，拥有什么权限就可以了。
 JWTAuthenticationFilter继承于BasicAuthenticationFilter，至于为什么要继承这个我也不太清楚了，
 这个我也是网上看到的其中一种实现，实在springSecurity苦手，
 不过我觉得不继承这个也没事呢（实现以下filter接口或者继承其他filter实现子类也可以吧）只要确保过滤器的顺序，
 JWTAuthorizationFilter在JWTAuthenticationFilter后面就没问题了。

*/
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException{
        final String tokenHeader = request.getHeader(ToolJWT.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if(tokenHeader == null || !tokenHeader.startsWith(ToolJWT.TOKEN_PREFIX)){
            chain.doFilter(request,response);
        }else{
            // 如果请求头中有token，则进行解析，并且设置认证信息
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
            super.doFilterInternal(request,response,chain);
        }
    }

    // 这里从token中获取用户信息并新建一个token,需将 List<Authority> 转成 List<SimpleGrantedAuthority>，否则前端拿不到角色列表名称
    private UsernamePasswordAuthenticationToken getAuthentication(final String tokenHeader){
        final String token = tokenHeader.replace(ToolJWT.TOKEN_PREFIX,"");
        final String username = ToolJWT.extractUsername(token);
        final String role = ToolJWT.extractRole(token);
        if(username != null){
            final String[] roles = role.split(",");
            final List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for(final String value : roles){
                authorities.add(new SimpleGrantedAuthority(value));
            }
            authorities.add(new SimpleGrantedAuthority("edit"));//一般情况下,角色是以ROLE_开头且是大写的,而权限可以是不区分大小写的,即@PreAuthorize("hasRole('ROLE_ADMIN')"),@PreAuthorize("hasAuthority('edit')")
            //return new UsernamePasswordAuthenticationToken(username,null,Collections.singleton(new SimpleGrantedAuthority(role)));//单个角色或权限
            return new UsernamePasswordAuthenticationToken(username,null,authorities);//多个角色或权限
        }
        return null;
    }
}
