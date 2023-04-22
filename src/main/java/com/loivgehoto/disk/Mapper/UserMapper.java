package com.loivgehoto.disk.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loivgehoto.disk.Model.User;


public interface UserMapper extends BaseMapper<User>//Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
{
//    @Select("select name from user where name =#{username}  and password = #{password}")


    public String checkUser(User user);

    public String Register_check(String user_name);

    public void Register(User user);


}
