package com.zs.ssm.service;

import com.zs.ssm.pojo.Permission;

import java.util.List;

public interface PermissionService {


    public List<Permission> findAll() throws Exception;
}
