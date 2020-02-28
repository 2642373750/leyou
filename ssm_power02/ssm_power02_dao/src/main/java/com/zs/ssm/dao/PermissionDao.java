package com.zs.ssm.dao;

import com.zs.ssm.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionDao {

    @Select("select * from permission where id in (select permissionId from role_permission where roleId=#{id})")
    public List<Permission> findById(String id) throws Exception;

    @Select("select * from permission")
    public List<Permission> findAll() throws Exception;
}
