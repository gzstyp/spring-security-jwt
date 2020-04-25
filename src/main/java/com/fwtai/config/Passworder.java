package com.fwtai.config;

import com.fwtai.tool.ToolSHA;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码器,其中 rawPassword 原始密码
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-13 16:45
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class Passworder implements PasswordEncoder{

    @Override
    public String encode(final CharSequence rawPassword){
        return ToolSHA.encoder(rawPassword);
    }

    @Override// 其中rawPassword 是未密码的密码,encodedPassword是加密后的密码即是数据库保存的密码,也就是上面的方法encode()处理后的密码
    public boolean matches(final CharSequence rawPassword,final String encodedPassword){
        return encodedPassword.equals(ToolSHA.encoder(rawPassword));
    }
}