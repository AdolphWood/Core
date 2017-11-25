package com.adolph.service.impl;

import com.adolph.entity.User;
import com.adolph.dao.mapper.UserMapper;
import com.adolph.entity.UserExample;
import com.adolph.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers(Integer userId) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (!"".equals(userId == null ? "" : userId)) {
            criteria.andIdEqualTo(userId);
        }
        return userMapper.selectByExample(example);

    }
}
