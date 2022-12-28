package com.example.mybatis;

import com.example.mybatis.entity.User;
import com.example.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisApplicationTests {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void contextLoads() {
    }

    /**
     * 在同一个sqlSession中两次执行相同的sql语句，第一次执行完毕会将数据库中查询的数据写到缓存（内存），
     * 第二次会从缓存中获取数据将不再从数据库查询，从而提高查询效率。
     * 如果没有声明需要刷新，并且缓存没有超时的情况下，SqlSession都会取出当前缓存的数据，而不会再次发送SQL到数据库。
     * 当一个sqlSession结束后该sqlSession中的一级缓存也就不存在了。
     * Mybatis默认开启一级缓存。
     */
    @Test
    public void test1() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //第一次查询
        User user = userMapper.findById(Long.valueOf(2));
        System.out.println(user);
        //第二次查询
        User user1 = userMapper.findById(Long.valueOf(2));
        System.out.println(user1);
    }

    /**
     * 可以看出，我们还是执行的相同的查询操作，但是由于不是处于同一sqlSession下，所以还是进行了两次数据库查询操作。
     */
    @Test
    public void test2() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
        SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
        UserMapper userMapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
        //第一次查询
        User user = userMapper1.findById(Long.valueOf(2));
        System.out.println(user);
        //第二次查询
        User user1 = userMapper2.findById(Long.valueOf(2));
        System.out.println(user1);
    }

    /**
     * 可以看出由于进行了更新操作，所以第二次查询的时候还是进行了数据库查询而不是从缓存中读取的数据。
     */
    @Test
    public void test3() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //第一次查询
        User user = userMapper.findById(Long.valueOf(2));
        System.out.println(user);
        //更新操作
        User user3 = new User();
        user3.setId(2);
        user3.setUserName("baba");
        user3.setUserAge(4);
        userMapper.update(user3);
        //第二次查询
        User user1 = userMapper.findById(Long.valueOf(2));
        System.out.println(user1);
    }

    @Test
    public void secondLevelCache(){
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        SqlSession sqlSession3 = sqlSessionFactory.openSession();

        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        UserMapper mapper3 = sqlSession3.getMapper(UserMapper.class);
        //第一次查询
        User user = mapper1.findById(Long.valueOf(2));
        System.out.println(user);
        //关流，将数据存入二级缓存中，不然不会向二级缓存中存数据
        sqlSession1.close();

        //第二次查询
        User user1 = mapper2.findById(Long.valueOf(2));
        System.out.println(user1);
        sqlSession2.close();
    }

}
