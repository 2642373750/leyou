package com.zs.ssm.service;

import com.zs.ssm.pojo.Role;

import java.util.List;

public interface RoleService {

    public List<Role> findAll() throws Exception;

    public void save(Role role) throws Exception;
}
