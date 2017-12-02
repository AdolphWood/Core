package com.adolph.test;

import com.adolph.beans.Customer;
import com.adolph.beans.Message;
import com.adolph.beans.Order;
import com.adolph.beans.People;
import com.adolph.mapper.OrderMapper;
import com.adolph.mapper.PeopleMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisTest {
    private ApplicationContext ctx = null;
    private PeopleMapper peopleMapper = null;
    private SqlSessionFactory sessionFactory = null;
    private OrderMapper orderMapper = null;
    {
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        peopleMapper = ctx.getBean(PeopleMapper.class);
        orderMapper = ctx.getBean(OrderMapper.class);
        sessionFactory = ctx.getBean(SqlSessionFactory.class);
    }
    @Test
    public void testSQLSession() throws IOException {
        //加载 mybatis 的配置文件
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //利用 mybatis 的配置文件来参建 SqlSessionFactory
        //SqlSessionFactory 类似于 hibernate 的 SessionFactory
        //用于创建 SqlSession 对象.
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //获取一个 SqlSession 对象.
        //SqlSession 类似于 hibernate 的 Session
        //实际上是 mybatis 应用程序和数据库的一次会话. 实际上就是一个连接.
        SqlSession session = sqlSessionFactory.openSession();

        try {
            //调用 SqlSession 的 selectOne 方法执行查询
            //两个参数
            //参数一: 利用 Mapper 文件的 namespace 和 Mapper 中的 id 唯一定位到一个 select(insert, update, delete) 节点.
            //参数二: 传入 SQL 的参数
            Message message = session.selectOne("com.adolph.mapper.MessageMapper.selectMessage", 9);
            System.out.println(message);
        } finally {
            session.close();
        }
    }
    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }
    @Test
    public void testPeopleBeans(){
        System.out.println(peopleMapper.getClass().getName());
        People people = peopleMapper.getById(5);
        System.out.println(people);
    }
    @Test
    @Transactional
    public void testMapperXML() {
        People people = new People();

        // people.setLastName("saveId");
        // people.setFirstName("saveId");
        // peopleMapper.saveId(people, 10);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("firstName", "saveByMap");
        params.put("lastName", "saveByMap");
        peopleMapper.saveMap(params);

        peopleMapper.saveParams("saveByParams", 30);

        people.setLastName("saveByPeople");
        people.setFirstName("saveByPeople");
        people.setAge(18);
        peopleMapper.savePeople(people);

        People peopleSelect = peopleMapper.selectPeople(10);
        System.out.println(peopleSelect.toString());
    }
    @Test
    public void testDatabaseMetadataProductName() throws SQLException{
        SqlSession session = sessionFactory.openSession();
        Connection connection = session.getConnection();

        DatabaseMetaData dmd = connection.getMetaData();
        String databaseProductName = dmd.getDatabaseProductName();
        System.out.println(databaseProductName);
    }
    @Test
    public void testDatabaseId(){
        People people = new People();
        people.setFirstName("Sue");
        people.setLastName("Wind");

        peopleMapper.testDatabaseId(people);
    }
    @Test
    public void testPrimaryKey(){
        Order order = new Order();
        order.setOrderNumber("XYZ....1234");

        Customer customer = new Customer();
        customer.setId(3);
        order.setCustomer(customer);

        orderMapper.save(order);
        System.out.println(order.getId());
    }
    @Test
    public void testOrderMapperGetAll(){
        List<Order> orders = orderMapper.getAll();

        for(Order order: orders){
            System.out.println(order.getId() + "," + order.getOrderNumber());
        }
    }
    @Test
    public void testCollection1(){
        Customer customer = orderMapper.selectCustomer2(1);
        System.out.println(customer);

        for(Order order: customer.getOrders()){
            System.out.println(order);
        }
    }
    @Test
    public void testCollection2(){
        Customer customer = orderMapper.selectCustomer3(1);
        System.out.println(customer);

        System.out.println("---------------------------------------");

        for(Order order: customer.getOrders()){
            System.out.println(order);
        }
    }
    @Test
    public void testResultMap(){
        Order order = orderMapper.testResultMap(3);
        System.out.println(order);
    }
    @Test
    public void testAssociation(){
        Order order = orderMapper.testAssociation(3);

        System.out.println(order);
    }
    @Test
    public void testAssociation2(){
        Order order = orderMapper.testAssociation2(3);
        System.out.println(order.getId() + "," + order.getOrderNumber());
        System.out.println("------------------------------------------------------------");
        System.out.println(order.getCustomer());
    }
    @Test
    public void testAssociation3(){
        Order order = orderMapper.testAssociation3(3);
        System.out.println(order.getId() + "," + order.getOrderNumber());
        System.out.println(order.getCustomer());
    }
    @Test
    public void testFirstLevelCache(){
        SqlSession session = sessionFactory.openSession();

        Order order1 = session.selectOne("com.adolph.mapper.OrderMapper.testCache", 1);
        System.out.println("------------------------------");
        Order order2 = session.selectOne("com.adolph.mapper.OrderMapper.testCache", 1);

        System.out.println(order1 == order2);

//		Order order1 = orderMapper.testCache(1);
//		System.out.println("------------------------------");
//
//		Order order2 = orderMapper.testCache(1);
//		System.out.println(order1 == order2);
    }
    @Test
    public void testSecondLevelCache() throws InterruptedException{
        SqlSession session = null;

        session = sessionFactory.openSession();
        Order order1 = session.selectOne("com.adolph.mapper.OrderMapper.testCache", 1);
        session.close();

        Thread.sleep(5000);

        System.out.println("------------------------------");
        session = sessionFactory.openSession();
        Order order2 = session.selectOne("com.adolph.mapper.OrderMapper.testCache", 1);
        session.close();

        System.out.println(order1 == order2);
    }

}
