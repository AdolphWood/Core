package com.adolph.test;

import com.adolph.beans.Message;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class MybatisTest {
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
}
