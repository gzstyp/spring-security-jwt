package com.fwtai.service;

import com.fwtai.dao.UserDao;
import com.fwtai.entity.JwtUser;
import com.fwtai.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Resource
	private UserDao dao;
	
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
    	
        final User user = dao.findByUsername(s);
        
        return new JwtUser(user);
    }

}