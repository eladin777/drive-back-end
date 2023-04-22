package com.loivgehoto.disk.Service;

import com.loivgehoto.disk.Mapper.UserMapper;
//import com.loivgehoto.disk.Util.MybatisUtil;
import com.loivgehoto.disk.Model.User;
//import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import javax.annotation.Resource;
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;

@Service
public class UserService
{
    @Autowired
    private UserMapper userMapper;

    public String checkUser(User user)
    {
//        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
//        UserMapper map=sqlSession.getMapper(UserMapper.class);
//      MybatisUtil.closeSqlSession(sqlSession);

        return userMapper.checkUser(user);
    }

    public String Register_check(String user_name)
    {
//        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
//        UserMapper map=sqlSession.getMapper(UserMapper.class);
////      MybatisUtil.closeSqlSession(sqlSession);
        return userMapper.Register_check(user_name);
    }

    public void Register(User user)
    {
//        SqlSession sqlSession= MybatisUtil.createSQLsession();//////////util类没写好总是空指针
//        UserMapper map=sqlSession.getMapper(UserMapper.class);
        userMapper.Register(user);
//        MybatisUtil.closeSqlSession(sqlSession);/////////important!!!!!!!!!!不加这句关闭数据库连接直接导致注册用户后执行的账户信息插入语句无效，数据库不会插入任何信息；加了之后正常


    }

}
