package com.zs.ssm.service;

import com.zs.ssm.pojo.Role;
import com.zs.ssm.pojo.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService{

    public List<UserInfo> findAll() throws Exception;

    public void save(UserInfo userInfo) throws  Exception;

    public UserInfo findById(String id) throws Exception;

    public List<Role> findUserByIdAndAllRole(String id) throws Exception;

    public void addRoleToUser(String userId,String[] roleIds) throws  Exception;
}
