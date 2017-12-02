package com.adolph.mapper;

import com.adolph.beans.People;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface PeopleMapper {
    void testDatabaseId(People people);

    void saveId(People people, @Param("id") Integer id);

    void saveMap(Map<String, Object> params);

    void saveParams(@Param("lastName") String lastName,@Param("age") int age);

    void savePeople(People people);

    People selectPeople(@Param("id") Integer id);

    @Select("SELECT id, last_name AS \"lastName\" FROM people WHERE id = #{id}")
    People getById(Integer id);

}
