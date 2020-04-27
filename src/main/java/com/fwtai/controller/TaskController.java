package com.fwtai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping("/getTasks")
    @ResponseBody
    public String listTasks(){
        return "任务列表";
    }

    @GetMapping("/newTasks")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//角色必须以大写的ROLE_开头(即数据库存的必须是以ROLE_开头)
    public String newTasks(){
        return "创建了一个新的任务";
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('edit')")//权限不区分大小写也以一定是以的ROLE_开头
    public String edit(){
        return "有edit权限";
    }
}