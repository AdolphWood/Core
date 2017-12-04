package com.adolph.mapper;

import com.adolph.beans.Customer;
import com.adolph.beans.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    List<Order> getAll();

    void save(Order order);

    Order testResultMap(@Param("id") Integer id);

    Order testAssociation(@Param("id") Integer id);

    Order testAssociation2(@Param("id") Integer id);

    Order testAssociation3(@Param("id") Integer id);

    Customer selectCustomer2(@Param("id") Integer id);

    Customer selectCustomer3(@Param("id") Integer id);

    Order testCache(@Param("id") Integer id);

    List<Order> testIf(Map<String, Object> params);

    List<Order> testForEach(@Param("ids") List<Integer> ids);
}
