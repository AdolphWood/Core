package com.adolph.mapper;

import com.adolph.entity.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {
	User getByName(@Param("name") String name);
}
