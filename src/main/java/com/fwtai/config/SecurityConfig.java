package com.fwtai.config;

import com.fwtai.AuthExceptionPoint;
import com.fwtai.filter.JWTAuthenticationFilter;
import com.fwtai.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
   配置SpringSecurity，将各“组件”组合到一起发挥作用了，那就需要配置了。
   需要开启一下注解@EnableWebSecurity然后再继承一下WebSecurityConfigurerAdapter 即可
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    //登录认证(多种认证方式中的一种)
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new Passworder());
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring()
          .antMatchers(HttpMethod.OPTIONS, "/**")
          // allow anonymous resource requests(允许匿名资源请求)
          .antMatchers(
            "/",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "*.html",
            "/**/*.css",
            "/**/*.js"
          );
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        //.cors().and()
        http.csrf().disable()//.ignoringAntMatchers("/h2-console/**"); // 禁用 H2 控制台的 CSRF 防护
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))//登录拦截器
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))//鉴权过滤器
            // 不需要session(create no session)禁用
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(new AuthExceptionPoint());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
