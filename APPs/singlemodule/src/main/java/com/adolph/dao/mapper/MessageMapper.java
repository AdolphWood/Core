package com.adolph.dao.mapper;

import com.adolph.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper {
    Message selectMessage(@Param("id") int id);
}
