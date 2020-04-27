package com.fwtai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 页面跳转
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-27 19:20
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Controller
public class PageController{

    @Autowired
    private HttpServletRequest request;

    @GetMapping({"/","index"})
    public String index(final HttpServletResponse response){
        return "index";
    }
}