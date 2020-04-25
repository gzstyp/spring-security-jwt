package com.fwtai.dao;

import com.fwtai.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao{

	void save(final User user);

	User findByUsername(String name);
	
}