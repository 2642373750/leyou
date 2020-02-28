package com.zs.ssm.dao;

import com.zs.ssm.pojo.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {

    @Select("select * from role where id in (select roleId from users_role where userId=#{id})")
    public List<Role> findById(String id) throws Exception;

    @Select("select * from role where id in (select roleId from users_role where userId=#{id})")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = java.util.List.class,many = @Many(select="com.zs.ssm.dao.PermissionDao.findById")),
    })
    public List<Role> findById02(String id) throws Exception;

    @Select("select * from role")
    public List<Role> findAll() throws Exception;

    @Insert("insert into role(roleName,roleDesc) values(#{roleName},#{roleDesc})")
    public void save(Role role) throws Exception;
}
