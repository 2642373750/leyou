package com.zs.ssm.dao;

import com.zs.ssm.pojo.Role;
import com.zs.ssm.pojo.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserDao {

    @Select("select * from users where username=#{username}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "phoneNum",column = "phoneNum"),
            @Result(property = "email",column = "email"),
            @Result(property = "status",column = "status"),
            @Result(property = "roles",column = "id",javaType = java.util.List.class,many = @Many(select = "com.zs.ssm.dao.RoleDao.findById"))
    })
    public UserInfo findByUsername(String username)throws Exception;

    @Select("select * from users")
    public List<UserInfo> findAll() throws Exception;

    @Insert("insert into users(email,username,password,phoneNum,status) values(#{email},#{username},#{password},#{phoneNum},#{status})")
    public void save(UserInfo userInfo) throws  Exception;

    @Select("select * from users where id=#{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "phoneNum",column = "phoneNum"),
            @Result(property = "email",column = "email"),
            @Result(property = "status",column = "status"),
            @Result(property = "roles",column = "id",javaType = java.util.List.class,many = @Many(select = "com.zs.ssm.dao.RoleDao.findById02"))
    })
    public UserInfo findById(String id) throws Exception;

    @Select("select * from role where id not in (select roleId from users_role where userId=#{id})")
    public List<Role> findUserByIdAndAllRole(String id) throws Exception;

    @Insert("insert into users_role values(#{userId},#{roleId})")
    public void addRoleToUser(@Param("userId") String userId,@Param("roleId") String roleId) throws  Exception;
}
