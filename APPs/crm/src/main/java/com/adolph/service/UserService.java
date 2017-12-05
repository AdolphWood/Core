package com.adolph.service;

import com.adolph.entity.User;
import com.adolph.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Transactional(readOnly=true)
	public User login(String name, String password){
		User user = userMapper.getByName(name);
		
		if(user != null 
				&& user.getEnabled() == 1
				&& user.getPassword().equals(password)){
			return user;
		}
		
		return null;
	}
}
