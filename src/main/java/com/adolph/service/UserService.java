package com.adolph.service;

import com.adolph.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(Integer userId);
}
