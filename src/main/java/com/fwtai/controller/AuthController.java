package com.fwtai.controller;

import com.fwtai.entity.User;
import com.fwtai.service.UserService;
import com.fwtai.tool.ToolSHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // http://192.168.1.102:8091/index.html
    @PostMapping("/register")
    public String registerUser(final HttpServletRequest request){
        final String userName = request.getParameter("userName");
        final String password = request.getParameter("password");
        final User user = new User();
        user.setUsername(userName);
        user.setPassword(ToolSHA.encoder(password));
        user.setRole("ROLE_USER");
        userService.save(user);
        return "注册成功";
    }
}