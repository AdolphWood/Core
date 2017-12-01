package com.adolph.test;

import com.adolph.beans.Message;
import com.adolph.beans.People;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MybatisTest {

    private ApplicationContext ctx = null;
    private PeopleMapper peopleMapper = null;

    {
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        peopleMapper = ctx.getBean(PeopleMapper.class);
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
}
