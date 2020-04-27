package com.fwtai.filter;

import com.fwtai.entity.JwtUser;
import com.fwtai.tool.ToolJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * 登录拦截器，验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 ,
 * attemptAuthentication：接收并解析用户凭证。 
 * successfulAuthentication：用户成功登录后，这个方法会被调用，我们在这个方法里生成token并返回。

 JWTAuthenticationFilter继承于UsernamePasswordAuthenticationFilter
 该拦截器用于获取用户登录的信息，只需创建一个token并调用authenticationManager.authenticate()让spring-security去进行验证就可以了，
 不用自己查数据库再对比密码了，这一步交给spring去操作。
 这个操作有点像是shiro的subject.login(new UsernamePasswordToken())，验证的事情交给框架。

 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        // http://192.168.1.102:8091/auth/login
        super.setFilterProcessesUrl("/auth/login");//默认是 super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,HttpServletResponse response) throws AuthenticationException{
        final String userName = request.getParameter("userName");
        final String password = request.getParameter("password");
        if(userName == null || userName.length() <= 0){
            throw new UsernameNotFoundException("请输入登录账号");
        }if(password == null || password.length() <= 0){
            throw new UsernameNotFoundException("请输入登录密码");
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
    }

    // 成功验证后调用的方法,如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final FilterChain chain,final Authentication authResult) throws IOException, ServletException{
        final JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        System.out.println("jwtUser:" + jwtUser.toString());
        String role = "";
        final Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
        for (final GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }
        final String token = ToolJWT.createToken(jwtUser.getUsername(), role);
        // 返回创建成功的token,但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        final String tokenStr = ToolJWT.TOKEN_PREFIX + token;
        response.setHeader("Authorization",tokenStr);
        responseJson("登录成功",response);
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request,final HttpServletResponse response,final AuthenticationException failed) throws IOException, ServletException{
        if(failed instanceof UsernameNotFoundException){
            responseJson(failed.getMessage(),response);
        }else{
            responseJson("登录认证失败,账号或密码错误",response);
        }
    }

    private void responseJson(final String json,final HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-Control","no-cache");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(json);
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }
}
